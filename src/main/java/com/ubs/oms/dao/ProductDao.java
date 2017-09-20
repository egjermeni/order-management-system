package com.ubs.oms.dao;

import java.util.List;

import com.ubs.oms.domain.Product;

/**
 * A set of methods for performing CRUD operation on Product
 * 
 * @author Edmond Gjermeni
 *
 */
public interface ProductDao extends BaseDao<Integer, Product> {

	public Product get(Integer id);

	public List<Product> getAll();

}
