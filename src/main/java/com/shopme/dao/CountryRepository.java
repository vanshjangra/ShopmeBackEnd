package com.shopme.dao;

import com.shopme.entity.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country,Integer> {
    public List<Country> findAllByOrderByNameAsc();
}
