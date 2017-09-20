package com.ubs.oms.dao;

import java.util.List;

import com.ubs.oms.domain.Order;

/**
 * A set of methods for performing CRUD operation on Order
 * 
 * @author Edmond Gjermeni
 *
 */
public interface OrderDao extends BaseDao<Integer, Order> {

	public Order get(Integer id);

	public List<Order> getOrdersByUserId(Integer userId) ;

}
