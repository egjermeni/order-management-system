package com.ubs.oms.dto;

import java.util.List;

public class GetProductsResponse {

	private List<ProductDetails> products;

	public List<ProductDetails> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDetails> products) {
		this.products = products;
	}

}
