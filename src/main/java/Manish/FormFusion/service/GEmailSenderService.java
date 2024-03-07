package Manish.FormFusion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GEmailSenderService {

    private final GEmailSender gEmailSender;

    @Autowired
    public GEmailSenderService(GEmailSender gEmailSender) {
        this.gEmailSender = gEmailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        String from = "manishchoudhary9650@gmail.com";
        boolean isSent = gEmailSender.sendEmail(to, from, subject, body);
        if (isSent) {
            System.out.println("Email sent successfully");
        } else {
            System.out.println("Failed to send email");
        }
    }
}
