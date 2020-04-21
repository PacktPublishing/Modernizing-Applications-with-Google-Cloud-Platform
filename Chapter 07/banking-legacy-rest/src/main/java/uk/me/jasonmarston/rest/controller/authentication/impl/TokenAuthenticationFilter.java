package uk.me.jasonmarston.rest.controller.authentication.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.factory.details.RegistrationDetailsBuilderFactory;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.framework.authentication.impl.JwtValidation;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger LOGGER = 
			LoggerFactory.getLogger(TokenAuthenticationFilter.class);
	
	@Autowired
	@Lazy
	private UserService userService;
	
	@Autowired
	@Lazy
	private RegistrationDetailsBuilderFactory
			registrationDetailsBuilderFactory;
	
	private JwtValidation auth = null;

	public TokenAuthenticationFilter() {
		try {
			auth = JwtValidation.getInstance();
		} catch (final IOException e) {
			logError(e);
		}
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, 
									final HttpServletResponse response, 
									final FilterChain filterChain)
			throws ServletException, IOException {
		try {
			final String jwt = getJwtFromRequest(request);
			if (StringUtils.hasText(jwt)) {
				final User user = userService.sync(auth.verifyIdToken(jwt));
				user.setCredentials(jwt);

				final UsernamePasswordAuthenticationToken authentication = 
						createAuthenticationToken(user);

				authentication
					.setDetails(
							new WebAuthenticationDetailsSource()
								.buildDetails(request)
					);

				SecurityContextHolder.getContext()
						.setAuthentication(authentication);

				try {
					filterChain.doFilter(request, response);
				}
				catch(final NestedServletException e) { 
					logError(e);
					response.sendError(
							HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Internal server error");
				}
			}
			else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Please provide credentials");
			}
        }
		catch(final RuntimeException | Error e) {
			logError(e);
			response.sendError(
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Internal server error");
		}
		catch (final Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Please provide valid credentials");
        }
	}

	private UsernamePasswordAuthenticationToken createAuthenticationToken(
			final User user) {
		return new UsernamePasswordAuthenticationToken(
				user,
				user.getCredentials(), 
				user.getAuthorities());
	}

	private String getJwtFromRequest(final HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken)
				&& bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
    }

	private void logError(final Throwable e) {
		final StringWriter stack = new StringWriter();
	    e.printStackTrace(new PrintWriter(stack));
		LOGGER.error(stack.toString());
	}
}