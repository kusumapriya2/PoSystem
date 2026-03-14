package com.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            if (to == null || to.isBlank()) {
                log.warn("Email NOT sent: customer email is null/blank");
                return;
            }

            log.info("Sending email to={} subject={}", to, subject);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);

            mailSender.send(msg);

            log.info("Email sent successfully to={}", to);

        } catch (Exception e) {
            log.error("Email sending FAILED", e);
        }
    }
}
