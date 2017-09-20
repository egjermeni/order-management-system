package com.ubs.oms.mapper;

/**
 * Simple class for transformin exceptions to ErrorResponse
 * 
 * @author Edmond Gjermeni
 */

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ubs.oms.dto.ErrorDetails;
import com.ubs.oms.dto.ErrorResponse;
import com.ubs.oms.exception.OmsException;

@Provider
public class ExceptionTransformer implements ExceptionMapper<Throwable> {
	@Override
	public Response toResponse(Throwable exception) {
		if (exception == null) {
			return buildGeneralErrorResponse(null);
		}
		// unwrap exception
		if (exception instanceof WebApplicationException) {
			if (exception.getCause() != null) {
				exception = exception.getCause();
			}
		}
		if (exception instanceof OmsException) {
			OmsException e = (OmsException) exception;
			if(e.getErrorCode() != null){
				return buildSpecificErrorResponse(exception);
			}
			return buildGeneralErrorResponse(exception);
		}
		return buildGeneralErrorResponse(exception);
	}

	private Response buildGeneralErrorResponse(Throwable exception) {
		Status status = Status.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse = new ErrorResponse();
		List<ErrorDetails> errors = new ArrayList<>();
		ErrorDetails error = new ErrorDetails();
		error.setErrorCode(ErrorMapper.GENERAL_ERROR.getErrorCode());
		error.setErrorText(ErrorMapper.GENERAL_ERROR.getErrorText());
		if (exception != null) {
			error.setInfo(exception.getMessage());
		}
		errors.add(error);
		errorResponse.setErrors(errors);
		return Response.status(status).entity(errorResponse).build();
	}

	private Response buildSpecificErrorResponse(Throwable exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		ErrorDetails error = new ErrorDetails();
		List<ErrorDetails> errors = new ArrayList<>();
		Status status = Status.INTERNAL_SERVER_ERROR;
		error.setErrorCode(((OmsException) exception).getErrorCode());
		error.setErrorText(((OmsException) exception).getErrorText());
		error.setInfo(((OmsException) exception).getErrorInfo());
		errors.add(error);
		errorResponse.setErrors(errors);
		return Response.status(status).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
	}
}
