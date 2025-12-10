package org.example.documind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DocuMindApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocuMindApplication.class, args);
    }

}
