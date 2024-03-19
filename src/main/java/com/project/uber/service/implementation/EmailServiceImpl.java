package com.project.uber.service.implementation;

import com.project.uber.dtos.EmailDto;
import com.project.uber.model.Order;
import com.project.uber.service.interfac.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;


    @Override
    public void sendSimpleMessage(EmailDto emailDto) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("uberdemercadorias@gmail.com");
        message.setTo(emailDto.to());
        message.setSubject(emailDto.subject());
        message.setText(emailDto.text());
        emailSender.send(message);
    }

    @Override
    public void sendOrderStatusUpdateEmail(Order order) {
        EmailDto emailDto = new EmailDto(
                order.getClient().getEmail(),
                "Order status update",
                "Your order status has been updated to: " +
                        order.getStatus());
        sendSimpleMessage(emailDto);
    }
}
