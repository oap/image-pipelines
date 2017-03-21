package com.opopov.cloud.image;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.AsyncRestTemplate;

@SpringBootApplication
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Starting the Image Decode App:");

        };
    }


    @Bean
    public HttpComponentsAsyncClientHttpRequestFactory httpRequestFactory() {
        HttpComponentsAsyncClientHttpRequestFactory nioFactory = new HttpComponentsAsyncClientHttpRequestFactory();
        return nioFactory;
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate(HttpComponentsAsyncClientHttpRequestFactory httpRequestFactory) {
        AsyncRestTemplate remoteResource = new AsyncRestTemplate(httpRequestFactory);
        return remoteResource;
    }
}
