package com.ubs.oms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ubs.oms.domain.Order;
import com.ubs.oms.exception.OmsException;

/**
 * The JPA implementation for persisting Order
 * 
 * @author Edmond Gjermeni
 *
 */
@Repository
public class OrderDaoJpaImpl extends BaseDaoJpaImpl<Integer, Order> implements OrderDao {

	@Override
	public Order get(Integer id) {
		return super.get(Order.class, id);
	}

	@Override
	public List<Order> getOrdersByUserId(Integer userId) {
		try {
			String query = "select t from Order t  where t.userId = :userId";
			return entityManager.createQuery(query, Order.class).setParameter("userId", userId).getResultList();
		} catch (Exception e) {
			throw new OmsException(e.getMessage());
		}
	}

}
