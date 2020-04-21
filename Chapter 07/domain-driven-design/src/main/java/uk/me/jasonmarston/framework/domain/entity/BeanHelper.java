package uk.me.jasonmarston.framework.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class BeanHelper {
	public static BeanHelper INSTANCE;
	
	@Autowired
	private ApplicationContext context;
	
	private BeanHelper() {
		INSTANCE = this;
	}

	public <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}
}
