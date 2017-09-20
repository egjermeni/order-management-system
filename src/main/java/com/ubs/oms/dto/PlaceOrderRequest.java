package com.ubs.oms.dto;

public class PlaceOrderRequest extends BaseRequest {

	private OrderDetails order;

	public OrderDetails getOrder() {
		return order;
	}

	public void setOrder(OrderDetails order) {
		this.order = order;
	}

}
