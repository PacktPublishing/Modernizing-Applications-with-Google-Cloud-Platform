package uk.me.jasonmarston.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.aggregate.ResetToken;
import uk.me.jasonmarston.domain.value.Token;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
@Validated
public interface ResetTokenRepository extends 
		JpaRepository<ResetToken, EntityId>,
		JpaSpecificationExecutor<ResetToken> {
	Optional<ResetToken> findByToken(final Token token);
}