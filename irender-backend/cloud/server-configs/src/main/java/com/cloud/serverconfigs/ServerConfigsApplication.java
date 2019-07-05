package com.cloud.serverconfigs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ServerConfigsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerConfigsApplication.class, args);
    }

}
