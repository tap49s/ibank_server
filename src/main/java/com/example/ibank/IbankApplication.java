package com.example.ibank;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IbankApplication {

	public static void main(String[] args) {
		// Load the .env file
        Dotenv dotenv = Dotenv.configure().load();

        // Set system properties to use the variables in application.yaml
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		SpringApplication.run(IbankApplication.class, args);
	}

}
