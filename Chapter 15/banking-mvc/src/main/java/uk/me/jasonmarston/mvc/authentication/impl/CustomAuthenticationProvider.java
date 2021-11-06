package uk.me.jasonmarston.mvc.authentication.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.impl.EmailAddress;
import uk.me.jasonmarston.domain.value.impl.Password;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	@Lazy
	private UserService userService;

	private void _validate(final User user, final String password) {
		if(user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		if(!user.isEnabled()) {
			throw new DisabledException("User not enabled");
		}
		
		if(!user.isAccountNonLocked()) {
			throw new LockedException("User account locked");
		}
		
		if(!user.isAccountNonExpired()) {
			throw new AccountExpiredException("User account expired");
		}
		
		if(!user.isCredentialsNonExpired()) {
			throw new CredentialsExpiredException("User credentials expired");
		}

		if(!userService.login(
				user.getId(),
				new Password(password))) {
			if(!userService.findById(user.getId()).isAccountNonLocked()) {
				throw new LockedException("User account locked");
			}
			throw new BadCredentialsException("User credentials bad");
		}
	}

	@Override
	public Authentication authenticate(final Authentication authentication)
			throws AuthenticationException {
		final String email = authentication.getName();
		final String password = authentication.getCredentials().toString();

		final User user = userService.findByEmail(new EmailAddress(email));

		_validate(user, password);

		return new UsernamePasswordAuthenticationToken(
				user,
				user.getPassword(),
				user.getAuthorities());
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication
				.equals(UsernamePasswordAuthenticationToken.class);
	}
}