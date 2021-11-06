package uk.me.jasonmarston.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.entity.impl.Transaction;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
@Validated
public interface TransactionRepository extends 
		JpaRepository<Transaction, EntityId>,
		JpaSpecificationExecutor<Transaction> {
	Optional<Transaction> findByAccountIdAndJournalCodeAndIsCorrection(
			final EntityId accountId,
			final EntityId journalCode,
			final boolean isCorrection);
}