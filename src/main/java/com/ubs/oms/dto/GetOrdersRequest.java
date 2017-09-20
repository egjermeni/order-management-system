package com.ubs.oms.dto;

public class GetOrdersRequest extends BaseRequest {
	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
