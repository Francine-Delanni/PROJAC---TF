package com.af.entregas;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class EntregasServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntregasServiceApplication.class, args);
    }
}
