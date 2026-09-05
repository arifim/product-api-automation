package com.ari.mystore.services;

import com.ari.mystore.models.Product;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class ProductService extends BaseService {

    public Response createProduct(Product product) {
        return given().spec(spec()).body(product)
               .when().post("/product/");
    }

    public Response getProduct(int id) {
        return given().spec(spec())
               .when().get("/product/" + id);
    }
    // по аналогии: updateProduct(id, product), deleteProduct(id)
}
