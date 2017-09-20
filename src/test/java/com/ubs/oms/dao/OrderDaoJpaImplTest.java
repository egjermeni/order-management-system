package com.ubs.oms.dao;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ubs.oms.domain.Order;
import com.ubs.oms.domain.OrderType;

import junit.framework.Assert;

/**
 * Simple Integration test for testing ProductDao
 *
 * @author Edmond Gjermeni
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-dao.xml" })
@TransactionConfiguration
public class OrderDaoJpaImplTest {

	@Autowired
	private OrderDao underTest;

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGetOrderById() throws Exception {
		// given

		// when
		Order actual = underTest.get(1);

		// then
		Assert.assertNotNull(actual);
	}

	@Test
	public void testGetOrderByUserId() throws Exception {
		// given

		// when
		List<Order> actual = underTest.getOrdersByUserId(1);

		// then
		Assert.assertNotNull(actual);
	}

	
	@Test
	public void testSaveOrder() throws Exception {
		// given
		Order order = new Order();
		order.setPrice(44.20);
		order.setUserId(3);
		order.setProductId(1);
		order.setQuantity(2);
		order.setType(OrderType.BUY.name());

		// when
		Order actual = underTest.store(order);

		// then
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
	}

	@Test
	public void testUpdateOrder() throws Exception {
		// given
		Order order = new Order();
		order.setId(1);
		order.setPrice(44.20);
		order.setUserId(3);
		order.setProductId(1);
		order.setQuantity(2);
		order.setType(OrderType.SELL.name());

		// when
		Order actual = underTest.store(order);

		// then
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
		Assert.assertEquals("SELL", actual.getType());
	}


}
