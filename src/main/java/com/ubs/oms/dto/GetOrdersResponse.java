package com.ubs.oms.dto;

import java.util.List;

public class GetOrdersResponse {
	private List<OrderDetails> orders;

	public List<OrderDetails> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDetails> orders) {
		this.orders = orders;
	}

}
