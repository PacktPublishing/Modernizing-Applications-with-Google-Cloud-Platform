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
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.me.jasonmarston.domain.user.aggregate.impl.User;
import uk.me.jasonmarston.domain.user.entity.impl.Authority;

@Configuration
@EnableJpaRepositories(
		basePackages = {"uk.me.jasonmarston.domain.user.repository"},
		entityManagerFactoryRef = "userEntityManagerFactory",
		transactionManagerRef = "userTransactionManager")
@EnableTransactionManagement
public class UserPersistenceConfig {
	@Autowired
	private Environment env;

	@Primary
	@Bean(name = "userDataSource")
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource userDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "userEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		properties.put("hibernate.implicit_naming_strategy", env.getProperty("spring.jpa.hibernate.naming_strategy"));

		return builder
				.dataSource(userDataSource())
				.packages(User.class, Authority.class)
				.properties(properties)
				.build();
	}

	@Primary
	@Bean(name = "userTransactionManager")
	public PlatformTransactionManager userTransactionManager(
			final @Qualifier("userEntityManagerFactory") LocalContainerEntityManagerFactoryBean userEntityManagerFactory) {
		return new JpaTransactionManager(userEntityManagerFactory.getObject());
	}
}