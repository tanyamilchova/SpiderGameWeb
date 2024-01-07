package com.example.spider.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
}
