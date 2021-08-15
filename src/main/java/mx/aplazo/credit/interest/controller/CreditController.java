package mx.aplazo.credit.interest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mx.aplazo.credit.interest.model.Credit;
import mx.aplazo.credit.interest.model.Payment;
import mx.aplazo.credit.interest.service.CreditService;

@RestController
public class CreditController {

	@Autowired
	private CreditService creditServ;

	@GetMapping("/")
	public List<Credit> getCredits() {
		return creditServ.getAll();
	}

	@PostMapping("/")
	public List<Payment> calculatePayments(@RequestBody @Valid Credit credit) {
		return creditServ.calculatePayments(credit);
	}
}
