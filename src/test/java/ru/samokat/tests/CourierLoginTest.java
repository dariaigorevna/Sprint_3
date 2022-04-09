package ru.samokat.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;
import ru.samokat.ConfigurationAndUtils;
import ru.samokat.classes.CourierRegisterScooter;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends ConfigurationAndUtils {

    @Test
    @DisplayName("Check creating courier")
    @Description("Courier can authorize")
    public void courierAuthorizationSuccess() {
        CourierRegisterScooter courier = new CourierRegisterScooter();
        ArrayList<String> loginPassCourier = courier.registerNewCourierAndReturnLoginPassword();

        SortedMap<String, String> elements = new TreeMap();
        elements.put("login", loginPassCourier.get(0));
        elements.put("password", loginPassCourier.get(1));
        String authorizationRequestBody = convertMapToJson(elements);

        given()
                .contentType(ContentType.JSON)
                .and()
                .body(authorizationRequestBody)
                .when()
                .post(COURIER_LOGIN)
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Checking authorization error - login") // имя теста
    @Description("Checking authorization error - incorrect login") // описание теста
    public void courierAuthorizationLoginError() {
        CourierRegisterScooter courier = new CourierRegisterScooter();
        ArrayList<String> loginPassCourier = courier.registerNewCourierAndReturnLoginPassword();

        SortedMap<String, String> elements = new TreeMap();
        elements.put("login", loginPassCourier.get(0) + "error");
        elements.put("password", loginPassCourier.get(1));
        String authorizationRequestBody = convertMapToJson(elements);

        given()
                .contentType(ContentType.JSON)
                .and()
                .body(authorizationRequestBody)
                .when()
                .post(COURIER_LOGIN)
                .then()
                .statusCode(404)
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Checking authorization error - password") // имя теста
    @Description("Checking authorization error - incorrect password") // описание теста
    public void courierAuthorizationPasswordError() {
        CourierRegisterScooter courier = new CourierRegisterScooter();
        ArrayList<String> loginPassCourier = courier.registerNewCourierAndReturnLoginPassword();

        SortedMap<String, String> elements = new TreeMap();
        elements.put("login", loginPassCourier.get(0));
        elements.put("password", loginPassCourier.get(1) + "error");
        String authorizationRequestBody = convertMapToJson(elements);

        given()
                .contentType(ContentType.JSON)
                .and()
                .body(authorizationRequestBody)
                .when()
                .post(COURIER_LOGIN)
                .then()
                .statusCode(404)
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Checking request attributes - the login field")
    @Description("In order to authorize in system, you need to pass all the required fields to the handle. Request without the field login")
    public void courierAuthorizationRequiredAttributeLogin() {
        CourierRegisterScooter courier = new CourierRegisterScooter();
        ArrayList<String> loginPassCourier = courier.registerNewCourierAndReturnLoginPassword();

        SortedMap<String, String> elements = new TreeMap();
        elements.put("password", loginPassCourier.get(1));
        String authorizationRequestBody = convertMapToJson(elements);

        given()
                .contentType(ContentType.JSON)
                .and()
                .body(authorizationRequestBody)
                .when()
                .post(COURIER_LOGIN)
                .then()
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

}
