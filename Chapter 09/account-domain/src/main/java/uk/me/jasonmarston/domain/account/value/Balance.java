package uk.me.jasonmarston.domain.account.value;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@Embeddable
public class Balance extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull
	@DecimalMin(value = "-9999999.99", inclusive = true)
	@DecimalMax(value = "9999999.99", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	@Column(nullable = false, precision = 7, scale = 2)
	private BigDecimal balance;

	private Balance() {
	}

	public Balance(final BigDecimal balance) {
		this();
		this.balance = balance;
	}

	public Balance(final String amountString) {
		this.balance = new BigDecimal(amountString);
	}

	public Balance add(final Amount amount) {
		return new Balance(balance.add(amount.getAmount()));
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Balance subtract(final Amount amount) {
		return new Balance(balance.subtract(amount.getAmount()));
	}

	@Override
	public String toString() {
		return balance.toString();
	}
}