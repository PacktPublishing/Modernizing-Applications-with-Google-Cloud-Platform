package uk.me.jasonmarston;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import uk.me.jasonmarston.mvc.authentication.impl.CustomAuthenticationProvider;
import uk.me.jasonmarston.mvc.filter.impl.LocaleFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@Lazy
	private CustomAuthenticationProvider authenticationProvider;
	
	@Autowired
	private LocaleFilter localeFilter;

	@Override
	protected void configure(
			AuthenticationManagerBuilder authenticationManagerBuilder)
					throws Exception {
		authenticationManagerBuilder.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/login").permitAll()
				.and()
			.logout()
				.logoutUrl("/logout").permitAll()
				.and()
			.httpBasic().disable()
			.authorizeRequests()
				.antMatchers(
					"/error",
					"/login",
					"/user/registration",
					"/user/registration/**",
					"/user/password/reset",
					"/user/password/reset/**",
					"/favicon.ico",
					"/**/*.png",
					"/**/*.gif",
					"/**/*.svg",
					"/**/*.jpg",
					"/**/*.html",
					"/**/*.css",
					"/**/*.js").permitAll()
				.anyRequest().authenticated();

		http.addFilterAfter(localeFilter,
            	UsernamePasswordAuthenticationFilter.class);
    }

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	public SpringSecurityDialect springSecurityDialect(){
		return new SpringSecurityDialect();
	}
}