package com.ubs.oms.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ubs.oms.domain.User;

import junit.framework.Assert;

/**
 * Simple Integration test for testing ProductDao
 *
 * @author Edmond Gjermeni
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-dao.xml" })
@TransactionConfiguration
public class UserDaoJpaImplTest {

	@Autowired
	private UserDao underTest;

	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() {
		//underTest.get
	}

	@Test
	public void testGetUserById() throws Exception {
		// given

		// when
		User actual = underTest.get(1);

		// then
		Assert.assertNotNull(actual);
	}

	@Test
	public void testGetUserByUsername() throws Exception {
		// given

		// when
		User actual = underTest.getUserByUsername("user1");

		// then
		Assert.assertNotNull(actual);
	}

	@Test
	public void testSaveUser() throws Exception {
		// given
		User user = new User();
		user.setUsername("user3");
		user.setPassword("pass3");
		user.setCompany("company 3");
		user.setFirstName("first name 3");
		user.setLastName("last name 3");

		// when
		User actual = underTest.store(user);

		// then
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
	}

	@Test
	public void testUpdateUser() throws Exception {
		// given
		User user = new User();
		user.setId(1);
		user.setUsername("user1");
		user.setPassword("pass1");
		user.setCompany("company 12");
		user.setFirstName("first name 1");
		user.setLastName("last name 1");
		user.setLastName("last name 1");
		
		// when
		underTest.store(user);
		User actual = underTest.get(1);

		// then
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
		Assert.assertEquals("company 12", actual.getCompany());
	}

}
