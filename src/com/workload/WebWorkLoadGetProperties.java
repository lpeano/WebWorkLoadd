package com.workload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WebWorkLoadGetProperties {
	
	public WebWorkLoadGetProperties() {
		// TODO Auto-generated constructor stub
	}
	
	public Properties getPropValues() throws IOException {		
		String propFileName="config.properties";
		Properties prop=new Properties();
		InputStream inputStream =getClass().getClassLoader().getResourceAsStream(propFileName);
		if(inputStream!=null) {
			prop.load(inputStream);
			return prop;
		} else {
			throw new FileNotFoundException("properties file: "+propFileName+"notFound in classpath");
		}
		
	}

}
