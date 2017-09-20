package com.ubs.oms.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

	private List<ErrorDetails> errors;

	public ErrorResponse() {
	    errors = new ArrayList<>();
    }
	
	public void addError(ErrorDetails error){
	    if(error !=null){
	        errors.add(error);
	    }
	}
	
	public List<ErrorDetails> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorDetails> errors) {
		this.errors = errors;
	}

	
}
