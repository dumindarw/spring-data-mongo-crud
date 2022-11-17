package com.drw.mongoex.repo;

import com.drw.mongoex.model.Product;

import java.util.List;
import java.util.Map;

public interface CustomProductRepository {

    public Product partialUpdate(String productId, String fieldName, Object fieldValue);
    public Map<String, Object> getAllProductsByPage(int pageNo, int pageSize, String[] fields, String sortBy);

    public Map<String, Object> productCount();

    public List<Product> getProductsByCategory(String category);
}
