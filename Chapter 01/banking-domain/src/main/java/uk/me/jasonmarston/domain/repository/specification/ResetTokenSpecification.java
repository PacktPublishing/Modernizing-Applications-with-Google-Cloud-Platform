package uk.me.jasonmarston.domain.repository.specification;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import uk.me.jasonmarston.domain.aggregate.ResetToken;

public class ResetTokenSpecification {
	public static class ResetTokenIsExpired 
			implements Specification<ResetToken> {
		private static final long serialVersionUID = 1L;

		@Override
		public Predicate toPredicate(
				final Root<ResetToken> root, 
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