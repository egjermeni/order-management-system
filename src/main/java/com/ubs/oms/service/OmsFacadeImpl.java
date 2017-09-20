package com.ubs.oms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ubs.oms.domain.User;
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

/**
 * Contains implementation for oms facade
 * 
 * @author Edmond Gjermeni
 * 
 */
@Component
public class OmsFacadeImpl implements OmsFacade {
	
	@Autowired
	private AccountManager accountManager;

	@Autowired
	private ProductOrderManager  productOrderManager;

	@Override
	public RegisterUserResponse register(RegisterUserRequest request) {
		return accountManager.register(request);
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		return accountManager.login(request);
	}

	@Override
	public GetProductsResponse getProducts(GetProductsRequest resquest) {
		accountManager.checkUserLoggedin(resquest.getToken());
		return productOrderManager.getProducts();
	}

	@Override
	public GetOrdersResponse getOrders(GetOrdersRequest request) {
		User user = accountManager.checkUserLoggedin(request.getToken());
		request.setUserId(user.getId());
		return productOrderManager.getOrders(request);
	}

	@Override
	public PlaceOrderResponse palaceOrder(PlaceOrderRequest request){
		User user = accountManager.checkUserLoggedin(request.getToken());
		request.getOrder().setUserId(user.getId());
		return productOrderManager.palaceOrder(request.getOrder());

	}

}
