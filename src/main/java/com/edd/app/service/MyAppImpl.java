package com.edd.app.service;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MyAppImpl {

	private String url;
	private String host;
	private String port;
	private String schema;


//	@PostConstruct
//	public void initApp() {
//
//	}

	public void testMethod(){
		//System.out.println("host="+host);
		System.out.println("url="+url);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}


	public void setSchema(String schema) {
		this.schema = schema;
	}
}
