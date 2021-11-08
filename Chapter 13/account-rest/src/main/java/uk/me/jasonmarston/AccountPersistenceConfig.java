package uk.me.jasonmarston;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
	public DataSource accountDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(
				env.getProperty("spring.account-datasource.jdbcUrl"));
		config.setUsername(
				env.getProperty("spring.account-datasource.username"));
		config.setPassword(
				env.getProperty("spring.account-datasource.password"));
		config.setDriverClassName(
				env.getProperty("spring.account-datasource.driverClassName"));
		String socketFactory = env
				.getProperty("spring.account-datasource.socketFactory");
		if(socketFactory != null) {
			config.addDataSourceProperty("socketFactory", socketFactory);
		}
		String cloudSqlInstance = env
				.getProperty("spring.account-datasource.cloudSqlInstance");
		if(cloudSqlInstance != null) {
			config.addDataSourceProperty("cloudSqlInstance", cloudSqlInstance);
		}
		return new HikariDataSource(config);
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