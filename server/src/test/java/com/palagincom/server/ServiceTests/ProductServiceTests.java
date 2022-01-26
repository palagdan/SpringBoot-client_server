package com.palagincom.server.ServiceTests;

import com.palagincom.server.Controller.ProductController;
import com.palagincom.server.business.ProductService;
import com.palagincom.server.dao.ProductJpaRepository;
import com.palagincom.server.domain.Category;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Product;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductServiceTests {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductJpaRepository productJpaRepository;

    @Test
    public void testReadAll() {
        Product product = new Product("Lenovo", Category.Computer, null, 29999.00, 0.00);
        Product product1 = new Product("Lenovo1", Category.Computer, null, 29999.00, 0.00);
        List<Product> products = List.of(product, product1);

        Mockito.when(productJpaRepository.findAll()).thenReturn(products);

        Collection<Product> productsCollection = productService.readAll();

        assertEquals(2,productsCollection.size());
        verify(productJpaRepository, times(1)).findAll();
    }


    @Test
    public void testCreate() throws Exception {
        Product product = new Product("Lenovo", Category.Computer, null, 29999.00, 0.00);

        productService.create(product);

        verify(productJpaRepository,times(1)).save(any());
        verify(productJpaRepository, times(1)).save(any(Product.class));
        verify(productJpaRepository, times(1)).save(product);
    }

    @Test
    public void findById(){
        Product product = new Product();
        product.setId_product(1);
        product.setCategory(Category.Computer);
        product.setName("Mac");
        product.setPrice(29999.00);
        product.setDiscount(0.00);

        Mockito.when(productJpaRepository.findById(1)).thenReturn(Optional.of(product));
        Mockito.when(productJpaRepository.findById(not(eq(1)))).thenReturn(Optional.empty());

        Product productFound = productService.findById(1).get();
        assertEquals(productFound,product);
    }
    @Test
    public void findByName(){
        Product product = new Product();
        product.setId_product(1);
        product.setCategory(Category.Computer);
        product.setName("Mac");
        product.setPrice(29999.00);
        product.setDiscount(0.00);

        Mockito.when(productJpaRepository.findByName("Mac")).thenReturn(Optional.of(product));
        Mockito.when(productJpaRepository.findByName(not(eq("Mac")))).thenReturn(Optional.empty());

        Product productFound = productService.findByName("Mac").get();
        assertEquals(productFound,product);
    }

    @Test
    public void findByCategory(){
        Product product = new Product();
        product.setId_product(1);
        product.setCategory(Category.Computer);
        product.setName("Mac");
        product.setPrice(29999.00);
        product.setDiscount(0.00);

        Mockito.when(productJpaRepository.findByCategory(Category.Computer)).thenReturn(List.of(product));
        Mockito.when(productJpaRepository.findByCategory(not(eq(Category.Computer)))).thenReturn(Collections.emptyList());

        List<Product> products = List.of(product);

        List<Product> productFound = productService.findByCategory(Category.Computer);
        assertEquals(productFound,products);
    }

    @Test
    public void findByPrice(){
        Product product = new Product();
        product.setId_product(1);
        product.setCategory(Category.Computer);
        product.setName("Mac");
        product.setPrice(29999.00);
        product.setDiscount(0.00);

        Mockito.when(productJpaRepository.findByPrice(29999.00)).thenReturn(List.of(product));
        Mockito.when(productJpaRepository.findByPrice(not(eq(29999.00)))).thenReturn(Collections.emptyList());

        List<Product> products = List.of(product);

       List<Product> productFound = productService.findByPrice(29999.00);
        assertEquals(productFound,products);
    }

    @Test
    public void findByDiscount(){
        Product product = new Product();
        product.setId_product(1);
        product.setCategory(Category.Computer);
        product.setName("Mac");
        product.setPrice(29999.00);
        product.setDiscount(2500.00);

        Mockito.when(productJpaRepository.findByDiscount(2500.00)).thenReturn(List.of(product));
        Mockito.when(productJpaRepository.findByDiscount(not(eq(2500.00)))).thenReturn(Collections.emptyList());

        List<Product> products = List.of(product);

        List<Product> productFound = productService.findByDiscount(2500.00);
        assertEquals(productFound,products);
    }
    @Test
    public void deleteTest() throws Exception {
        Product product = new Product();
        product.setId_product(1);
        product.setCategory(Category.Computer);
        product.setName("Mac");
        product.setPrice(29999.00);
        product.setDiscount(2500.00);

        Mockito.when(productJpaRepository.findById(1)).thenReturn(Optional.of(product));
        Mockito.when(productJpaRepository.findById(not(eq(1)))).thenReturn(Optional.of(product));

        Mockito.doNothing().when(productJpaRepository).delete(product);
        productService.delete(product.getId_product());


         Mockito.verify(productJpaRepository, Mockito.times(1)).findById(1);
         Mockito.verify(productJpaRepository, Mockito.times(1)).delete(product);
    }
    @Test
    public void updateTest() throws Exception {
        Product product = new Product();
        product.setId_product(1);
        product.setCategory(Category.Computer);
        product.setName("Mac");
        product.setPrice(29999.00);
        product.setDiscount(2500.00);

        Mockito.when(productJpaRepository.findById(1)).thenReturn(Optional.of(product));
        Mockito.when(productJpaRepository.findById(not(eq(1)))).thenReturn(Optional.of(product));

        productService.update(1,product);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productJpaRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Product productProvided = argumentCaptor.getValue();
        assertEquals(product,productProvided);

    }




}
