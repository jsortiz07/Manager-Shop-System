package com.manager.shop.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class EmailUtils {



    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list){
        // se construye el mensaje para realizar el envio
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lopez.3143351231@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (list!=null &&list.size()>0)
            message.setCc(getCcArray(list));
        emailSender.send(message);


    }

    private String[] getCcArray(List<String>ccList){
        String[] cc  = new String[ccList.size()];

        for (int i=0;i<ccList.size();i++){
            cc[i]= ccList.get(i);
        }

        return cc;
    }
}
