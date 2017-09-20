package com.ubs.oms.dao;

import java.util.List;

/**
 * A generic interaface containig a set of methods for performing CRUD
 * operation.
 * 
 * @author Edmond Gjermeni
 *
 */
public interface BaseDao<K, V> {

	public V get(Class<V> clazz, K id);

	public V store(V v);

	public List<V> getAll(Class<V> clazz);

}
