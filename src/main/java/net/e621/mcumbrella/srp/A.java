package net.e621.mcumbrella.srp;
import javax.mail.*;
import java.io.*;
import java.util.Properties;
public class A {
    public void readCfg(){

    }
    public static void main(String[] args) //throws MessagingException
    {
        System.out.println("[i] Starting.");
        //INIT__________________________________________
        String ver = null, user = null, pswd = null, to = null, file = null;
        Properties c=new Properties();
        try {

            BufferedReader f=new BufferedReader(new FileReader("cfg.properties"));
            c.load(f);
            ver=c.getProperty("ver");
            user=c.getProperty("user");
            pswd=c.getProperty("pswd");
            to=c.getProperty("to");
            file=c.getProperty("file");

        } catch (Throwable e) {
            System.out.println("[X] Error loading config:");e.printStackTrace();System.exit(-1);
        }
        if(args.length!=0&& args[0].equals("-d")){System.out.println("[D] Values:\nver  | "+ver+"\nuser | "+user+"\npswd | "+pswd+"\nto   | "+to+"\nfile | "+file);}
        if(
           ver==null||user==null||pswd==null||to==null||file==null||
           !ver.equals("1")||user.equals("")||pswd.equals("")||to.equals("")||file.equals("")
        ){System.out.println("[X] Error loading config: Incorrect config");System.exit(-1);}
        System.out.println("[i] Started.");
        //INIT END--------------------------------------

        //MAIN__________________________________________
        System.out.println("[i] Sending.");
        System.out.println("[i] Success.");
        //MAIN END--------------------------------------

        System.exit(0);
    }
}
