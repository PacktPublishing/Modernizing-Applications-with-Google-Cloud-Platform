package uk.me.jasonmarston.domain.account.repository;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.account.aggregate.impl.Account;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
@Validated
public interface AccountRepository extends JpaRepository<Account, EntityId> {
	List<Account> findByOwnerId(@NotNull @Valid final String ownerId);
}