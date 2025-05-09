package services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "houstlili55@gmail.com"; // Replace with your Gmail address
    private static final String EMAIL_PASSWORD = "tkan wztf czov yopl"; // Replace with your 16-character App Password (no spaces)

    public static boolean sendNewPasswordEmail(String toEmail, String newPassword) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        // Debug: Print credentials being used
        System.out.println("Attempting authentication with username: " + EMAIL_USERNAME + " and password length: " + EMAIL_PASSWORD.length());

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Votre nouveau mot de passe");

            String msg = "Votre mot de passe a été réinitialisé. Voici votre nouveau mot de passe :\n" +
                    newPassword +
                    "\nVeuillez vous connecter avec ce mot de passe et le modifier dès que possible.";
            message.setText(msg);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}