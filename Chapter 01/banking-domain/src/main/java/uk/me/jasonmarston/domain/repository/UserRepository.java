package uk.me.jasonmarston.domain.repository;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
@Validated
public interface UserRepository extends JpaRepository<User, EntityId> {
	Optional<User> findByEmail(@NotNull @Valid final EmailAddress email);
	Optional<User> findByUid(@NotNull final String userId);
}