package com.palagincom.server.dao;

import com.palagincom.server.domain.Category;
import com.palagincom.server.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);
    List<Product> findByCategory(Category category);
    List<Product> findByPrice(double price) ;
    int deleteByName(String name);

    List<Product> findByDiscount(double discount);
}
