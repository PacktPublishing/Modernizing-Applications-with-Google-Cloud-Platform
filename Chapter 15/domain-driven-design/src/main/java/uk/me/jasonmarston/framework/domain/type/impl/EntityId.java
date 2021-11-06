package uk.me.jasonmarston.framework.domain.type.impl;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@Embeddable
public class EntityId extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Id is required")
	@Column(columnDefinition = "CHAR(36)" )
	private String id;

	public EntityId() {
		this.id = UUID.randomUUID().toString();
	}

	public EntityId(final String stringId) {
		UUID.fromString(stringId);
		this.id = stringId;
	}

	public EntityId(final UUID id) {
		this.id = id.toString();
	}

	public UUID getId() {
		return UUID.fromString(id);
	}
}