package com.frank;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableApolloConfig
public class DtpApplication {
    public static void main(String[] args) {
        SpringApplication.run(DtpApplication.class, args);
        System.out.println("Javas started!");
    }

}
