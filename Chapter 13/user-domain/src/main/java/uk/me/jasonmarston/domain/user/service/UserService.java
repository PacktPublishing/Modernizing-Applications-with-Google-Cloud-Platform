package uk.me.jasonmarston.domain.user.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.user.aggregate.impl.User;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface UserService {
	User findById(@NotNull @Valid final EntityId id);
}