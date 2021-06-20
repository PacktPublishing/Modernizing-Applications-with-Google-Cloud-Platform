package uk.me.jasonmarston;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableSpringConfigured
@EnableRedisHttpSession
public class ServletApplication extends SpringBootServletInitializer {
    public static void main(final String[] args) {
        SpringApplication.run(ServletApplication.class, args);
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
    	return ConfigureRedisAction.NO_OP;
    }
}