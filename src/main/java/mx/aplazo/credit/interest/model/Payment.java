package mx.aplazo.credit.interest.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Payment {

	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;

	private Integer number;

	private Double amount;

	private LocalDate payBefore;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Credit credit;

	public Payment() {

	}

	public Payment(Integer number, Double amount, LocalDate payBefore) {
		this.number = number;
		this.amount = amount;
		this.payBefore = payBefore;
	}

}
