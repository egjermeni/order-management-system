package com.ubs.oms.mapper;

/**
 * A simple error mapper for grouping a set of different errors 
 * and centralizing error codes.
 * 
 * @author Edmond Gjermeni
 */
public enum ErrorMapper{
    
    GENERAL_ERROR("ERR0001","General Error"),
    SCHEMA_ERROR("ERR0002","Invalid against schema"),
    INVALID_BUY_PRICE("ERR0003","Buy price must be equal to current product price"),
    INVALID_LOGIN("ERR0004","Invalid login"),
    ACCESS_DENIED("ERR0005","Access denied! Please login in order to access services"),
 	DUPLICATE_USER_ERROR("ERR0006","Duplicate user. Please choose different username");
	
	private String errorCode;
	private String errorText;

	ErrorMapper(String errorCode, String errorText) {
		this.errorCode = errorCode;
		this.errorText = errorText;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorText() {
		return errorText;
	}
	
	public static ErrorMapper getErrorMapper(String errorCode) {
		if (errorCode != null) {
			for (ErrorMapper errorMapper : ErrorMapper.values()) {
				if (errorMapper.getErrorCode().equalsIgnoreCase(errorCode)) {
					return errorMapper;
				}
			}
		}
		return ErrorMapper.GENERAL_ERROR;
	}

}
