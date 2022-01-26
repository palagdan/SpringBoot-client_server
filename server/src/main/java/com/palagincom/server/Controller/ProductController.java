package com.palagincom.server.Controller;

import com.palagincom.server.domain.Category;
import com.palagincom.server.domain.Product;
import com.palagincom.server.business.ProductService;
import com.palagincom.server.exceptions.ProductDoesNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/v1")
public class ProductController {


    private final ProductService productService;


    @Autowired
    ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> addProduct(@RequestBody Product product)throws Exception{
       try{
           Product product1 = productService.create(product);
         return ResponseEntity.status(HttpStatus.CREATED).body(product1);
       }catch(InstanceAlreadyExistsException e){
           throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/product")
    List<Product> findAllProducts(){
        return productService.readAll();
    }

    @GetMapping("/product/{id}")
    public Product productFindById(@PathVariable int id){
        Optional<Product> product = productService.findById(id);
        return product.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/product/{id}")
    public void deleteById( @PathVariable int id )throws Exception{
        try{
            productService.delete(id);
        }catch(ProductDoesNotExist e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/product/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct( @PathVariable int id,@RequestBody Product product)throws Exception {
        try {
             productService.update(id,product);
        } catch (ProductDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/product/name/{name}")
    public Product productFindByName(@PathVariable String name){
            Optional<Product> product = productService.findByName(name);
            return product.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
    @GetMapping("/product/price/{price}")
       public List<Product> productFindByPrice(@PathVariable Double price){
           List<Product> product = productService.findByPrice(price);
        if(product.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return product;
    }
    @GetMapping("/product/category/{category}")
    public List<Product> productFindByCategory(@PathVariable Category category){
        List<Product> products = productService.findByCategory(category);
        if(products.isEmpty())throw new ResponseStatusException((HttpStatus.NOT_FOUND));

        return products;
    }

    @GetMapping("/product/discount/{discount}")
    public List<Product> productFindByDiscount(@PathVariable double discount){
        List<Product> products = productService.findByDiscount(discount);
        if(products.isEmpty())throw new ResponseStatusException((HttpStatus.NOT_FOUND));

        return products;
    }





}
