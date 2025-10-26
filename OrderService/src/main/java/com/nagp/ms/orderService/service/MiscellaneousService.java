package com.nagp.ms.orderService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MiscellaneousService {

    private static final String TOPIC = "order-notifications";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotification(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Published notification to Kafka: " + message);
    }
    public boolean mockPayment() {
        return new Random().nextBoolean();
    }
}
