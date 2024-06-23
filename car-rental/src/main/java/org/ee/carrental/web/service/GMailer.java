package org.ee.carrental.web.service;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class GMailer {
    private static final String EMAIL_FROM = "carrentalkcfb@gmail.com";
    private static final String APP_PASSWORD = "zfnm egcy vrog bcou";
    private static String confirmedReservation = """
    Otrzymaliśmy Pańską rezerwację nr - %s na samochód %s %s;
    W przypadku niedokonania płatności w terminie 3 Dni roboczych, rezerwacja zostanie automatycznie anulowana.
    Dziękujemy za skorzystanie z naszych usług,
    Car-Rental
    """;

    private static String canceledReservation = """
    W związku z brakiem płatności za rezerwację nr - %s samochód %s %s
    zmuszeni jesteśmy anulować Pańską rezerwację.
    Car-Rental
    """;

    private static String confirmedPayment = """
    Otrzymaliśmy Pańską płatność za rezerwację nr - %s na samochód %s %s
    Dziękujemy za skorzystanie z naszych usług i do zobaczenia,
    Car-Rental
    """;

    public void sendEmail(String EMAIL_TO, String brand, String model, String reservationNumber, String emailType) throws Exception {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO));
        String emailSubject = "";
        String emailContent = "";

        switch (emailType) {
            case "confirmedReservation":
                emailContent = String.format(confirmedReservation, reservationNumber, brand, model);
                emailSubject = "Pańska rezerwacja została przyjęta do systemu - Car-Rental";
                break;
            case "canceledReservation":
                emailContent = String.format(canceledReservation, reservationNumber, brand, model);
                emailSubject = "Pańska rezerwacja została anulowana - Car-Rental";
                break;
            case "confirmedPayment":
                emailContent = String.format(confirmedPayment, reservationNumber, brand, model);
                emailSubject = "Pańska rezerwacja została potwierdzona - Car-Rental";
                break;
            default:
                throw new IllegalArgumentException("Unknown email type: " + emailType);
        }

        message.setSubject(emailSubject);
        message.setText(emailContent);
        Transport.send(message);
    }

    private Session getEmailSession() {
        return Session.getInstance(getGmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
            }
        });
    }

    private Properties getGmailProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return prop;
    }
}