package com.ubs.oms.service;

import com.ubs.oms.domain.User;
import com.ubs.oms.dto.LoginRequest;
import com.ubs.oms.dto.LoginResponse;
import com.ubs.oms.dto.RegisterUserRequest;
import com.ubs.oms.dto.RegisterUserResponse;

/**
 * A set for methods for managing user account
 * 
 * @author Edmond Gjermeni
 *
 */
public interface AccountManager {

	public RegisterUserResponse register(RegisterUserRequest request);

	public LoginResponse login(LoginRequest request);

	public User checkUserLoggedin(String token);

}
