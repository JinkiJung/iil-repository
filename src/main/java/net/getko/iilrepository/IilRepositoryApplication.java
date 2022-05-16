package net.getko.iilrepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class IilRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(IilRepositoryApplication.class, args);
	}

}
