package com.ubs.oms.rest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ubs.oms.domain.OrderType;
import com.ubs.oms.dto.GetOrdersRequest;
import com.ubs.oms.dto.GetOrdersResponse;
import com.ubs.oms.dto.GetProductsRequest;
import com.ubs.oms.dto.GetProductsResponse;
import com.ubs.oms.dto.LoginRequest;
import com.ubs.oms.dto.LoginResponse;
import com.ubs.oms.dto.PlaceOrderRequest;
import com.ubs.oms.dto.PlaceOrderResponse;
import com.ubs.oms.dto.RegisterUserRequest;
import com.ubs.oms.dto.RegisterUserResponse;
import com.ubs.oms.exception.OmsException;
import com.ubs.oms.mapper.ErrorMapper;
import com.ubs.oms.service.OmsFacade;

@Service("OmsServicesRest")
@Component
public class OmsServicesRest {

	@Autowired
	private OmsFacade omsFacade;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/user/register")
	public RegisterUserResponse register(RegisterUserRequest request) {
		return omsFacade.register(request);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/user/login")
	public LoginResponse login(LoginRequest request) {
		LoginResponse loginResponse = omsFacade.login(request);
		Message message = PhaseInterceptorChain.getCurrentMessage();
		HttpServletResponse response = (HttpServletResponse) message.get(AbstractHTTPDestination.HTTP_RESPONSE);
		response.setHeader("oms.token", loginResponse.getToken());
		response.addCookie(new Cookie("oms.token", loginResponse.getToken()));
		return loginResponse;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/user/orders")
	public GetOrdersResponse getOrders(GetOrdersRequest request) {
		return omsFacade.getOrders(request);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/user/placeorder")
	public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {
		String orderType = request.getOrder().getType();
		if (!(OrderType.BUY.name().equalsIgnoreCase(orderType) || OrderType.SELL.name().equalsIgnoreCase(orderType))) {
			throw new OmsException(ErrorMapper.SCHEMA_ERROR);
		}
		return omsFacade.palaceOrder(request);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/product")
	public GetProductsResponse getProducts(GetProductsRequest request) {
		return omsFacade.getProducts(request);
	}

}
