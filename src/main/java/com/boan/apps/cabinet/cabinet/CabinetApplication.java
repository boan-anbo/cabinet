package com.boan.apps.cabinet.cabinet;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

@SpringBootApplication
public class CabinetApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabinetApplication.class, args);
    }

}
