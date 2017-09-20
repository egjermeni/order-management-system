package com.ubs.oms.dao;

import com.ubs.oms.domain.User;

/**
 * A set of methods for performing CRUD operation on User
 * 
 * @author Edmond Gjermeni
 *
 */
public interface UserDao extends BaseDao<Integer, User> {

	public User get(Integer id);

	public User getUserByUsername(String username);

	public User getUserByUsernameAndPassword(String username, String password);

}
