package uk.me.jasonmarston.mvc.alerts;

public class AlertInfo extends AbstractAlert {
	public AlertInfo(final String key) {
		super(key);
		type = "alert-info";
	}
}
