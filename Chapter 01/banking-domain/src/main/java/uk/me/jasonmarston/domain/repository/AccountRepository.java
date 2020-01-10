package uk.me.jasonmarston.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.aggregate.Account;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
@Validated
public interface AccountRepository extends JpaRepository<Account, EntityId> {
}