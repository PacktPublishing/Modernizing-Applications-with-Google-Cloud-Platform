package uk.me.jasonmarston.framework.domain;

import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface IdentifiableDomainObject extends DomainObject {
	EntityId getId();
}