package com.biztweets.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

@Repository
@PropertySource("classpath:environment.properties")
public class PropertyReader {
	
	@Autowired
	private Environment env;
	
	public String getProperty(String key) {
		return env.getProperty(key);
	}

}
