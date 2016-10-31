package xm.bibibiradio.post.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
    final String              SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    private Properties        prop;

    private Session           session;
    private String            password;
    private String            user;

    private static String     smtp        = "";
    private static String     port        = "465";
    private static boolean    isSSL       = true;
    private static boolean    isAuth      = true;
    private static String     userName    = "";
    private static String     passwd      = "";

    private static MailSender mailSender;

    public static MailSender getMailSender() {
        if (mailSender == null) {
            synchronized (MailSender.class) {
                if (mailSender == null) {
                    mailSender = new MailSender();
                    mailSender.init(smtp, port, isSSL, isAuth, userName, passwd);
                }
            }
        }
        return mailSender;
    }

    public void init(String smtp, String port, boolean isSSL, boolean isAuth, String user,
                     String password) {
        this.user = user;
        this.password = password;
        prop = new Properties();

        prop.put("mail.smtp.host", smtp);
        prop.put("mail.smtp.auth", Boolean.toString(isAuth));
        if (isSSL) {
            prop.put("mail.smtp.ssl.enable", "true");
            prop.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        }

        if (port != null) {
            prop.setProperty("mail.smtp.port", port);
            prop.setProperty("mail.smtp.socketFactory.port", port);
        }
        prop.setProperty("mail.smtp.connectiontimeout", "8000");

        session = Session.getDefaultInstance(prop);
    }

    public void sendMail(String from, String to, String copyto, String subject, String content)
                                                                                               throws Exception {
        Transport transport = session.getTransport("smtp");
        transport.connect((String) prop.get("mail.smtp.host"), user, password);
        MimeMessage mimeMsg = new MimeMessage(session);
        //MimeMultipart mp = new MimeMultipart();

        mimeMsg.setSubject(subject, "UTF-8");
        mimeMsg.setFrom(new InternetAddress(from));
        mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        if (copyto != null) {
            mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto));
        }

        mimeMsg.setContent(content, "text/html;charset=UTF-8");
        mimeMsg.saveChanges();

        transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));

        transport.close();
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static String getSmtp() {
        return smtp;
    }

    public static void setSmtp(String smtp) {
        MailSender.smtp = smtp;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        MailSender.port = port;
    }

    public static boolean isSSL() {
        return isSSL;
    }

    public static void setSSL(boolean isSSL) {
        MailSender.isSSL = isSSL;
    }

    public static boolean isAuth() {
        return isAuth;
    }

    public static void setAuth(boolean isAuth) {
        MailSender.isAuth = isAuth;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        MailSender.userName = userName;
    }

    public static String getPasswd() {
        return passwd;
    }

    public static void setPasswd(String passwd) {
        MailSender.passwd = passwd;
    }

    public static void setMailSender(MailSender mailSender) {
        MailSender.mailSender = mailSender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
