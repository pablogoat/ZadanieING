package com.example.zadanie.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailGenerateService {

    public SimpleMailMessage generateMail(List<String> addresses, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(addresses.toArray(new String[0]));
        message.setText(text);

        return message;
    }
}
