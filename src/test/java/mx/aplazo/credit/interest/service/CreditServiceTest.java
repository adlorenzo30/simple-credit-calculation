package mx.aplazo.credit.interest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.aplazo.credit.interest.model.Credit;
import mx.aplazo.credit.interest.model.Payment;
import mx.aplazo.credit.interest.repository.CreditRepository;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

	@InjectMocks
	private CreditService creditServ;

	@Mock
	private CreditRepository creditRepo;

	@DisplayName("calculatePayments method")
	@ParameterizedTest
	@CsvSource({ "50000.00, 52, 10.0", "120000.50,43,4.5", "543.89,5,7.7" })
	void test(double amount, int terms, double rate) {
		// prepare mock
		when(creditRepo.save(any(Credit.class))).thenReturn(null);

		// prepare call
		// putting terms into years for simplicity
		double years = (double) terms / 52;
		double expectedCreditAmount = amount / (rate / 100 * years);

		Credit creditRequest = new Credit(amount, terms, rate);

		// execute call
		List<Payment> result = creditServ.calculatePayments(creditRequest);

		// evaluate results
		assertEquals(terms, result.size());
		BigDecimal creditAmount = BigDecimal.ZERO;
		for (int i = 0; i < result.size(); i++) {
			assertEquals(i + 1, result.get(i).getNumber());
			creditAmount = creditAmount.add(new BigDecimal(result.get(i).getAmount()));
			if (i < result.size() - 1)
				assertEquals(result.get(i).getPayBefore().plusDays(7), result.get(i + 1).getPayBefore());
		}
		assertTrue(expectedCreditAmount <= creditAmount.setScale(2, RoundingMode.CEILING).doubleValue());
		
		creditRequest.setPayments(result);
		verify(creditRepo,times(1)).save(creditRequest);
	}
}
