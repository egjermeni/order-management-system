package com.ubs.oms.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ubs.oms.dao.UserDao;
import com.ubs.oms.domain.User;
import com.ubs.oms.dto.LoginRequest;
import com.ubs.oms.dto.LoginResponse;
import com.ubs.oms.dto.RegisterUserRequest;
import com.ubs.oms.dto.RegisterUserResponse;
import com.ubs.oms.exception.OmsException;
import com.ubs.oms.mapper.ErrorMapper;
import com.ubs.oms.mapper.Mapper;

/**
 * Contains implementation for mamaging user account
 * 
 * @author Edmond Gjermeni
 *
 */
@Component
public class AccountManagerImpl implements AccountManager {

	/**
	 * This map conatins users who are logged in. 
	 * Provides a simple security mechanizm for single sign on (SSO)
	 * A better way is by using spring security, a distibured cache and idenity server.
	 */
	private volatile Map<String, User> sessionMap;

	@Autowired
	private UserDao userDao;
	
	@PostConstruct
	public void initSessionMap() {
		// make sure thread safaty when users try to login concurrently
		sessionMap = new ConcurrentHashMap<>();
	}

	@Override
	public RegisterUserResponse register(RegisterUserRequest request) {
		try {
			User user = userDao.store(Mapper.map(request));
			RegisterUserResponse response = Mapper.map(user);
			return response;
		} catch (Exception e) {
			if(e.getMessage().contains("ConstraintViolationException")){
				throw new OmsException(ErrorMapper.DUPLICATE_USER_ERROR);
			}
			throw new OmsException(ErrorMapper.GENERAL_ERROR);
		}
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		LoginResponse response = null;
		String token = getToken(request);
		if (sessionMap.containsKey(token)) {
			// user is already loged in, so we return same token
			response = new LoginResponse();
			response.setToken(token);
		} else {
			// check in db if user is registered
			User user = userDao.getUserByUsernameAndPassword(request.getUsername(), request.getPassword());
			if (user == null) {
				throw new OmsException(ErrorMapper.INVALID_LOGIN);
			} else {
				sessionMap.put(token, user);
				response = new LoginResponse();
				response.setToken(token);
			}
		}
		return response;
	}

	@Override
	public User checkUserLoggedin(String token) {
		User user = sessionMap.get(token);
		if (user == null) {
			throw new OmsException(ErrorMapper.ACCESS_DENIED);
		}
		return  user;
	}

	protected String getToken(LoginRequest request) {
		String token = null;
		if (request != null && request.getUsername() != null && request.getPassword() != null && request.getCompany() != null) {
			try {
				String data = request.getUsername() + "." + request.getPassword() + "." + request.getCompany();
			    String key = "&txbsaoqyr52";
			    Mac hasher = Mac.getInstance("HmacSHA256");
			    hasher.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
			    byte[] hash = hasher.doFinal(data.getBytes());
			    //token = DatatypeConverter.printHexBinary(hash);
			    token = DatatypeConverter.printBase64Binary(hash);
			} catch (Exception e) {
				throw new OmsException(e.getMessage());
			}
		}
		return token;
	}
	
	public  void setSessionMap(Map<String, User> sessionMap) {
		this.sessionMap = sessionMap;
	}
}
