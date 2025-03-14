package com.shopme.restcontroller;

import com.shopme.dto.ProductDto;
import com.shopme.entity.Product;
import com.shopme.exception.ProductNotFoundException;
import com.shopme.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    @Autowired
    private ProductService service;

    @PostMapping("/products/check_unique")
    public String checkUnique(Integer id, String name){
        return service.checkUnique(id, name);
    }

    @GetMapping("/products/get/{id}")
    public ProductDto getProductInfo(@PathVariable("id") Integer id) throws ProductNotFoundException {
        Product product = service.get(id);
        return new ProductDto(product.getName(), product.getMainImagePath(),
                product.getDiscountPrice(), product.getCost());
    }
}
