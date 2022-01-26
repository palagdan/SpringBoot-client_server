package com.palagincom.server.ApplicationTest;

import com.palagincom.server.Controller.CustomerController;

import com.palagincom.server.Controller.OrderController;
import com.palagincom.server.Controller.ProductController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RestServerTests {

    @Autowired
    CustomerController customerController;

    @Autowired
    OrderController orderController;

    @Autowired
    ProductController productController;

    @Test
    public void testContextLoadsController() {
        Assertions.assertThat(customerController).isNotNull();
    }

    @Test
    public void testContextLoadsController1() {
        Assertions.assertThat(productController).isNotNull();
    }

    @Test
    public void testContextLoadsController2() {
        Assertions.assertThat(orderController).isNotNull();
    }
}
