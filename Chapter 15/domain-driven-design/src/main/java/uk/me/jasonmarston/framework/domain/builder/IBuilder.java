package uk.me.jasonmarston.framework.domain.builder;

import uk.me.jasonmarston.framework.domain.DomainObject;

public interface IBuilder<T extends DomainObject> {
	T build();
}