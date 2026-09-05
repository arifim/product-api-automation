package com.ari.mystore.services;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseService {

    protected RequestSpecification spec() {
        return new RequestSpecBuilder()
            .setBaseUri("http://localhost:8000")
            .setContentType(ContentType.JSON)
            .build();
    }

}
