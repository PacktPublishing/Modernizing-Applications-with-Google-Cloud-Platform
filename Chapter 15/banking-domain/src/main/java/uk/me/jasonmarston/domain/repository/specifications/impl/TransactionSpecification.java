package uk.me.jasonmarston.domain.repository.specification.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import uk.me.jasonmarston.domain.entity.impl.Transaction;
import uk.me.jasonmarston.domain.value.impl.TransactionType;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class TransactionSpecification {
	public static class DepositHasIdAndAccountId 
		implements Specification<Transaction> {

		private static final long serialVersionUID = 1L;
		private EntityId id;
		private EntityId accountId;

		public DepositHasIdAndAccountId(
				final EntityId id,
				final EntityId accountId) {
			this.accountId = accountId;
			this.id = id;
		}

		@Override
		public Predicate toPredicate(final Root<Transaction> root,
				final CriteriaQuery<?> query,
				final CriteriaBuilder builder) {
			return new TransactionSpecification
					.TransactionHasIdAccountIdAndType(id,
							accountId,
							TransactionType.DEPOSIT)
					.toPredicate(root, query, builder);
		}
	}

	public static class TransactionHasIdAccountIdAndType 
			implements Specification<Transaction> {
		private static final long serialVersionUID = 1L;
		private EntityId id;
		private EntityId accountId;
		private TransactionType transactionType;

		public TransactionHasIdAccountIdAndType(
				final EntityId id,
				final EntityId accountId,
				final TransactionType transactionType) {
			this.id = id;
			this.accountId = accountId;
			this.transactionType = transactionType;
		}

		private Predicate _accountId(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("account").get("id"),
					this.accountId);			
		}

		private Predicate _id(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("id"), this.id);
		}

		private Predicate _transactionType(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("type"), transactionType);
		}

		@Override
		public Predicate toPredicate(
				final Root<Transaction> root, 
				final CriteriaQuery<?> query, 
				final CriteriaBuilder builder) {
			return builder.and(
					_id(root, builder),
					builder.and(
						_accountId(root, builder),
						_transactionType(root, builder)));
		}
	}

	public static class TransactionHasIdAndAccountId 
			implements Specification<Transaction> {

		private static final long serialVersionUID = 1L;
		private EntityId id;
		private EntityId accountId;

		public TransactionHasIdAndAccountId(
				final EntityId id,
				final EntityId accountId) {
			this.id = id;
			this.accountId = accountId;
		}

		private Predicate _accountId(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("id"), this.accountId);
		}

		private Predicate _id(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("id"), this.id);
		}

		@Override
		public Predicate toPredicate(
				final Root<Transaction> root, 
				final CriteriaQuery<?> query, 
				final CriteriaBuilder builder) {
			return builder.and(_id(root, builder), _accountId(root, builder));
		}
	}

	public static class WithdrawalHasIdAndAccountId 
		implements Specification<Transaction> {

		private static final long serialVersionUID = 1L;
		private EntityId id;
		private EntityId accountId;

		public WithdrawalHasIdAndAccountId(
				final EntityId id,
				final EntityId accountId) {
			this.id = id;
			this.accountId = accountId;
		}

		@Override
		public Predicate toPredicate(final Root<Transaction> root,
				final CriteriaQuery<?> query,
				final CriteriaBuilder builder) {
			return new TransactionSpecification
					.TransactionHasIdAccountIdAndType(id,
							accountId,
							TransactionType.WITHDRAWAL)
					.toPredicate(root, query, builder);
		}
	}
}