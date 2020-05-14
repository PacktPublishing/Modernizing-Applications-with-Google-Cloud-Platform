package uk.me.jasonmarston.mvc.controller.advice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ConstraintAdvice {
	private static final String EXCEPTION =
			"javax.servlet.error.exception";
	private static final String STATUS_CODE = 
			"javax.servlet.error.status_code";

	@ExceptionHandler({
			TransactionSystemException.class,
			ConstraintViolationException.class,
			MethodArgumentNotValidException.class,
			IllegalArgumentException.class})
	public String handleException(final Throwable e, HttpServletRequest req) throws Throwable {
		if(e instanceof TransactionSystemException) {
			final Throwable t = ((TransactionSystemException) e).getRootCause();
			if(!(t instanceof ConstraintViolationException) &&
				!(t instanceof MethodArgumentNotValidException) &&
				!(t instanceof IllegalArgumentException)) {
				throw e;
			}
			req.setAttribute(EXCEPTION, t);
		}
		else {
			req.setAttribute(EXCEPTION, e);
		}
		req.setAttribute(STATUS_CODE,  HttpStatus.BAD_REQUEST.value());

		return "forward:/error";
	}
}