package com.ubs.oms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ubs.oms.dao.OrderDao;
import com.ubs.oms.dao.ProductDao;
import com.ubs.oms.domain.Order;
import com.ubs.oms.domain.OrderType;
import com.ubs.oms.domain.Product;
import com.ubs.oms.dto.GetOrdersRequest;
import com.ubs.oms.dto.GetOrdersResponse;
import com.ubs.oms.dto.GetProductsResponse;
import com.ubs.oms.dto.OrderDetails;
import com.ubs.oms.dto.PlaceOrderResponse;
import com.ubs.oms.exception.OmsException;
import com.ubs.oms.mapper.ErrorMapper;
import com.ubs.oms.mapper.Mapper;

/**
 * Contains implementation for mamaging user product orders
 * 
 * @author Edmond Gjermeni
 *
 */
@Component
public class ProductOrderManagerImpl implements ProductOrderManager {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private ProductDao productDao;

	@Override
	public GetProductsResponse getProducts() {
		GetProductsResponse response = new GetProductsResponse();
		response.setProducts(Mapper.map(productDao.getAll()));
		return response;
	}

	@Override
	public GetOrdersResponse getOrders(GetOrdersRequest request) {
		GetOrdersResponse response = new GetOrdersResponse();
		if (request != null) {
			response.setOrders(Mapper.mapTo(orderDao.getOrdersByUserId(request.getUserId())));
		}
		return response;
	}

	@Override
	public PlaceOrderResponse palaceOrder(OrderDetails orderDetails){
		Product product = productDao.get(orderDetails.getProductId());
		if (OrderType.BUY.name().equalsIgnoreCase(orderDetails.getType())){
			if(!orderDetails.getPrice().equals(product.getCurrentPrice())) {
				throw new OmsException(ErrorMapper.INVALID_BUY_PRICE);
			}
		}
		Order order = orderDao.store(Mapper.map(orderDetails));
		orderDetails.setId(order.getId());
		PlaceOrderResponse response = new PlaceOrderResponse();
		response.setOrderDetails(orderDetails);
		return response;
	}

}
