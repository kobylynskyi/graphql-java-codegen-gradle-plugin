package com.kobylynskyi.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class GraphqlJavaToolsExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlJavaToolsExampleApplication.class, args);
    }

    @Bean
    public ServerEndpointExporter endpointExporter() {
        return new ServerEndpointExporter();
    }

}