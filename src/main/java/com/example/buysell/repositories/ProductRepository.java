package com.example.buysell.repositories;

import com.example.buysell.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCity(String city);
    List<Product> findByTitleContainingIgnoreCase(String title);

//    @Query(value = "SELECT e FROM Product as e WHERE :title is null or e.title like %:title%  and " +
//            ":city is null or e.city like %:city%")
//    List<Product> findByTitleAndCityContainingIgnoreCase(String title, String city);

}
