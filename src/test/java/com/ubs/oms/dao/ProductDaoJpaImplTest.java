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

import com.ubs.oms.domain.Product;

import junit.framework.Assert;

/**
 * Simple Integration test for testing ProductDao
 *
 * @author Edmond Gjermeni
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-dao.xml" })
@TransactionConfiguration
public class ProductDaoJpaImplTest {

	@Autowired
	private ProductDao underTest;

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGetAllProducts() throws Exception {
		// given

		// when
		List<Product> actual = underTest.getAll();

		// then
		Assert.assertNotNull(actual);
		Assert.assertFalse(actual.isEmpty());
	}

	@Test
	public void testGetProduct() throws Exception {

		Product actual = underTest.get(1);

		// then
		Assert.assertNotNull(actual);
	}

	@Test
	public void testSaveProduct() throws Exception {
		// given
		Product product = new Product();
		product.setName("product 6");
		product.setCurrentPrice(75.35);
		Assert.assertNull(product.getId());

		// when
		Product actual = underTest.store(product);

		// then
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
	}

	@Test
	public void testUpdateProduct() throws Exception {
		// given
		Product product = new Product();
		product.setId(1);
		product.setName("product 1");
		product.setCurrentPrice(63.50);

		// when
		underTest.store(product);

		// then
		Product actual = underTest.get(1);
		Assert.assertNotNull(actual);
		Assert.assertEquals(63.50, actual.getCurrentPrice());
	}

}
