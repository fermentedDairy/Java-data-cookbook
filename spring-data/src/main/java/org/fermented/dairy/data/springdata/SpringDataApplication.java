package org.fermented.dairy.data.springdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"org.fermented.dairy.data.*",})
public class SpringDataApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SpringDataApplication.class, args);
    }

}
