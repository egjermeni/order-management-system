package com.ubs.oms.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Edmond.Gjermeni
 *
 */
@Entity
@Table(name="orders")
public class Order implements Serializable{
	private static final long serialVersionUID = 6290282840332317641L;
	//public static enum Type{BUY, SELL}
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Access(AccessType.PROPERTY)
	private Integer id;

	@Column(name="user_id")
	@Access(AccessType.PROPERTY)
	private Integer userId;

	@Column(name="product_id")
	@Access(AccessType.PROPERTY)
	private Integer productId;
	
	@Column(name="order_type")
	@Access(AccessType.PROPERTY)
	private String type;

	@Column(name="price")
	@Access(AccessType.PROPERTY)
	private Double price;
	
	@Column(name="quantity")
	@Access(AccessType.PROPERTY)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Order)) {
			return false;
		}
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	
}
