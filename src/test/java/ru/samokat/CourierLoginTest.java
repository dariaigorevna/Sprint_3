package ru.samokat;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check creating courier")
    @Description("Courier can authorize")
    public void courierAuthorizationSuccess() {
        CourierRegisterScooter courier = new CourierRegisterScooter();
        ArrayList<String> loginPassCourier = courier.registerNewCourierAndReturnLoginPassword();
        String authorizationRequestBody = "{\"login\":\"" + loginPassCourier.get(0) + "\","
                + "\"password\":\"" + loginPassCourier.get(1) + "\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(authorizationRequestBody)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(200).assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Checking authorization error - login") // имя теста
    @Description("Checking authorization error - incorrect login") // описание теста
    public void courierAuthorizationLoginError() {
        CourierRegisterScooter courier = new CourierRegisterScooter();
        ArrayList<String> loginPassCourier = courier.registerNewCourierAndReturnLoginPassword();
        String authorizationRequestBody = "{\"login\":\"" + loginPassCourier.get(0) + "error" + "\","
                + "\"password\":\"" + loginPassCourier.get(1) + "\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(authorizationRequestBody)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404).assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Checking authorization error - password") // имя теста
    @Description("Checking authorization error - incorrect password") // описание теста
    public void courierAuthorizationPasswordError() {
        CourierRegisterScooter courier = new CourierRegisterScooter();
        ArrayList<String> loginPassCourier = courier.registerNewCourierAndReturnLoginPassword();
        String authorizationRequestBody = "{\"login\":\"" + loginPassCourier.get(0) + "\","
                + "\"password\":\"" + loginPassCourier.get(1) + "error" + "\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(authorizationRequestBody)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404).assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Checking request attributes - the login field")
    @Description("In order to authorize in system, you need to pass all the required fields to the handle. Request without the field login")
    public void courierAuthorizationRequiredAttributeLogin() {
        CourierRegisterScooter courier = new CourierRegisterScooter();
        ArrayList<String> loginPassCourier = courier.registerNewCourierAndReturnLoginPassword();
        String authorizationRequestBody = "{\"password\":\"" + loginPassCourier.get(1) + "\"}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(authorizationRequestBody)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

}
