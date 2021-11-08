package uk.me.jasonmarston.domain.user.repository;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.user.aggregate.impl.User;
import uk.me.jasonmarston.domain.user.value.impl.EmailAddress;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
@Validated
public interface UserRepository extends JpaRepository<User, EntityId> {
	Optional<User> findByEmail(@NotNull @Valid final EmailAddress email);
	Optional<User> findByUid(@NotNull final String userId);
}