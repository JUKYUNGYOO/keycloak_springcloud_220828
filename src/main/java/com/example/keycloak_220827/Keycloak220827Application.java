package com.example.keycloak_220827;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Keycloak220827Application {

    public static void main(String[] args) {
        SpringApplication.run(Keycloak220827Application.class, args);
    }

}
