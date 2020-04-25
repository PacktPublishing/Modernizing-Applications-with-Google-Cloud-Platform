package uk.me.jasonmarston.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.factory.entity.AuthorityBuilderFactory;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.entity.AbstractEntity;

@Entity(name ="USER_AUTHORITIES")
public class Authority extends AbstractEntity {
	public static class Builder implements IBuilder<Authority> {
		private User user;
		private GrantedAuthority grantedAuthority;

		@Override
		public Authority build() {
			if(user == null || grantedAuthority == null) {
				throw new IllegalArgumentException("Invalid authority");
			}

			final Authority authority = new Authority();
			authority.user = user;
			authority.authority = grantedAuthority.getAuthority();

			return authority;
		}

		public Builder forUser(final User user) {
			this.user = user;
			return this;
		}

		public Builder withAuthority(final GrantedAuthority grantedAuthority) {
			this.grantedAuthority = grantedAuthority;
			return this;
		}
	}

	@Service
	public static class FactoryImpl implements AuthorityBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@AttributeOverride(name="id", column=@Column(name="userId", nullable = false))
	@NotNull
	private User user;

	@NotNull
	@Column(columnDefinition = "VARCHAR(20)")
	private String authority;
	
	private Authority() {
		super();
	}

	@Override
	protected ToStringBuilder _addFieldsToToString() {
		return _getToStringBuilder()
				.append("userId", user.getId());
	}

	@Override
	protected String[] _getExcludeFromUniqueness() {
		return new String[] { "user" };
	}

	public GrantedAuthority getAuthority() {
		return new SimpleGrantedAuthority(authority);
	}
}