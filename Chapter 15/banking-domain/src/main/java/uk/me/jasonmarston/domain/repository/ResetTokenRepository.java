package uk.me.jasonmarston.domain.repository;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.aggregate.impl.ResetToken;
import uk.me.jasonmarston.domain.value.impl.Token;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
@Validated
public interface ResetTokenRepository extends 
		JpaRepository<ResetToken, EntityId>,
		JpaSpecificationExecutor<ResetToken> {
	Optional<ResetToken> findByToken(@NotNull @Valid final Token token);
}