package com.ubs.oms.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class ErrorDetails {

	private String errorCode;
	private String errorText;
	 @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	private String info;
	
	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
}
