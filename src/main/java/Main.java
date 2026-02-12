import Moka7.S7;
import Moka7.S7Client;
import java.io.FileWriter;
import  java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){

        S7Client plc = null;
        String csvPath = "f:/testeComm/teste.csv";
        String tmpPath = "f:/testeComm/teste.tmp";
        Scanner scanner = new Scanner(System.in);
        String plcIp;
        int result;

        try{
            plc = new S7Client();
        } catch(Exception e){
            System.out.println("Erro na criação do cliente");
            System.out.println(e.getMessage());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.out.println("Erro no primeiro sleep");
                throw new RuntimeException(ex);
            }
            return;
        }

        System.out.println("Digite o IP do clp");
        plcIp = scanner.next();
        scanner.close();

        try{
            result = plc.ConnectTo(plcIp,0,2);

            if(result == 0){
                System.out.println("Conectado com sucesso");
            } else {
                System.out.println("falha na conexão");
                return;
            }
        }catch (Exception e){
            System.out.println("Erro na conexão do clp");
            System.out.println(e.getMessage());
            try {
                Thread.sleep(5000);

            } catch (InterruptedException ex) {
                System.out.println("Erro no segundo sleep");
                throw new RuntimeException(ex);
            }
            return;
        }


        byte[] db = new byte[38];

        try {
            result = plc.ReadArea(S7.S7AreaDB, 1, 0, db.length, db);

            if (result != 0) {
                System.out.println("Erro de leitura");
                return;
            }
        } catch (Exception e){
            System.out.println("Erro ao ler a DB");
            System.out.println(e.getMessage());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }

        System.out.println(S7.GetShortAt(db,0));


        int numFerramenta = S7.GetShortAt(db, 0);
        int velocidade = S7.GetShortAt(db, 2);
        int contador = S7.GetShortAt(db, 4);

        char[] nomeFerramenta = new char[32];

        try {
            for (int i = 0; i <= 31; i++) {
                nomeFerramenta[i] = (char) (db[6 + i]);
            }
        } catch (Exception e){
            System.out.println("Erro ao montar nome da ferramenta");
            System.out.println(e.getMessage());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.out.println("Erro no montar ferramenta sleep");
                throw new RuntimeException(ex);
            }
        }
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(tmpPath, false));

            writer.append("num. Ferr;".concat(String.valueOf(numFerramenta)));
            writer.newLine();
            writer.append("velocidade;".concat(String.valueOf(velocidade)));
            writer.newLine();
            writer.append("contador;".concat(String.valueOf(contador)));
            writer.newLine();
            writer.append("nome;".concat(String.valueOf(nomeFerramenta)));
            writer.flush();
            writer.close();

        } catch (Exception e){
            System.out.println("Erro na leitura");
            System.out.println(e.getMessage());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.out.println("Erro no terceiro sleep");
                throw new RuntimeException(ex);
            }
            return;
        }

        try {
            Files.move(
                    Paths.get(tmpPath),
                    Paths.get(csvPath),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );
        } catch (Exception e) {
            System.out.println("Erro ao atualizar CSV final: " + e.getMessage());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.out.println("Erro no ultimo sleep");
                throw new RuntimeException(ex);
            }
        }

    }
}