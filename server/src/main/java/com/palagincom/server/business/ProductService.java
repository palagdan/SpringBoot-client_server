package com.palagincom.server.business;

import com.palagincom.server.domain.Category;
import com.palagincom.server.domain.Product;
import com.palagincom.server.dao.ProductJpaRepository;
import com.palagincom.server.exceptions.ProductDoesNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {



    private final ProductJpaRepository productJpaRepository;

    @Autowired
    public ProductService(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    public List<Product> readAll() {
        return productJpaRepository.findAll();
    }

    public Optional<Product> findById(int id) {
        return productJpaRepository.findById(id);
    }

    public Optional<Product> findByName(String name) {
        return productJpaRepository.findByName(name);
    }

    public List<Product> findByCategory(Category category) {
        return productJpaRepository.findByCategory(category);
    }

    public List<Product> findByPrice(double price) {
        return productJpaRepository.findByPrice(price);
    }

    public List<Product> findByDiscount(double discount){return productJpaRepository.findByDiscount(discount);}

    @Transactional
    public Product create(Product product) throws Exception {
        if (productJpaRepository.findByName(product.getName()).isPresent())
            throw new InstanceAlreadyExistsException("Product,that you want to add " + product.getName() + " already exists");
        return productJpaRepository.save(product);
    }
    @Transactional
    public void  update(int id,Product product)throws Exception{
        Optional <Product> find = productJpaRepository.findById(id);

        if(find.isEmpty()) throw new ProductDoesNotExist("Product with id " + product.getId_product() + " not found");

        productJpaRepository.save(product);

    }



    @Transactional
    public void delete(int id) throws Exception{
        Optional<Product> product = findById(id);
        if(product.isEmpty())
            throw new ProductDoesNotExist("Product with id " + id + " not found.");
        productJpaRepository.delete(product.get());
    }


}