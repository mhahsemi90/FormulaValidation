package org.hcm.pcn.formula_validator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FormulaValidatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormulaValidatorApplication.class, args);
    }

}
