package uk.me.jasonmarston.framework.domain.factory;

import uk.me.jasonmarston.framework.domain.DomainObject;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;

public interface IFactory<B extends IBuilder<? extends DomainObject>> {
	B create();
}