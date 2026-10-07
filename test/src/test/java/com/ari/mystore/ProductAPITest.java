package com.ari.mystore;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import com.ari.mystore.services.HealthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ari.mystore.models.Product;
import com.ari.mystore.services.ProductService;

import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ProductAPITest {

    ProductService productService = new ProductService();
    HealthService healthService = new HealthService();

    @Test
    void createProduct_valid_returns200() {
        var productName = "Banana-" + UUID.randomUUID();
        Product product = new Product(productName, 0.59);
        Response response = productService.createProduct(product);
        assertThat(response.statusCode())
            .isEqualTo(200);
        assertThat(response.jsonPath().getString("name"))
            .isEqualTo(productName);
    }

    @Test 
    void createProduct_duplicateName_returns409() {
        var product1 = new Product("Avocado-" + UUID.randomUUID(), 0.89);
        productService.createProduct(product1);
        var response = productService.createProduct(product1);
        assertThat(response.statusCode()).isEqualTo(409);
        
    }

    @Test
    void createProduct_negativePrice_returns422() {
        Product product = new Product("Bad", -5.0);
        Response response = productService.createProduct(product);
        assertThat(response.statusCode()).isEqualTo(422);   // валидация!
    }

    @Test 
    void updateProduct_valid_returns200() {
        String productName = "Orange-" + UUID.randomUUID();
        Product product = new Product(productName, 2.99);
        Response response = productService.createProduct(product);
        int id = response.body().jsonPath().getInt("id");
        Product updatedProduct = new Product(productName, 10.99);
        response = productService.updateProduct(id, updatedProduct);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("name")).isEqualTo(productName);
        assertThat(Double.parseDouble(response.jsonPath().getString("price"))).isEqualTo(10.99);

    }

    @Test
    @DisplayName("Update non existing product")
    void updateProduct_notExisting_returns404() {
        Response response = productService.updateProduct(1000, new Product("New product", 333.00));
        assertThat(response.statusCode()).isEqualTo(404);

    }

    @Test
    @DisplayName("Update product with negative price")
    void updateProduct_negativePrice_returns422() {
        String productName = "Melon-" + UUID.randomUUID();
        Product product = new Product(productName, 2.99);
        Response response = productService.createProduct(product);
        int id = response.body().jsonPath().getInt("id");
        Product updatedProduct = new Product(productName, -10.99);
        response = productService.updateProduct(id, updatedProduct);
        assertThat(response.statusCode()).isEqualTo(422);
    }

    @Test
    @DisplayName("Get all products")
    void getAllProducts_return200() {
        Response allProducts = productService.getAllProducts();
        assertThat(allProducts.statusCode()).isEqualTo(200);
        assertThat(allProducts.jsonPath().getList("").size()).isGreaterThan(0);

    }

    @Test
    @DisplayName("Get existing product -> 200")
    void getProduct_existingId_returns200() {
        int productId = 1;
        Response response = productService.getProduct(productId);
        assertThat(response.statusCode()).isEqualTo(200);
        int actualProductId = response.body().jsonPath().getInt("id");
        assertThat(productId).isEqualTo(actualProductId);
    }

    @Test
    @DisplayName("Get not existing product -> returns 404")
    void getProduct_notExisting_returns404() {
        Response response = productService.getProduct(6332);
        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("Check health of the server")
    void getHealthCheck_return200() {
        Response response = healthService.healthCheck();
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("status")).isEqualTo("ok");
    }

    @Test
    @DisplayName("Delete an existing product")
    void deleteProduct_existingId_returns200() {
        Response response = productService.createProduct(new Product("Bread", 5.99));
        int id = response.jsonPath().getInt("id");
        response = productService.deleteProduct(id);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("detail")).isEqualTo("Product deleted");

    }

    @Test
    @DisplayName("Delete non existing product")
    void deleteProduct_notExisting_returns404() {
        Response response = productService.deleteProduct(1000);
        assertThat(response.statusCode()).isEqualTo(404);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -100.0})
    void createProduct_invalidPrice_returns422(double price) {
        Product p = new Product("Coconut-" + UUID.randomUUID(), price);
        Response response = productService.createProduct(p);
        assertThat(response.statusCode()).isEqualTo(422);
    }
}
