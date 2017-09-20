package com.ubs.oms.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.ubs.oms.domain.User;

import javassist.bytecode.stackmap.TypeData.ClassName;

/**
 * The JPA implementation for persisting User
 * 
 * @author Edmond Gjermeni
 *
 */
@Repository
public class UserDaoJpaImpl extends BaseDaoJpaImpl<Integer, User> implements UserDao {

	private static final Logger logger = Logger.getLogger(ClassName.class.getName());

	@Override
	public User get(Integer id) {
		return super.get(User.class, id);
	}

	@Override
	public User getUserByUsername(String username) {
		try {
			String query = "select t from User t where t.username = :username";
			return entityManager.createQuery(query, User.class).setParameter("username", username).getSingleResult();
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
			return null;
		}
	}

	@Override
	public User getUserByUsernameAndPassword(String username, String password) {
		try {
			String query = "select t from User t where t.username = :username and t.password = :password";
			return entityManager.createQuery(query, User.class)
					.setParameter("username", username)
					.setParameter("password", password)
					.getSingleResult();
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
			return null;
		}
	}

}
