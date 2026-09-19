package com.ari.mystore.services;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class HealthService extends BaseService {

    public Response healthCheck() {
        return given()
                .spec(spec())
                .when()
                .get("/health");
    }
}
