package net.e621.mcumbrella.srp;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Date;
import java.util.Properties;
public class A {
    public static void main(String[] args)
    {
        System.out.println("[i] Starting.");
        //INIT__________________________________________
        String ver = null, user = null, pswd = null, to = null, file = null, smtp = null, subj = null, body = null, name = null;
        Properties c = new Properties();
        try {

            BufferedReader f = new BufferedReader(new FileReader("cfg.properties"));
            c.load(f);
            ver = c.getProperty("ver");
            user = c.getProperty("user");
            pswd = c.getProperty("pswd");
            to = c.getProperty("to");
            file = c.getProperty("file");
            smtp = c.getProperty("smtp");
            subj = c.getProperty("subj");
            body = c.getProperty("body");
            name = c.getProperty("name");//TODO: flag: detect real file name and use it

        } catch (Throwable e) {
            System.out.println("[X] Error loading config:");
            e.printStackTrace();
            System.exit(-1);
        }
        if (args.length != 0 && args[0].equals("-d")) {
            System.out.println("[D] Values:\nver  | " + ver + "\nuser | " + user + "\npswd | " + pswd + "\nto   | " + to + "\nfile | " + file + "\nsmtp | " + smtp + "\nsubj | " + subj);
        }
        if (//TODO: maybe sth can be optimized here
                user == null || pswd == null || to == null || smtp == null || subj == null || body == null ||
                        !ver.equals("2") || user.equals("") || pswd.equals("") || to.equals("") || smtp.equals("") || subj.equals("") || body.equals("")
        ) {
            System.out.println("[X] Error loading config: Incorrect config");
            System.exit(-1);
        }
        if(file!=null&&!new File(file).exists()&&!file.equals("")){System.out.println("[X] Specified file not found: "+file);System.exit(-1);}
        System.out.println("[i] Started.");
        //INIT END--------------------------------------

        //MAIN__________________________________________
        System.out.println("[i] Sending.");
        try {
            final Properties p = new Properties();
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.host", smtp);
            p.put("mail.transport.protocol", "smtp");
            if (args.length != 0 && args[0].equals("-d")){p.put("mail.debug", "true");}
            p.put("mail.smtp.starttls.enable", "true");
            p.put("mail.user", user);
            p.put("mail.password", pswd);
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    String userName = p.getProperty("mail.user");
                    String password = p.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            Session s = Session.getInstance(p, authenticator);
            MimeMessage m = new MimeMessage(s);
            InternetAddress form = new InternetAddress(p.getProperty("mail.user"));
            m.setFrom(form);
            m.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            m.setSubject(subj);
            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart bp = new MimeBodyPart();
            bp.setContent(body, "text/html;charset=UTF-8");
            mp.addBodyPart(bp);
            if (file!=null&&!file.equals("")) {
                MimeBodyPart fl = new MimeBodyPart();
                fl.setDataHandler(new DataHandler(new FileDataSource(file)));
                //TODO: detect real file name and use it
                mp.addBodyPart(fl);
            }
            mp.setSubType("mixed");
            m.setContent(mp);
            m.setSentDate(new Date());
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
            Transport.send(m);
            System.out.println("[i] Success.");
        } catch (Throwable e) {
            System.out.println("[X] Error sending email:");
            e.printStackTrace();
            System.exit(-1);
        }
        //MAIN END--------------------------------------

        System.exit(0);
    }
}