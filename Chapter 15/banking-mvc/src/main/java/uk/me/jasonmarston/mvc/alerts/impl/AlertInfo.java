package uk.me.jasonmarston.mvc.alerts.impl;

import uk.me.jasonmarston.mvc.alerts.AbstractAlert;

public class AlertInfo extends AbstractAlert {
	public AlertInfo(final String key) {
		super(key);
		type = "alert-info";
	}
}
