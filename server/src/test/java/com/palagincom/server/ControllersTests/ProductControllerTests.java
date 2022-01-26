package com.palagincom.server.ControllersTests;


import com.palagincom.server.Controller.CustomerController;
import com.palagincom.server.Controller.ProductController;
import com.palagincom.server.business.CustomerService;
import com.palagincom.server.business.ProductService;
import com.palagincom.server.domain.Category;
import com.palagincom.server.domain.Customer;
import com.palagincom.server.domain.Product;
import com.palagincom.server.exceptions.CustomerNotFound;
import com.palagincom.server.exceptions.ProductDoesNotExist;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.management.InstanceAlreadyExistsException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @MockBean
    ProductService productService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateExisting() throws Exception {

        doThrow(new InstanceAlreadyExistsException()).when(productService).create(any(Product.class));

        Product product = new Product("Lenovo", Category.Computer, null, 29999.00, 0.00);
        mockMvc.perform(post("/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Lenovo\" , \"category\": \"Computer\" , \"orders\" : null , \"price\" : 29999.00, \"discount\" : 0.00}"
                                ))
                .andExpect(status().isConflict());
    }
    @Test
    public void testFindByName()throws Exception{
        Product product = new Product("Lenovo", Category.Computer,null,  29999.00, 0.00);

        Mockito.when(productService.findByName("Lenovo")).thenReturn(Optional.of(product));


        //for existing name
        mockMvc.perform(get("/v1/product/name/Lenovo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Lenovo")));


        //For not existing name
        Mockito.when(productService.findByName(not(eq("Lenovo")))).thenReturn(Optional.empty());
        mockMvc.perform(get("/v1/product/name/notlenovo"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByPrice()throws Exception{

        Product product = new Product("Lenovo", Category.Computer,null,  29999.00, 0.00);
        Product product1 = new Product("MacBook", Category.Computer,null, 29999.00, 0.00);

        List<Product> products = List.of(product,product1);

        Mockito.when(productService.findByPrice(29999.00)).thenReturn(products);

        //for existing price
        mockMvc.perform(get("/v1/product/price/29999.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].price", Matchers.is(29999.00)))
                .andExpect(jsonPath("$[1].price", Matchers.is(29999.00)));


        //For not existing price
        Mockito.when(productService.findByPrice(not(eq(29999.00)))).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/v1/product/price/2999.00"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateNotExisting() throws Exception {
        Mockito.doThrow(new ProductDoesNotExist()).when(productService).update(eq(3), any(Product.class));
        mockMvc.perform(put("/v1/product/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Lenovo\" , \"category\": \"Computer\" , \"orders\" : null , \"price\" : 29999.00, \"discount\" : 0.00}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readAllTests() throws Exception {

        Product product = new Product("Lenovo", Category.Computer, null, 29999.00, 0.00);
        Product product1 = new Product("MacBook", Category.Computer,null, 29999.00, 0.00);
        Product product2 = new Product("MacBookPro", Category.Computer,null,  29999.00, 0.00);
        List<Product> productList = List.of(product, product1, product2);
        Mockito.when(productService.readAll()).thenReturn(productList);

        mockMvc.perform(get("/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Lenovo")))
                .andExpect(jsonPath("$[1].name", Matchers.is("MacBook")))
                .andExpect(jsonPath("$[2].name", Matchers.is("MacBookPro")));
    }

    @Test
    public void readOne() throws Exception {
        Product product = new Product("Lenovo", Category.Computer,null, 29999.00, 0.00);

        //For existing id
        Mockito.when(productService.findById(1)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Lenovo")));

        //For not existing id
        Mockito.when(productService.findByName(not(eq("Lenovo")))).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/product/2"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testDelete() throws Exception {

        Product product = new Product("Lenovo", Category.Computer, null, 29999.00, 0.00);


        Mockito.when(productService.findById(not(eq(1)))).thenReturn(Optional.empty());
        Mockito.when(productService.findById(1)).thenReturn(Optional.of(product));


        mockMvc.perform(get("/v1/product/2"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/v1/product/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void createTest() throws Exception {
        Product product = new Product("Mc1", Category.Computer, null, 29999.00, 0.00);

        Mockito.when(productService.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Mc1\" " +
                                ",\"category\": \"Computer\" "+
                                ",\"price\": 29999.00" +
                                ",\"discount\": 0.00}"))
                .andExpect(status().isCreated())
                .andReturn();

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productService, Mockito.times(1)).create(argumentCaptor.capture());
        Product productProvidedToService = argumentCaptor.getValue();
        assertEquals("Mc1", productProvidedToService.getName());
        assertEquals(Category.Computer,productProvidedToService.getCategory());
        assertEquals(29999.00, productProvidedToService.getPrice());
        assertEquals(0.00, productProvidedToService.getDiscount());

    }

    @Test
    public void testUpdate() throws Exception {

        Mockito.doNothing().when(productService).update(eq(2), any(Product.class));

        Product product = new Product();
        product.setId_product(2);
        product.setName("Mac");
        product.setPrice(29999.00);
        product.setCategory(Category.Computer);
        product.setDiscount(0.00);


        mockMvc.perform(put("/v1/product/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Mac\" " +
                                ",\"category\": \"Computer\" "+
                                ",\"price\": 29999.00" +
                                ",\"discount\": 0.00}"))
                .andExpect(status().isNoContent());

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
       Mockito.verify(productService, Mockito.times(1)).update(eq(2),(argumentCaptor.capture()));
        Product productProvidedToService = argumentCaptor.getValue();
        assertEquals("Mac", productProvidedToService.getName());
        assertEquals(Category.Computer,productProvidedToService.getCategory());
        assertEquals(29999.00, productProvidedToService.getPrice());
        assertEquals(0.00, productProvidedToService.getDiscount());

    }





}
