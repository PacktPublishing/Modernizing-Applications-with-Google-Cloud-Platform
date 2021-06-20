package uk.me.jasonmarston;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
    	String port = System.getenv().getOrDefault("PORT", "8080");
        System.setProperty("server.port", port);
        SpringApplication.run(Application.class, args);
    }
}