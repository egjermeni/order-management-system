package com.ubs.oms.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ubs.oms.exception.OmsException;

/**
 * The JPA implementation for persisting objects
 * 
 * @author Edmond Gjermeni
 *
 */
@Repository
public class BaseDaoJpaImpl<K, V> implements BaseDao<K, V> {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public V get(Class<V> clazz, K id) {
		try {
			return entityManager.find(clazz, id);
		} catch (Exception e) {
			throw new OmsException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public V store(V v) {
		try {
			return entityManager.merge(v);
		} catch (Exception e) {
			throw new OmsException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<V> getAll(Class<V> clazz) {
		try {
			String query = "select t from " + clazz.getSimpleName() + " t";
			return entityManager.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new OmsException(e.getMessage());
		}
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
