package com.drw.mongoex.service;

import com.drw.mongoex.model.Product;
import com.drw.mongoex.repo.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Testcontainers
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Container
    static
    MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry){
            registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void Test_initial_record_count(){
        assertEquals(6, productRepository.findAll().size());
    }

    @Test
    void Test_partial_update_product(){
        productRepository.partialUpdate("1", "brand", "Phillips");
        assertEquals("Phillips", productRepository.findById("1").orElseGet(Product::new).getBrand());
    }

    @Test
    void Test_get_all_products_by_page()  {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Product>> mapType = new TypeReference<List<Product>>() {};

        Object obj = productRepository.getAllProductsByPage(0, 2, new String[]{"modelNumber", "brand"}, "ASC").get("items");

        List<Object> objects2 =(List<Object>) obj;
        assertEquals(2, objects2.size());
    }

    @Test
    void Test_products_by_category(){
        assertEquals(2, productRepository.getProductsByCategory("kitchenware").size());
    }

    @Test
    void Test_delete_by_brand_and_model_number(){
        productRepository.deleteByBrandAndModelNumber("Sony", "106");
        assertEquals(5, productRepository.findAll().size());
    }
}
