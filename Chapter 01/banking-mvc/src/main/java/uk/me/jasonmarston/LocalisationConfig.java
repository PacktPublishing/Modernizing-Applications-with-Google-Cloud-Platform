package uk.me.jasonmarston;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import uk.me.jasonmarston.mvc.filter.impl.LocaleFilter;

@Configuration
public class LocalisationConfig implements WebMvcConfigurer {
	// Cannot be lazy due to final methods
	@Autowired
	private ResourceBundleMessageSource resourceBundleMessageSource;

	private ITemplateResolver _htmlEmailTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = 
				new ClassLoaderTemplateResolver();

		templateResolver.setPrefix("/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCheckExistence(true);
		templateResolver.setCacheable(false);

		return templateResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean(name = "htmlEmailTemplateEngine")
	public TemplateEngine htmlEmailTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setDialect(new SpringStandardDialect());
		templateEngine.addTemplateResolver(_htmlEmailTemplateResolver());
		templateEngine.setTemplateEngineMessageSource(
				resourceBundleMessageSource);

		return templateEngine;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		final LocaleChangeInterceptor interceptor = 
				new LocaleChangeInterceptor();

		return interceptor;
	}

	@Bean
	public LocaleFilter localeFilter() {
		return new LocaleFilter();
	}

	@Bean
	public LocaleResolver localeResolver() {
		final CookieLocaleResolver resolver = 
				new CookieLocaleResolver();
		resolver.setDefaultLocale(Locale.forLanguageTag("en-UK"));
		resolver.setCookieMaxAge(Integer.MAX_VALUE);

		return resolver;
	}

	@Bean
	public Java8TimeDialect  springJava8TimeDialect (){
		return new Java8TimeDialect ();
	}
}