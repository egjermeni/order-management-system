package com.ubs.oms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ubs.oms.domain.Product;

/**
 * The JPA implementation for persisting Product
 * 
 * @author Edmond Gjermeni
 *
 */
@Repository
public class ProductDaoJpaImpl extends BaseDaoJpaImpl<Integer, Product> implements ProductDao {

	@Override
	public Product get(Integer id) {
		return super.get(Product.class, id);
	}

	@Override
	public List<Product> getAll() {
		return super.getAll(Product.class);
	}

}
