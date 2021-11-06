package uk.me.jasonmarston.domain.repository;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.value.impl.Token;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
@Validated
public interface VerificationTokenRepository extends 
		JpaRepository<VerificationToken, EntityId>,
		JpaSpecificationExecutor<VerificationToken> {
	Optional<VerificationToken> findByToken(@NotNull @Valid final Token token);
}
