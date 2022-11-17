package com.drw.mongoex.controller;

import com.drw.mongoex.dto.ErrorResponse;
import com.drw.mongoex.model.Product;
import com.drw.mongoex.service.ProductService;
import com.drw.mongoex.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.drw.mongoex.util.ResponseHandler.generateResponse;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<Object> addProducts(@RequestBody List<Product> products){
        return generateResponse("Product Found", HttpStatus.OK, productService.insertProducts(products));
    }

    @GetMapping("/products")
    public ResponseEntity<Object> findProducts(){
        return generateResponse("Product Found", HttpStatus.OK, productService.findProducts());
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Object> findProduct(@PathVariable final String productId){
        Optional<Product> product = productService.findProduct(productId);
        if(product.isPresent())
            return generateResponse("Product Found", HttpStatus.OK, product);
        else
            return generateResponse("Product not Found", HttpStatus.NOT_FOUND, null);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<Object> partialUpdateProduct(@PathVariable final String productId, @RequestBody Product product) throws Exception{

        Optional<Product> p = productService.partialUpdateProduct(productId, product);
        if(p.isPresent())
            return generateResponse("Products Updated", HttpStatus.OK, p);
        else
            return generateResponse("Products not Found to update", HttpStatus.NOT_FOUND, null);
    }

    @GetMapping("/products/page")
    public ResponseEntity<Object> getAllProductsByPage(@RequestParam int pageNo, @RequestParam int pageSize, @RequestParam String[] fields, @RequestParam String sortBy){
        return generateResponse("Products By Page", HttpStatus.OK, productService.getAllProductsByPage(pageNo,  pageSize, fields, sortBy));
    }

    @GetMapping("/products/count")
    public ResponseEntity<Object> productCount(){
        return generateResponse("Products Count", HttpStatus.OK, productService.productCount());
    }

    @GetMapping("/products/categories/{category}")
    public ResponseEntity<Object> productByCategory( @PathVariable final String category){
        return generateResponse("Products by Category", HttpStatus.OK, productService.productByCategory(category));
    }

    @DeleteMapping("/products/brand/{brand}/model/{modelNumber}")
    public void deleteByBrandAndModelNumber(@PathVariable final String brand, @PathVariable final String modelNumber){
        productService.deleteByBrandAndModelNumber(brand, modelNumber);
    }
}
