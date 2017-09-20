package com.ubs.oms.exception;

import com.ubs.oms.mapper.ErrorMapper;

/**
 * Simple order management system  exception
 * 
 * @author Edmond Gjermeni
 *
 */
public class OmsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String errorText;
	private String errorInfo;

	public OmsException(String errorInfo) {
		super(errorInfo);
	}

	public OmsException(ErrorMapper errorMapper) {
		this.errorCode = errorMapper.getErrorCode();
		this.errorText = errorMapper.getErrorText();
		this.errorInfo = getMessage();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorText() {
		return errorText;
	}

	public String getErrorInfo() {
		return errorInfo;
	};
}
