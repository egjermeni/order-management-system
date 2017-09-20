package com.ubs.oms.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ubs.oms.dao.UserDao;
import com.ubs.oms.domain.User;
import com.ubs.oms.dto.LoginRequest;
import com.ubs.oms.dto.LoginResponse;
import com.ubs.oms.dto.RegisterUserRequest;
import com.ubs.oms.dto.RegisterUserResponse;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class AccountManagerImplTest {

	@InjectMocks
	private AccountManagerImpl underTest;

	@Mock
	private UserDao userDao;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRegister() throws  Exception{
		// given
		RegisterUserRequest request = createRegisterUserRequest();
		User user = createUser();
		Mockito.when(userDao.store(Mockito.any(User.class))).thenReturn(user);

		// when
		RegisterUserResponse actual = underTest.register(request);
		
		// then
		Assert.assertEquals(Integer.valueOf(1), actual.getUserId());
	}

	@Test
	public void testLogin() {
		// given
		LoginRequest request = createLoginRequest();
		User user = createUser();
		Map<String, User> sessionMap = new HashMap<String, User>();
		underTest.setSessionMap(sessionMap);
		Mockito.when(userDao.getUserByUsernameAndPassword("user1", "pass1")).thenReturn(user);

		// when
		LoginResponse actual = underTest.login(request);
		
		// then
		Assert.assertEquals("on4MAl9lBjm/sQjyDX0LK4bLWhRrTtydu6MDnDLblE4=", actual.getToken());
	}

	@Test
	public void testCheckUserLoggedin() throws  Exception{
		// given
		LoginRequest request = createLoginRequest();
		User user = createUser();
		Map<String, User> sessionMap = new HashMap<String, User>();
		String token = "on4MAl9lBjm/sQjyDX0LK4bLWhRrTtydu6MDnDLblE4=";
		sessionMap.put(token,  user);
		underTest.setSessionMap(sessionMap);

		// when
		LoginResponse  actual  = underTest.login(request);
		
		// then
		Assert.assertEquals("on4MAl9lBjm/sQjyDX0LK4bLWhRrTtydu6MDnDLblE4=", actual.getToken());
	}
	
	@Test
	public void testGetToken_ShouldReturnSameToken() throws  Exception{
		// given
		LoginRequest request = new LoginRequest();
		request.setUsername("user1");
		request.setPassword("pass1");
		request.setCompany("company1");
		// when
		for (int i = 0; i < 100000; i++) {
			String actual = underTest.getToken(request);
			// then
			Assert.assertEquals("on4MAl9lBjm/sQjyDX0LK4bLWhRrTtydu6MDnDLblE4=", actual);
		}
		
	}
	
	private User createUser(){
		User user = new User();
		user.setId(1);
		user.setFirstName("user1");
		user.setPassword("pass1");
		user.setCompany("company1");
		user.setFirstName("first name 1");
		user.setLastName("last name 1");
		return user;
	}

	private LoginRequest createLoginRequest(){
		LoginRequest request = new LoginRequest();
		request.setUsername("user1");
		request.setPassword("pass1");
		request.setCompany("company1");
		return request;
	}

	private RegisterUserRequest createRegisterUserRequest(){
		RegisterUserRequest request = new RegisterUserRequest();
		request.setUsername("user1");
		request.setPassword("pass1");
		request.setCompany("company1");
		request.setFirstName("first name 1");
		request.setLastName("last name 1");
		return request;
	}

}
