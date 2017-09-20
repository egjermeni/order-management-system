package com.ubs.oms.service;

import com.ubs.oms.dto.GetOrdersRequest;
import com.ubs.oms.dto.GetOrdersResponse;
import com.ubs.oms.dto.GetProductsResponse;
import com.ubs.oms.dto.OrderDetails;
import com.ubs.oms.dto.PlaceOrderResponse;

/**
 * A set for methods for managing user product orders
 * 
 * @author Edmond Gjermeni
 *
 */
public interface ProductOrderManager {

	public GetOrdersResponse getOrders(GetOrdersRequest request);

	public PlaceOrderResponse palaceOrder(OrderDetails orderDetails);

	public GetProductsResponse getProducts();

}
