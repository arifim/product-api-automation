package com.ari.mystore;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class DemoTest {

    private String url = "https://jsonplaceholder.typicode.com/users";

    @Test
    public void testDemo() {

        ValidatableResponse all = RestAssured.given()
        .when()
        .contentType(ContentType.JSON)
        .get(url)
        .then()
        .log().all();

        System.out.println(all.toString());
    }

}
