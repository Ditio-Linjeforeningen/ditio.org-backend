package org.ditio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		
		
		
		/*
		String Dburl  = System.getenv("SPRING_DATASOURCE_URL");
		String username = System.getenv("POSTGRES_USER");
		String password = System.getenv("POSTGRES_PASSWORD");

		Flyway flyway = Flyway.configure()
		.dataSource(Dburl, username, password)
		.load();
		
		// Start the migration
		flyway.migrate(); 
		*/
	}
	

}
