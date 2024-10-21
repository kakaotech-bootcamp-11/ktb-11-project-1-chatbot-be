package org.ktb.chatbotbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Ktb11Project1ChatbotBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ktb11Project1ChatbotBeApplication.class, args);
    }

}
