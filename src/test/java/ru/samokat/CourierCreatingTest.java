package ru.samokat;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreatingTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @Test
    @DisplayName("Check creating courier")
    @Description("Courier can be creating")
    public void courierCouldBeCreating() {
        Courier courier = new Courier(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201).assertThat()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check non-creating the same couriers")
    @Description("Two the same courier cannot be creating")
    public void courierShouldBeUnique() {
        Courier courier = new Courier(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(409)
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Checking request attributes - the password field")
    @Description("In order to create a courier, you need to pass all the required fields to the handle. Request without field the password")
    public void courierRequiredAttributePassword() {
        String registerRequestBody = "{\"login\":\"" + RandomStringUtils.randomAlphabetic(10) + "\","
                + "\"firstName\":\"" + RandomStringUtils.randomAlphabetic(10) + "\"}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Checking request attributes - the login field")
    @Description("In order to create a courier, you need to pass all the required fields to the handle. Request without the field login")
    public void courierRequiredAttributeLogin() {

        String registerRequestBody = "{\"password\":\"" + RandomStringUtils.randomAlphabetic(10) + "\","
                + "\"firstName\":\"" + RandomStringUtils.randomAlphabetic(10) + "\"}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}

