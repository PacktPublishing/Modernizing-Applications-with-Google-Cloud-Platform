package uk.me.jasonmarston.framework.domain.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import uk.me.jasonmarston.framework.domain.AbstractDomainObject;
import uk.me.jasonmarston.framework.domain.IdentifiableDomainObject;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@MappedSuperclass
public abstract class AbstractEntity extends AbstractDomainObject 
		implements IdentifiableDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@JsonUnwrapped
	@NotNull
	protected EntityId id;

	protected AbstractEntity() {
		this.id = new EntityId();
	}

	@Override
	public EntityId getId() {
		return id;
	}
}