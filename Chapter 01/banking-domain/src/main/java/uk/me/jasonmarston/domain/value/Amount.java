package uk.me.jasonmarston.domain.value;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@Embeddable
public class Amount extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull
	@DecimalMin(value = "0.00", inclusive = true)
	@DecimalMax(value = "9999999.99", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	@Column(nullable = false, precision = 7, scale = 2)
	private BigDecimal amount;

	private Amount() {
	}

	public Amount(final String amountString) {
		this();
		this.amount = new BigDecimal(amountString);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return amount.toString();
	}
}