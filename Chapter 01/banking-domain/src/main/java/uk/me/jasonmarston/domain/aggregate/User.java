package uk.me.jasonmarston.domain.aggregate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.entity.Authority;
import uk.me.jasonmarston.domain.factory.aggregate.UserBuilderFactory;
import uk.me.jasonmarston.domain.factory.entity.AuthorityBuilderFactory;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.framework.authentication.impl.Token;
import uk.me.jasonmarston.framework.domain.aggregate.AbstractAggregate;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;

@Entity(name = "USER_PROFILES")
public class User extends AbstractAggregate implements UserDetails {
	public static class Builder implements IBuilder<User> {
		private EmailAddress email;
		private Password password;
		private Locale locale;
		private Set<GrantedAuthority> authorities = 
				new HashSet<GrantedAuthority>();

		private Builder() {
			this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		
		public Builder addAuthority(final SimpleGrantedAuthority authority) {
			authorities.add(authority);
			return this;
		}

		@Override
		public User build() {
			if(email == null || password == null || locale == null) {
				throw new 
					IllegalArgumentException("Invalid registration details");
			}

			final User user = new User();
			user.email = email;
			user.changePassword(password);
			user.locale = locale;
			for(GrantedAuthority authority : authorities) {
				user.addAuthority(authority);
			}

			return user;
		}

		public Builder forEmail(final EmailAddress email) {
			this.email = email;
			return this;
		}

		public Builder inLocale(final Locale locale) {
			this.locale = locale;
			return this;
		}

		public Builder withPassword(final Password password) {
			this.password = password;
			return this;
		}
	}

	@Service
	public static class Factory implements UserBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@NotNull
	@AttributeOverride(name="email", column=@Column(name="email", columnDefinition = "CHAR(250)", unique = true, nullable = false))
	private EmailAddress email;

	@NotNull
	@Column(columnDefinition = "VARCHAR(42)", nullable = false)
	private Locale locale;

	private String picture;

	@Transient
	private String credentials;

	@NotNull
	@Column(nullable = false)
	private Password password;

	@NotNull
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
	private Set<Authority> authorities = new HashSet<Authority>();
	
	@Transient
	private Set<GrantedAuthority> _authorities = 
			new HashSet<GrantedAuthority>();
	
	@NotNull
	@Column(columnDefinition = "VARCHAR(100)", unique = true, nullable = false)
	private String uid;

	private boolean enabled = false;

	@Column(columnDefinition="TIMESTAMP", nullable = false)
	private ZonedDateTime lastLogin = _getTestDateTime();

	@Column(columnDefinition="TIMESTAMP", nullable = false)
	private ZonedDateTime lastLoginFailure = _getTestDateTime();

	@Column(nullable = false)
	private int failedLogins = 0;

	private User() {
		super();
		uid = getId().getId().toString();
	}
	
	public User(Token token) {
		this.uid = token.getUid();
		this.email = new EmailAddress(token.getEmail());
		this.enabled = token.isEmailVerified();
		this.picture = token.getPicture();
		this.password = new Password(_getBean(PasswordEncoder.class)
				.encode(UUID.randomUUID().toString()));
		this.locale = Locale.forLanguageTag("en-UK");
	}

	private Set<GrantedAuthority> _getAuthorities() {
		if(!_authorities.isEmpty()) {
			return _authorities;
		}

		if(!authorities.isEmpty()) {
			for(final Authority authority : authorities) {
				_authorities.add(authority.getAuthority());
			}
		}

		return _authorities;
	}

	@Override
	protected String[] _getExcludeFromUniqueness() {
		return new String[] { "_authorities", "authorities", "credentials" };
	}

	private ZonedDateTime _getTestDateTime() {
		final Instant fiveMinutesAgo = Instant
				.now()
				.minus(5, ChronoUnit.MINUTES);
		final ZoneId utc = ZoneId.of("UTC");
		final ZonedDateTime dateTime = ZonedDateTime
				.ofInstant(fiveMinutesAgo, utc);
		return dateTime;
	}

	private ZonedDateTime _updateAccountNonLocked() {
		final ZonedDateTime dateTime = _getTestDateTime(); 
		if(lastLoginFailure == null) {
			failedLogins = 0;
			return dateTime;
		}
		if(dateTime.isAfter(lastLoginFailure)) {
			failedLogins = 0;
		}
		return dateTime;
	}

	public boolean addAuthority(final GrantedAuthority grantedAuthority) {
		if(_getAuthorities().add(grantedAuthority)) {
			final Authority.Builder builder = 
					_getBean(AuthorityBuilderFactory.class).create();

			final Authority authority = builder
					.forUser(this)
					.withAuthority(grantedAuthority)
					.build();

			authorities.add(authority);

			return true;
		}
		return false;
	}

	public void changePassword(final Password password) {
		this.password = new Password(_getBean(PasswordEncoder.class)
				.encode(password.toString()));
	}

	public void enable() {
		enabled = true;
	}

	@Override
	public Set<GrantedAuthority> getAuthorities() {
		return Collections.unmodifiableSet(_getAuthorities());
	}

	public String getCredentials() {
		return credentials;
	}

	public String getEmail() {
		return email.toString();
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public String getPassword() {
		return password.toString();
	}

	public String getPicture() {
		return picture;
	}
	
	public String getUid() {
		return uid;
	}

	@Override
	public String getUsername() {
		return email.toString();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		if(failedLogins == 0) {
			return true;
		}
		final ZonedDateTime dateTime = _updateAccountNonLocked();
		if(lastLoginFailure == null) {
			return true;
		}
		if(dateTime.isAfter(lastLoginFailure)) {
			return true;
		}
		if(failedLogins > 4) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isCurrentPassword(final Password password) {
		return _getBean(PasswordEncoder.class).matches(
				password.getPassword(),
				this.password.getPassword());
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public boolean login(final Password password) {
		final Instant now = Instant.now();
		final ZoneId utc = ZoneId.of("UTC");
		final ZonedDateTime current = ZonedDateTime.ofInstant(now, utc);
		if(isCurrentPassword(password)) {
			failedLogins = 0;
			lastLogin = current;
			return true;
		}
		else {
			_updateAccountNonLocked();
			failedLogins++;
			lastLoginFailure = current;
			return false;
		}
	}

	public boolean removeAuthority(final GrantedAuthority grantedAuthority) {
		if(!_getAuthorities().remove(grantedAuthority)) {
			return false;
		}
		for(final Authority authority : authorities) {
			if(authority.getAuthority().equals(grantedAuthority)) {
				authorities.remove(authority);
				break;
			}
		}
		return true;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
	
	public void sync(Token token) {
		this.email = new EmailAddress(token.getEmail());
		this.enabled = token.isEmailVerified();
		this.picture = token.getPicture();
	}
}