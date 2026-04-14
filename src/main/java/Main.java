import Moka7.S7;
import Moka7.S7Client;
import java.io.FileWriter;
import  java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws InterruptedException {
        //jpackage --type exe --name ProjetoGestamp --input . --main-jar .\ProjetoGestamp-1.0-SNAPSHOT.jar --win-console
        //Wix tool set v3
        S7Client plc = new S7Client();

        try{
            plc.ConnectTo("192.168.0.1",0,1);
            System.out.println(plc.Connected);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }

        byte[] dbBuffer = new byte[10];

        while (true){
            plc.ReadArea(S7.S7AreaDB, 1, 0,10, dbBuffer);

            System.out.println((S7.GetShortAt(dbBuffer,0)));

            Thread.sleep(1000);
        }
    }
}