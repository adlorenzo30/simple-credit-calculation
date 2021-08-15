package mx.aplazo.credit.interest.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Credit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull(message = "amount is required")
	@DecimalMin("1.00")
	private Double amount;

	@NotNull(message = "terms is required")
	@Min(1)
	private Integer terms;

	@NotNull(message = "rate is required")
	@DecimalMin("0.1")
	private Double rate;

	@OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Payment> payments;

	public Credit() {

	}

	public Credit(Double amount, Integer terms, Double rate) {
		this.amount = amount;
		this.terms = terms;
		this.rate = rate;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
		payments.forEach(p -> p.setCredit(this));
	}
}
