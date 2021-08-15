package mx.aplazo.credit.interest.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.aplazo.credit.interest.model.Credit;
import mx.aplazo.credit.interest.model.Payment;
import mx.aplazo.credit.interest.repository.CreditRepository;

@Service
public class CreditService {
	
	@Autowired
	private CreditRepository creditRepo;

	public List<Payment> calculatePayments(Credit credit) {
		// convert percent rate to decimal
		double rate = credit.getRate() / 100;
		// putting terms into years for simplicity
		double years = (double) credit.getTerms() / 52;
		// calculate credit amount
		BigDecimal creditAmount = new BigDecimal(credit.getAmount() / (rate * years));

		// calculate individual payment amount
		BigDecimal paymentAmount = creditAmount.divide(new BigDecimal(credit.getTerms()), 2, RoundingMode.CEILING);

		// get initial payBefore date
		LocalDate payBefore = LocalDate.now();

		// prepare list of payments
		List<Payment> payments = new ArrayList<>(credit.getTerms());

		for (int i = 0; i < credit.getTerms(); i++) {
			payments.add(new Payment(i + 1, paymentAmount.doubleValue(), payBefore.plusDays(i * 7)));
		}
		
		// save credit to DB
		credit.setPayments(payments);
		creditRepo.save(credit);

		return payments;
	}
	
	public List<Credit> getAll(){
		return creditRepo.findAll();
	}

}
