package uk.me.jasonmarston.mvc.controller.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SimpleErrorController implements ErrorController {
	@Value("server.error.include-stacktrace")
	private String includeStackTrace;

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping("/error")
	public ModelAndView requestError(HttpServletRequest request) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "error.heading");

		final Instant now = Instant.now();
		final ZoneId utc = ZoneId.of("UTC");
		final ZonedDateTime current = ZonedDateTime.ofInstant(now, utc);
		final DateTimeFormatter formatter = 
				DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss zzz yyyy");
		model.addObject("timestamp", current.format(formatter));

		model.addObject("path", request.getRequestURI());
		model.addObject("status", "999");
		model.addObject("error", "UNKNOWN");

		final Integer statusCode = (Integer) request
				.getAttribute("javax.servlet.error.status_code");
		if(statusCode != null) {
			model.addObject("status", statusCode.toString());
			model.addObject("error", HttpStatus
					.valueOf(statusCode)
					.getReasonPhrase());
		}

		final Exception exception = (Exception) request
				.getAttribute("javax.servlet.error.exception");
		if(exception != null) {
			model.addObject("message", exception.getMessage());
			if(includeStackTrace.equalsIgnoreCase("always")) {
				model.addObject("exception", exception
						.getClass()
						.getCanonicalName());
				model.addObject("trace", ExceptionUtils
						.getStackTrace(exception));
			}
		}

		model.setViewName("error");

		return model;
	}
}
