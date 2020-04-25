package uk.me.jasonmarston.domain.service.impl;

import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.details.RegistrationDetails;
import uk.me.jasonmarston.domain.factory.aggregate.UserBuilderFactory;
import uk.me.jasonmarston.domain.repository.UserRepository;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.framework.authentication.impl.Token;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Service
@Validated
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.REPEATABLE_READ, 
		readOnly = false)
public class UserServiceImpl implements UserService {
	@Autowired
	@Lazy
	private UserBuilderFactory userBuilderFactory;

	@Autowired
	@Lazy
	private UserRepository userRepository;

	@Value("${banking.initial.admin.email}")
    private String emailString;

	@Override
	public User addAuthority(
			@NotNull @Valid final EntityId id,
			@NotNull final GrantedAuthority authority) {
		final Optional<User> optional = userRepository.findById(id);
		if(optional.isPresent()) {
			final User user = optional.get();
			user.addAuthority(authority);
			return userRepository.save(user);
		}
		throw new IllegalArgumentException("Invalid UserId");
	}

	@Override
	public User changePassword(
			@NotNull @Valid EntityId id,
			@NotNull @Valid Password password) {
		final Optional<User> optional = userRepository.findById(id);
		if(optional.isPresent()) {
			final User user = optional.get();
			user.changePassword(password);
			return userRepository.save(user);
		}

		throw new IllegalArgumentException("Invalid UserId");
	}

	@Override
	public void delete(@NotNull @Valid EntityId id) {
		userRepository.deleteById(id);
	}

	@Override
	public User enable(@NotNull @Valid EntityId id) {
		final Optional<User> optional = userRepository.findById(id);
		if(optional.isPresent()) {
			final User user = optional.get();
			user.enable();
			return userRepository.save(user);
		}
		throw new IllegalArgumentException("Invalid UserId");
	}

	@Override
	public User findByEmail(@NotNull @Valid final EmailAddress email) {
		final Optional<User> optional = userRepository.findByEmail(email);
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public User findById(@NotNull @Valid final EntityId id) {
		final Optional<User> optional = userRepository.findById(id); 
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public boolean isCurrentPassword(@NotNull @Valid EntityId id, @NotNull Password password) {
		final Optional<User> optional = userRepository.findById(id);
		if(optional.isPresent()) {
			final User user = optional.get();
			return user.isCurrentPassword(password);
		}

		throw new IllegalArgumentException("Invalid UserId");
	}

	@Override
	public boolean login(@NotNull @Valid EntityId id, @NotNull Password password) {
		final Optional<User> optional = userRepository.findById(id);
		if(optional.isPresent()) {
			final User user = optional.get();
			final boolean loggedIn = user.login(password); 
			userRepository.save(user);
			return loggedIn;
		}

		throw new IllegalArgumentException("Invalid UserId");
	}

	@Override
	public User register(
			@NotNull @Valid final RegistrationDetails registrationDetails) {
		if(userRepository.findByEmail(registrationDetails.getEmail())
						.isPresent()) {
			throw new EntityExistsException("Already registered.");
		}

		final User.Builder builder = userBuilderFactory.create();

		final User user = builder
				.forEmail(registrationDetails.getEmail())
				.withPassword(registrationDetails.getPassword())
				.inLocale(registrationDetails.getLocale())
				.build();

		return userRepository.save(user);
	}

	@Override
	public User registerAdministrator(
			@NotNull @Valid final RegistrationDetails registrationDetails) {
		if(userRepository.findByEmail(registrationDetails.getEmail())
						.isPresent()) {
			throw new EntityExistsException("Already registered.");
		}

		final User.Builder builder = userBuilderFactory.create();

		final User user = builder
				.forEmail(registrationDetails.getEmail())
				.withPassword(registrationDetails.getPassword())
				.inLocale(registrationDetails.getLocale())
				.build();
		
		user.addAuthority(new SimpleGrantedAuthority("ROLE_ADMIN"));
		user.enable();

		return userRepository.save(user);
	}

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