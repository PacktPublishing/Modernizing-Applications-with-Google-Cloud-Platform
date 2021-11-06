package uk.me.jasonmarston.mvc.alerts.impl;

import uk.me.jasonmarston.mvc.alerts.AbstractAlert;

public class AlertDanger extends AbstractAlert {
	public AlertDanger(final String key) {
		super(key);
		type = "alert-danger";
	}
}
