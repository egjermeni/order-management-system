package com.ubs.oms.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author Edmond.Gjermeni
 *
 */
public class OrderDetails {
	private Integer id;
	
	 @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	private Integer userId;

	private Integer productId;

	private String type;

	private Double price;

	private Integer quantity;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
