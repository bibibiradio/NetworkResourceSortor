package xm.bibibiradio.mainsystem.util;

import java.util.Properties;

//import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;

public class MailSender {
    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    
    private Properties prop;
    private String     userName;
    private String     passwd;
    private Session    session;
    
    static{
        //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    }

    public void init(String smtp, String port ,boolean isSSL,boolean isAuth, String userName, String pwd) {
        prop = new Properties();

        prop.put("mail.smtp.host", smtp);
        prop.put("mail.smtp.auth", Boolean.toString(isAuth));
        if(isSSL){
            prop.put("mail.smtp.ssl.enable", "true");
        }
        prop.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        if(port != null){
            prop.setProperty("mail.smtp.port", port);
            prop.setProperty("mail.smtp.socketFactory.port", port);
        }
        this.userName = userName;
        this.passwd = pwd;

        session = Session.getDefaultInstance(prop);
    }

    public void sendMail(String from, String to, String copyto, String subject, String content) throws Exception {
        Transport transport = session.getTransport("smtp");
        transport.connect((String) prop.get("mail.smtp.host"), userName, passwd);
        MimeMessage mimeMsg = new MimeMessage(session);
        //MimeMultipart mp = new MimeMultipart();

        mimeMsg.setSubject(subject);
        mimeMsg.setFrom(new InternetAddress(from));
        mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto));

        //BodyPart bp = new MimeBodyPart();
        //bp.setContent("" + content, "text/html;charset=UTF-8");
        //mp.addBodyPart(bp);
        mimeMsg.setContent(content,"text/html;charset=UTF-8");
        mimeMsg.saveChanges();
        
        transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
        
        transport.close();
    }
}
