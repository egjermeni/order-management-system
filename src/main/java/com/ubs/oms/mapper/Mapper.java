package com.ubs.oms.mapper;

import java.util.ArrayList;
import java.util.List;

import com.ubs.oms.domain.Order;
import com.ubs.oms.domain.Product;
import com.ubs.oms.domain.User;
import com.ubs.oms.dto.OrderDetails;
import com.ubs.oms.dto.ProductDetails;
import com.ubs.oms.dto.RegisterUserRequest;
import com.ubs.oms.dto.RegisterUserResponse;

/**
 * A mapper class for mapping one object to another.
 * 
 * @author Edmond Gjermeni
 *
 */
public class Mapper {

	public static User map(final RegisterUserRequest request) {
		if (request == null) {
			return null;
		}
		final User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(request.getPassword());
		user.setCompany(request.getCompany());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		return user;
	}

	public static RegisterUserResponse map(final User user) {
		if (user == null) {
			return null;
		}
		final RegisterUserResponse response = new RegisterUserResponse();
		response.setUserId(user.getId());
		return response;
	}

	public static ProductDetails map(final Product product) {
		if (product == null) {
			return null;
		}
		final ProductDetails productDetails = new ProductDetails();
		productDetails.setId(product.getId());
		productDetails.setName(product.getName());
		productDetails.setCurrentPrice(product.getCurrentPrice());
		productDetails.setStatus(product.getStatus());
		return productDetails;
	}

	public static List<ProductDetails> map(final List<Product> products) {
		final List<ProductDetails> productList = new ArrayList<>();
		if (products != null && !products.isEmpty()) {
			for (Product product : products) {
				if (product != null) {
					productList.add(map(product));
				}
			}
		}
		return productList;
	}

	public static OrderDetails map(final Order order) {
		if (order == null) {
			return null;
		}
		final OrderDetails orderDetails = new OrderDetails();
		orderDetails.setId(order.getId());
		orderDetails.setUserId(order.getUserId());
		orderDetails.setProductId(order.getProductId());
		orderDetails.setPrice(order.getPrice());
		orderDetails.setType(order.getType());
		orderDetails.setQuantity(order.getQuantity());
		return orderDetails;
	}

	public static Order map(final OrderDetails orderDetails) {
		if (orderDetails == null) {
			return null;
		}
		final Order order = new Order();
		order.setId(orderDetails.getId());
		order.setUserId(orderDetails.getUserId());
		order.setProductId(orderDetails.getProductId());
		order.setPrice(orderDetails.getPrice());
		order.setType(orderDetails.getType());
		order.setQuantity(orderDetails.getQuantity());
		return order;
	}

	public static List<OrderDetails> mapTo(final List<Order> orders) {
		final List<OrderDetails> productList = new ArrayList<>();
		if (orders != null && !orders.isEmpty()) {
			for (Order order : orders) {
				if (order != null) {
					productList.add(map(order));
				}
			}
		}
		return productList;
	}

}
