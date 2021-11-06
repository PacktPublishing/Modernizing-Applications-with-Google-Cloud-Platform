package uk.me.jasonmarston.domain.factory.aggregate;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.framework.domain.factory.IFactory;

public interface UserBuilderFactory extends IFactory<User.Builder> {
}