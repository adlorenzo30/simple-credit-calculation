package mx.aplazo.credit.interest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import mx.aplazo.credit.interest.model.Credit;
import mx.aplazo.credit.interest.model.Payment;
import mx.aplazo.credit.interest.service.CreditService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CreditController.class)
public class CreditControllerTest {

	@MockBean
	private CreditService creditServ;

	@Autowired
	private MockMvc mockMvc;

	@Nested
	class CalculatePayments {
		
		@Test
		@DisplayName("Happy pass")
		void test() throws Exception {
			// prepare mock
			Payment p1 = new Payment(1, 500.00, LocalDate.of(2021, 8, 15));
			Payment p2 = new Payment(2, 500.00, LocalDate.of(2021, 8, 22));
			List<Payment> payments = new ArrayList<>(2);
			payments.add(p1);
			payments.add(p2);
			when(creditServ.calculatePayments(any(Credit.class))).thenReturn(payments);

			// execute call
			String requestBody = "{\"amount\":50000,\"terms\":52,\"rate\":10}";
			MockHttpServletResponse response = mockMvc
					.perform(post("/").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn()
					.getResponse();

			// validate results
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		}

		@DisplayName("validation fail")
		@ParameterizedTest
		@CsvSource({ "{\"amount\":null,\"terms\":52,\"rate\":10.0}",
				"{\"amount\":50000.00,\"terms\":null,\"rate\":10.0}",
				"{\"amount\":50000.00,\"terms\":52,\"rate\":null}", "{\"amount\":0.1,\"terms\":52,\"rate\":10.4}",
				"{\"amount\":50000.00,\"terms\":0,\"rate\":10.0}", "{\"amount\":50000.00,\"terms\":52,\"rate\":0}" })
		void test2(String requestBody) throws Exception {
			// execute call
			MockHttpServletResponse response = mockMvc
					.perform(post("/").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andReturn()
					.getResponse();

			// validate results
			assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		}
	}

}
