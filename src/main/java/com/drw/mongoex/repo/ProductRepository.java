package com.drw.mongoex.repo;

import com.drw.mongoex.model.Product;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String> , CustomProductRepository {

    @DeleteQuery(value = "{ 'brand':  ?0 , 'modelNumber':  ?1 }")
    public void deleteByBrandAndModelNumber(String brand, String modelNumber);
}
