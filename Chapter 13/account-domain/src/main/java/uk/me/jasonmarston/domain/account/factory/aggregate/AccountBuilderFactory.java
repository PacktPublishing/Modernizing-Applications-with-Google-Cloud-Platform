package uk.me.jasonmarston.domain.account.factory.aggregate;

import uk.me.jasonmarston.domain.account.aggregate.impl.Account;
import uk.me.jasonmarston.framework.domain.factory.IFactory;

public interface AccountBuilderFactory extends IFactory<Account.Builder> {
}