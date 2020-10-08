package net.e621.mcumbrella.srp;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;
public class A {
    public void readCfg(){

    }
    public static void main(String[] args) //throws MessagingException
    {
        System.out.println("[i] Starting.");
        //INIT__________________________________________
        String ver = null, user = null, pswd = null, to = null, file = null, smtp = null, subj=null,body=null;
        Properties c=new Properties();
        try {

            BufferedReader f=new BufferedReader(new FileReader("cfg.properties"));
            c.load(f);
            ver=c.getProperty("ver");
            user=c.getProperty("user");
            pswd=c.getProperty("pswd");
            to=c.getProperty("to");
            file=c.getProperty("file");
            smtp=c.getProperty("smtp");
            subj=c.getProperty("subj");
            body=c.getProperty("body");

        } catch (Throwable e) {
            System.out.println("[X] Error loading config:");e.printStackTrace();System.exit(-1);
        }
        if(args.length!=0&& args[0].equals("-d")){System.out.println("[D] Values:\nver  | "+ver+"\nuser | "+user+"\npswd | "+pswd+"\nto   | "+to+"\nfile | "+file);}
        if(
           ver==null||user==null||pswd==null||to==null||file==null||
           !ver.equals("2")||user.equals("")||pswd.equals("")||to.equals("")||file.equals("")
        ){System.out.println("[X] Error loading config: Incorrect config");System.exit(-1);}
        System.out.println("[i] Started.");
        //INIT END--------------------------------------

        //MAIN__________________________________________
        System.out.println("[i] Sending.");
        try {
            final Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.qq.com");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.debug", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.user", user);
            props.put("mail.password", pswd);
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            Session mailSession = Session.getInstance(props, authenticator);
            MimeMessage message = new MimeMessage(mailSession);
            InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
            message.setFrom(form);
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            message.setSubject("A");
            message.setContent("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "text/html;charset=UTF-8");
            Transport.send(message);
            System.out.println("[i] Success.");
        }catch (Throwable e){System.out.println("[X] Error sending email:");e.printStackTrace();}

        //MAIN END--------------------------------------

        System.exit(0);
    }
}
