package edu.ucalgary.javalogic.Email;


import edu.ucalgary.javalogic.Entities.Payment;
import edu.ucalgary.javalogic.Entities.Ticket;
import edu.ucalgary.javalogic.Entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.antlr.v4.runtime.misc.Utils.readFile;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public static String readFileAsString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString().trim();
    }

//    public void sendEmail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        message.setFrom("acmeplex480@gmail.com");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//
//        mailSender.send(message);
//    }

//    public void sendHtmlEmail() throws MessagingException, IOException {
//        MimeMessage message = mailSender.createMimeMessage();
//
//        message.setFrom("acmeplex480@gmail.com");
//        message.setRecipients(MimeMessage.RecipientType.TO, "meyer2842@gmail.com");
//        message.setSubject("Test email from Spring");
//
//
//        String htmlContent = readFileAsString("backend/java-logic/src/main/java/edu/ucalgary/javalogic/email/receiptHTMLTemplate.html");
//
//        message.setContent(htmlContent, "text/html; charset=utf-8");
//
//        mailSender.send(message);
//    }

    public void sendReceipt(User user, Payment payment) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom("acmeplex480@gmail.com");
        message.setRecipients(MimeMessage.RecipientType.TO, user.getEmail());
//        message.setRecipients(MimeMessage.RecipientType.TO, "meyer2842@gmail.com");
        message.setSubject("ACMEPlex Movie Ticket Receipt");

        String htmlContent = readFileAsString("backend/java-logic/src/main/java/edu/ucalgary/javalogic/Email/receiptHTMLTemplate.html");

        htmlContent = htmlContent.replace("{{userName}}", user.getFirstName());
        htmlContent = htmlContent.replace("{{paymentId}}", Integer.toString(payment.getId()));
        htmlContent = htmlContent.replace("{{paymentType}}", payment.getPaymentType());
        htmlContent = htmlContent.replace("{{subtotal}}", String.format("%.2f", payment.getSubtotal()));
        htmlContent = htmlContent.replace("{{creditUsed}}", String.format("%.2f", payment.getCreditUsed()));
        htmlContent = htmlContent.replace("{{total}}", String.format("%.2f", payment.getTotal()));
        htmlContent = htmlContent.replace("{{paymentMethod}}", "Card ending in: " + payment.getPaymentMethod().getCardNum().substring(15, 19));

        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);

    }

    public void sendCancellation(User user, Payment payment) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom("acmeplex480@gmail.com");
        message.setRecipients(MimeMessage.RecipientType.TO, user.getEmail());
//        message.setRecipients(MimeMessage.RecipientType.TO, "meyer2842@gmail.com");
        message.setSubject("ACMEPlex Ticket Cancellation Receipt");

        String htmlContent = readFileAsString("backend/java-logic/src/main/java/edu/ucalgary/javalogic/Email/cancellationHTMLTemplate.html");

        htmlContent = htmlContent.replace("{{userName}}", user.getFirstName());
        htmlContent = htmlContent.replace("{{paymentId}}", Integer.toString(payment.getId()));
        htmlContent = htmlContent.replace("{{paymentType}}", payment.getPaymentType());
        htmlContent = htmlContent.replace("{{total}}", String.format("%.2f", -1*payment.getCreditUsed()));

        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);

    }

    public void sendTicket(User user, Ticket ticket) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

//        FileSystemResource file = new FileSystemResource(new File("backend/java-logic/src/main/java/edu/ucalgary/javalogic/email/TicketHTMLTemplate.html"));
//        helper.addAttachment("backend/java-logic/src/main/java/edu/ucalgary/javalogic/email/TicketHTMLTemplate.html", file);

//        message.setFrom("acmeplex480@gmail.com");
////        message.setRecipients(MimeMessage.RecipientType.TO, user.getEmail());
//        message.setRecipients(MimeMessage.RecipientType.TO, "meyer2842@gmail.com");
//        message.setSubject("ACMEPlex Movie Ticket");

        String htmlContent = readFileAsString("backend/java-logic/src/main/java/edu/ucalgary/javalogic/Email/TicketHTMLTemplate.html");

        htmlContent = htmlContent.replace("{{ticketId}}", Integer.toString(ticket.getId()));
        htmlContent = htmlContent.replace("{{userName}}", user.getFirstName());
        htmlContent = htmlContent.replace("{{state}}", ticket.getState());
        htmlContent = htmlContent.replace("{{theatreName}}", ticket.getTheatre().getName());
        htmlContent = htmlContent.replace("{{movieName}}", ticket.getMovie().getTitle());
        htmlContent = htmlContent.replace("{{showtime}}", ticket.getShowtime().getShowingDateTimeString());
        htmlContent = htmlContent.replace("{{seat}}", ticket.getSeat().getSeat());
        htmlContent = htmlContent.replace("{{qrCodeUrl}}", "./ticketQRCode.png");
//        htmlContent = htmlContent.replace("{{paymentId}}", Integer.toString(payment.getId()));
//        htmlContent = htmlContent.replace("{{paymentType}}", payment.getPaymentType());
//        htmlContent = htmlContent.replace("{{total}}", String.format("%.2f", -1*payment.getCreditUsed()));

//        message.setContent(htmlContent, "text/html; charset=utf-8");

        helper.setFrom("acmeplex480@gmail.com");

//        helper.setTo("meyer2842@gmail.com");
        helper.setTo(user.getEmail());
        helper.setSubject("ACMEPlex Movie Ticket");
        helper.setText(htmlContent, true);

        FileSystemResource file = new FileSystemResource(new File("backend/java-logic/src/main/java/edu/ucalgary/javalogic/Email/ticketQRCode.png"));
        helper.addAttachment("ticketQRCode.png", file);

        mailSender.send(message);
    }
}
