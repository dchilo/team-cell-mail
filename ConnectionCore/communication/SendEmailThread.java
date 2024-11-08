package communication;

import utils.Email;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import io.github.cdimascio.dotenv.Dotenv;

public class SendEmailThread implements Runnable {

    private final Email email;
    private static final Dotenv dotenv = Dotenv.configure().load();

    public SendEmailThread(Email email) {
        this.email = email;
    }

    private static String loadEnvVar(String name) {
        return dotenv.get(name);
    }

    private Properties configureSMTPProperties() {
        Properties properties = new Properties();
        String server = loadEnvVar("SMTP_SERVER");

        if ("gmail".equalsIgnoreCase(server)) {
            properties.setProperty("mail.smtp.host", loadEnvVar("SMTP_HOST_GMAIL"));
            properties.setProperty("mail.smtp.port", loadEnvVar("SMTP_PORT_GMAIL"));
            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.setProperty("mail.smtp.auth", "true");
        } else if ("tecnoweb".equalsIgnoreCase(server)) {
            properties.setProperty("mail.smtp.host", loadEnvVar("SMTP_HOST_TECNOWEB"));
            properties.setProperty("mail.smtp.port", loadEnvVar("SMTP_PORT_TECNOWEB"));
            properties.setProperty("mail.smtp.auth", "false");
            properties.setProperty("mail.smtp.starttls.enable", "false");
        }
        // Agrega aquí más configuraciones para otros dominios si es necesario

        return properties;
    }

    private PasswordAuthentication getCredentials() {
        String server = loadEnvVar("SMTP_SERVER");

        if ("gmail".equalsIgnoreCase(server)) {
            return new PasswordAuthentication(loadEnvVar("SMTP_USER_GMAIL"), loadEnvVar("SMTP_PASSWORD_GMAIL"));
        } else if ("tecnoweb".equalsIgnoreCase(server)) {
            return new PasswordAuthentication(loadEnvVar("SMTP_USER_TECNOWEB"), loadEnvVar("SMTP_PASSWORD_TECNOWEB"));
        }
        return null; // Devuelve null si no hay autenticación
    }

    @Override
    public void run() {
        try {
            Properties properties = configureSMTPProperties();

            Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return getCredentials();
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(loadEnvVar("SMTP_USER_TECNOWEB"))); // Remitente, ajusta según tu servidor
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(email.getTo()));
            message.setSubject(email.getSubject());

            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(email.getMessage(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);
            message.saveChanges();

            Transport.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(SendEmailThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
