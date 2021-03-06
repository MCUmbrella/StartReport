package net.e621.mcumbrella.srp;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.security.Security;
import java.util.*;
public class A {
    public static void main(String[] args) {
        System.out.println("[i] StartReport starting.");
        //INIT__________________________________________
        String ver = null, user = null, pswd = null, to = null, file = null, smtp = null, ssl = null, port = null, subj = null, body = null, stls=null,ehlo=null;
        boolean cfgSet = false, dbgSet = false;
        String cfgf = "cfg.properties";
        for (String arg : args) {
            if (arg.equals("-d")) {
                dbgSet = true;
                break;
            }
        }
        for (String arg : args) {
            if (arg.startsWith("-c=") && !cfgSet) {
                cfgSet = true;
                cfgf = arg.substring(3);
            }
        }
        Properties c = new Properties();
        try {

            BufferedReader f = new BufferedReader(new FileReader(cfgf));
            c.load(f);
            ver = c.getProperty("ver");
            user = c.getProperty("user");
            pswd = c.getProperty("pswd");
            to = c.getProperty("to");
            file = c.getProperty("file");
            smtp = c.getProperty("smtp");
            ssl = c.getProperty("ssl");
            port = c.getProperty("port");
            subj = c.getProperty("subj");
            body = c.getProperty("body").replace("{DATE}",new Date().toString()).replace("{FILE}",file);
            stls=c.getProperty("stls");
            ehlo=c.getProperty("ehlo");

        } catch (Throwable e) {
            System.out.println("[X] Error loading config:");
            e.printStackTrace();
            System.exit(-1);
        }
        if (dbgSet) {
            System.out.println("[D] Build number: 7 - Made by Umbrella Studio.\n    Inspired by life, designed for life.\n" + "[D] Values:\nver\t| " + ver + "\nuser\t| " + user + "\npswd\t| " + pswd + "\nto\t| " + to + "\nfile\t| " + file + "\nsmtp\t| " + smtp + "\nport\t| " + port +"\nsubj\t| " + subj + "\nbody\t| " + body);
        }
        if (
                user == null || pswd == null || to == null || smtp == null || ssl ==null || port==null || subj == null ||
                        !ver.equals("3") || user.equals("") || pswd.equals("") || to.equals("") || smtp.equals("") || ssl.equals("") || port.equals("") || subj.equals("") || body.equals("")
        ) {
            System.out.println("[X] Error loading config: Incorrect config");
            System.exit(-1);
        }
        if (!file.equals("") && !new File(file).exists()) {
            System.out.println("[X] Specified file not found: " + file);
            System.exit(-1);
        }
        System.out.println("[i] initialization complete. Sending mail.");
        //INIT -> MAIN----------------------------------
        try {
            final Properties p = new Properties();
            if(ssl.equals("true"))
            {
                if(dbgSet){System.out.println("[D] SSL enabled.");}
                p.put("mail.transport.protocol", "smtps");
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                p.put("mail.smtp.ssl.enable", "true");
                p.put("mail.smtps.ssl.enable", "true");
                p.put("mail.smtps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                p.put("mail.smtps.socketFactory.fallback", "false");
                p.put("mail.smtps.socketFactory.port", port);
                p.put("mail.host", smtp);
                p.put("mail.smtps.auth", "true");
                p.put("mail.smtps.port", port);
                p.put("mail.smtps.ssl.trust", smtp);
            }else
            {
                if(dbgSet){System.out.println("[D] SSL disabled.");}
                p.put("mail.transport.protocol", "smtp");
                p.put("mail.smtp.ssl.enable", "false");
            }
            p.put("mail.smtp.starttls.enable", stls);
            p.setProperty("mail.smtp.ssl.protocols", "TLSv1.1 TLSv1.2");
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.host", smtp);
            p.put("mail.smtp.localhost", ehlo);
            p.put("mail.smtp.port", port);
            if (dbgSet) {
                p.put("mail.debug", "true");
            }
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
            if (!file.equals("")) {
                MimeBodyPart fl = new MimeBodyPart();
                fl.setDataHandler(new DataHandler(new FileDataSource(file)));
                fl.setFileName(MimeUtility.encodeText(file.split("/")[file.split("/").length - 1]));
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