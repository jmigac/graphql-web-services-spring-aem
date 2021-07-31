package io.ecx.spring.graphql.webservice.services;

import java.util.Arrays;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailServis implements MailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(final SimpleMailMessage simpleMailMessage) throws MailException {
        this.javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void send(final SimpleMailMessage... simpleMailMessages) throws MailException {
        Arrays.stream(simpleMailMessages).forEach(this::send);
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        final JavaMailSenderImpl posiljatelj = new JavaMailSenderImpl();
        posiljatelj.setHost("smtp.gmail.com");
        posiljatelj.setPort(587);
        posiljatelj.setUsername("jmig4c@gmail.com");
        posiljatelj.setPassword("lepoglava15");
        final Properties props = posiljatelj.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        return posiljatelj;
    }

}
