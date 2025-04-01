package com.zerowaste.services.broadcasts;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.services.broadcasts.errors.BroadcastListNotFoundException;

@Service
public class TriggerBroadcastService {
    private JavaMailSender mailSender;
    private BroadcastListsRepository broadcastListRepository;

    public TriggerBroadcastService(
        JavaMailSender mailSender,
        BroadcastListsRepository broadcastListRepository
    ) {
        this.mailSender = mailSender;
        this.broadcastListRepository = broadcastListRepository;
    }

    @Value("${spring.mail.username}")
    private String from;

    public void execute(Long id) {
        var broadcastList = broadcastListRepository.findById(id).orElse(null);

        if (broadcastList == null) {
            throw new BroadcastListNotFoundException("Lista de transmissão #" + id + " não encontrada");
        }

        var message = new SimpleMailMessage();

        Hibernate.initialize(broadcastList.getBroadcastEmails());

        var broadcastEmails = broadcastList.getBroadcastEmails();

        if (broadcastEmails.isEmpty()) {
            return;
        }

        for (var broadcastEmail : broadcastList.getBroadcastEmails()) {
            message.setTo(broadcastEmail.getEmail());
            message.setSubject(broadcastList.getName());
            message.setText("Alguma mensagem genérica no corpo do e-mail.");
            message.setFrom(this.from);
            mailSender.send(message);
        }
    }
}
