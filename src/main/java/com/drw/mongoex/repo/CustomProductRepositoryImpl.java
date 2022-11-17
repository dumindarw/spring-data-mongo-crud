package com.drw.mongoex.repo;

import com.drw.mongoex.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Map;

public class CustomProductRepositoryImpl implements CustomProductRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Product partialUpdate(String productId, String fieldName, Object fieldValue) {
        return mongoTemplate.findAndModify(
                BasicQuery.query(Criteria.where("id").is(productId)),
                BasicUpdate.update(fieldName, fieldValue),
                FindAndModifyOptions.options().returnNew(true), Product.class
        );
    }

    @Override
    public Map<String, Object> getAllProductsByPage(int pageNo, int pageSize, String[] fields, String sortBy) {
        Query query = new Query();
        for (var field: fields){
            query.fields().include(field);
        }

        Sort sort = Sort.by(sortBy);
        Pageable pageable  = PageRequest.of(pageNo, pageSize);
        query.with(pageable);

        List<Product> products = mongoTemplate.find(query, Product.class);
        Page<Product> productsPage = PageableExecutionUtils.getPage(products, pageable, () -> mongoTemplate.count(new Query(), Product.class));
        return Map.of("items", productsPage.getContent(), "totalPages", productsPage.getTotalPages(), "currentPage", pageNo);
    }

    @Override
    public Map<String, Object> productCount() {
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group().count().as("count"));
        return mongoTemplate.aggregate(aggregation, Product.class, Product.class).getRawResults();
    }

    @Override
    public List<Product> getProductsByCategory(String category){
        Query query = Query.query(Criteria.where("category").in(category));
        query.fields().exclude("id","category");
        return mongoTemplate.find(query, Product.class);
    }
}
