package uk.me.jasonmarston;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"uk.me.jasonmarston.domain.repository"})
@EnableTransactionManagement
public class PersistenceConfig {
}