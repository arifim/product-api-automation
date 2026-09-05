package com.ari.mystore;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.ari.mystore.models.Product;
import com.ari.mystore.services.ProductService;

import io.restassured.response.Response;

public class ProductAPITest {

    ProductService productService = new ProductService();

    @Test
    void createProduct_valid_returns200() {
        Product product = new Product(null, "Banana", 0.59);
        Response response = productService.createProduct(product);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("name")).isEqualTo("Banana");
    }

    @Test
    void createProduct_negativePrice_returns422() {
        Product product = new Product(null, "Bad", -5.0);
        Response response = productService.createProduct(product);

        assertThat(response.statusCode()).isEqualTo(422);   // валидация!
    }
}
