package com.ubs.oms.service;

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
 * A simplified interface for communication between subsystem component
 * 
 * @author Edmond Gjermeni
 *
 */
public interface OmsFacade {

	public RegisterUserResponse register(RegisterUserRequest request);

	public LoginResponse login(LoginRequest request);

	public GetProductsResponse getProducts(GetProductsRequest resquest);

	public GetOrdersResponse getOrders(GetOrdersRequest request);

	public PlaceOrderResponse palaceOrder(PlaceOrderRequest request);

}
