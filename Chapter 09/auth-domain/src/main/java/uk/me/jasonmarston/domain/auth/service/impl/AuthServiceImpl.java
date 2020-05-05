package uk.me.jasonmarston.domain.auth.service.impl;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.auth.service.AuthService;
import uk.me.jasonmarston.domain.user.aggregate.User;
import uk.me.jasonmarston.domain.user.repository.UserRepository;
import uk.me.jasonmarston.framework.authentication.impl.Token;

@Service
@Validated
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.REPEATABLE_READ, 
		readOnly = false)
public class AuthServiceImpl implements AuthService {
	@Autowired
	@Lazy
	private UserRepository userRepository;

	@Value("${banking.initial.admin.email}")
    private String emailString;

	@Override
	public User sync(@NotNull Token token) {
		final Optional<User> optional = userRepository.findByUid(token.getUid());
		User user = null;
		if(optional.isPresent()) {
			user = optional.get();
			user.sync(token);
		}
		else {
			user = new User(token);
		}

		user.addAuthority(new SimpleGrantedAuthority("ROLE_USER"));
		if(user.getEmail().toString().equals(emailString)) {
			user.addAuthority(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		return userRepository.save(user);
	}
}