package uk.me.jasonmarston.mvc.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.mvc.controller.AuthenticationHelper;

public class LocaleFilter extends OncePerRequestFilter {
	@Autowired
	@Lazy
	private LocaleResolver localeResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final User user = AuthenticationHelper.getUser();
		if(user != null) {
			localeResolver.setLocale(request, response, user.getLocale());
		}

		filterChain.doFilter(request, response);
	}
}
