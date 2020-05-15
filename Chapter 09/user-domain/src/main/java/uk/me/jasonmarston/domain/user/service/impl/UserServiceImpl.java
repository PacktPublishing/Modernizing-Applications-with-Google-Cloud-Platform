package uk.me.jasonmarston.domain.user.service.impl;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.user.aggregate.impl.User;
import uk.me.jasonmarston.domain.user.repository.UserRepository;
import uk.me.jasonmarston.domain.user.service.UserService;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Service
@Validated
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.REPEATABLE_READ, 
		readOnly = false)
public class UserServiceImpl implements UserService {
	@Autowired
	@Lazy
	private UserRepository userRepository;

	@Override
	public User findById(@NotNull @Valid final EntityId id) {
		final Optional<User> optional = userRepository.findById(id); 
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}
}