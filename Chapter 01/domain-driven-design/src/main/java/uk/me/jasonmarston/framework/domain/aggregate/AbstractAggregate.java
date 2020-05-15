package uk.me.jasonmarston.framework.domain.aggregate;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import uk.me.jasonmarston.framework.domain.entity.AbstractEntity;

@MappedSuperclass
public abstract class AbstractAggregate extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Version
	private Long version;

	public Long getVersion() {
		return version;
	}
}