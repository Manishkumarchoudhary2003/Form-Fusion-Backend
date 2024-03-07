//package Manish.FormFusion.service;
//
//import org.springframework.stereotype.Service;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//@Service
//public class EmailService {
//
//
//    private final JavaMailSender javaMailSender;
//
//    public EmailService(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//
//
//    public void sendEmail(String to,String subject,String body) {
//
//        try {
//            System.out.println("In Try Block...........");
//            MimeMessage message = javaMailSender.createMimeMessage();
//            System.out.println("Message Created ..........");
//            MimeMessageHelper helper = new MimeMessageHelper(message,true);
//            System.out.println("helper..................");
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(body,true);
//            System.out.println("Before sending mail...........");
//            javaMailSender.send(message);
//            System.out.println("Mail sent ...................");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            e.getMessage();
//            System.out.println("Got Exception...................");
//            throw new RuntimeException(e);
//        }
//
//    }
//}


package Manish.FormFusion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

    private final GEmailSenderService gEmailSenderService;

    @Autowired
    public EmailService(GEmailSenderService gEmailSenderService) {
        this.gEmailSenderService = gEmailSenderService;
    }

    public void sendEmail(String to, String subject, String body) {
        gEmailSenderService.sendEmail(to, subject, body);
    }
}
