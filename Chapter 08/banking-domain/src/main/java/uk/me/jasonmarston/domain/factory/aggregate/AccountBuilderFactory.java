package uk.me.jasonmarston.domain.factory.aggregate;

import uk.me.jasonmarston.domain.aggregate.impl.Account;
import uk.me.jasonmarston.framework.domain.factory.IFactory;

public interface AccountBuilderFactory extends IFactory<Account.Builder> {
}