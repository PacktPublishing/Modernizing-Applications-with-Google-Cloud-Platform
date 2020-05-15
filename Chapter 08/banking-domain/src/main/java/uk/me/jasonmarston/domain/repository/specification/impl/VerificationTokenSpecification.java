package uk.me.jasonmarston.domain.repository.specification.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;

public class VerificationTokenSpecification {
	public static class VerificationTokenIsExpired 
			implements Specification<VerificationToken> {
		private static final long serialVersionUID = 1L;

		@Override
		public Predicate toPredicate(
				final Root<VerificationToken> root, 
				final CriteriaQuery<?> query, 
				final CriteriaBuilder builder) {
			final Instant now = Instant.now();
			final ZoneId utc = ZoneId.of("UTC");
			final ZonedDateTime current = ZonedDateTime.ofInstant(now, utc);

			return builder.lessThanOrEqualTo(
					root.<ZonedDateTime>get("expiryDate"),
					current);
		}
	}
}