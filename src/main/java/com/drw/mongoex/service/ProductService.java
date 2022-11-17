package com.drw.mongoex.service;

import com.drw.mongoex.model.Product;
import com.drw.mongoex.repo.ProductDAO;
import com.drw.mongoex.repo.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDAO productDAO;

    public List<Product> insertProducts(List<Product> products) {
        return productRepository.insert(products);
    }

    public List<Product> findProducts() {
        return productDAO.findAll();
    }

    public Optional<Product> findProduct(String productId) {
        return Optional.ofNullable(productDAO.findById(productId));
    }

    public Optional<Product> partialUpdateProduct(String productId, Product product) throws Exception {
        
        Product p = null;
        
        for (Field field : Product.class.getDeclaredFields()) {
            final String fieldName = field.getName();
            log.info("fieldName : " + fieldName);
            if (fieldName.equals("id")) {
                continue;
            }

            final Method getter = Product.class.getDeclaredMethod("get" + StringUtils.capitalize(fieldName));
            final Object fieldValue = getter.invoke(product);

            log.info("getter : "+ getter.getName() + " ,fieldValue : " + fieldValue);

            if (Objects.nonNull(fieldValue)) {
                p = productRepository.partialUpdate(productId, fieldName, fieldValue);
            }
        }

        return Optional.ofNullable(p);

    }

    public Map<String, Object> getAllProductsByPage(int pageNo, int pageSize, String[] fields, String SortBy) {
        return productRepository.getAllProductsByPage(pageNo, pageSize, fields, SortBy);
    }

    public Map<String, Object> productCount() {
        return productRepository.productCount();
    }

    public List<Product> productByCategory(String category){
        return productRepository.getProductsByCategory(category);
    }

    public void deleteByBrandAndModelNumber(String brand, String modelNumber){
          productRepository.deleteByBrandAndModelNumber(brand, modelNumber);
    }
}


