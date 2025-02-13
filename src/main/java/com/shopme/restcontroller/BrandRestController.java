package com.shopme.restcontroller;

import com.shopme.dto.CategoryDto;
import com.shopme.entity.Brand;
import com.shopme.entity.Category;
import com.shopme.exception.BrandNotFoundException;
import com.shopme.exception.BrandNotFoundRestException;
import com.shopme.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {
    @Autowired
    private BrandService service;

    @PostMapping("/brands/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name){
        return service.checkUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDto> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException{
        List<CategoryDto> listCategories = new ArrayList<>();

        try {
            Brand brand = service.get(brandId);
            Set<Category> categories = brand.getCategories();

            for (Category category : categories){
                CategoryDto dto = new CategoryDto(category.getId(), category.getName());
                listCategories.add(dto);
            }

            return listCategories;
        }
        catch (BrandNotFoundException e){
            throw new BrandNotFoundRestException();
        }
    }
}
