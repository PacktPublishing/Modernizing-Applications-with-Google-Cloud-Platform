package uk.me.jasonmarston.framework.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import uk.me.jasonmarston.framework.domain.entity.BeanHelper;

public class AbstractDomainObject implements DomainObject {
	private static final long serialVersionUID = 1L;

	protected ToStringBuilder _addFieldsToToString() {
		return _getToStringBuilder();
	}

	protected <T> T _getBean(final Class<T> clazz) {
		return BeanHelper.INSTANCE.getBean(clazz);
	}

	protected String[] _getExcludeFromUniqueness() {
		return new String[] {};
	}

	protected ReflectionToStringBuilder _getToStringBuilder() {
		return new ReflectionToStringBuilder(
				this,
				ToStringStyle.MULTI_LINE_STYLE)
			.setExcludeFieldNames(_getExcludeFromUniqueness());
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(
				this,
				obj,
				_getExcludeFromUniqueness());
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, _getExcludeFromUniqueness());
	}

	@Override
	public String toString() {
		return _getToStringBuilder().build();
	}
}