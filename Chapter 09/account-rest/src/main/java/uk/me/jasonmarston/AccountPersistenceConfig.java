package uk.me.jasonmarston;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.me.jasonmarston.domain.account.aggregate.impl.Account;
import uk.me.jasonmarston.domain.account.entity.impl.Transaction;

@Configuration
@EnableJpaRepositories(
		basePackages = {"uk.me.jasonmarston.domain.account.repository"},
		entityManagerFactoryRef = "accountEntityManagerFactory",
		transactionManagerRef = "accountTransactionManager")
@EnableTransactionManagement
public class AccountPersistenceConfig {
	@Autowired
	private Environment env;

	@Bean(name = "accountDataSource")
	@ConfigurationProperties(prefix="spring.account-datasource")
	public DataSource accountDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "accountEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean accountEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		properties.put("hibernate.implicit_naming_strategy", env.getProperty("spring.jpa.hibernate.naming_strategy"));
        
		return builder
				.dataSource(accountDataSource())
				.packages(Account.class, Transaction.class)
				.properties(properties)
				.build();
	}

	@Bean(name = "accountTransactionManager")
	public PlatformTransactionManager accountTransactionManager(
			final @Qualifier("accountEntityManagerFactory") LocalContainerEntityManagerFactoryBean accountEntityManagerFactory) {
		return new JpaTransactionManager(accountEntityManagerFactory.getObject());
	}
}