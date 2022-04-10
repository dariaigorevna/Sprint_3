package ru.samokat.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import ru.samokat.ConfigurationAndUtils;
import ru.samokat.classes.Courier;

import java.util.SortedMap;
import java.util.TreeMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreatingTest extends ConfigurationAndUtils {

    @Test
    @DisplayName("Check creating courier")
    @Description("Courier can be creating")
    public void courierCouldBeCreating() {
        Courier courier = new Courier(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        given()
                .contentType(ContentType.JSON)
                .and()
                .body(courier)
                .when()
                .post(COURIER_CREATING)
                .then()
                .statusCode(201)
                .assertThat()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check non-creating the same couriers")
    @Description("Two the same courier cannot be creating")
    public void courierShouldBeUnique() {
        Courier courier = new Courier(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        given()
                .contentType(ContentType.JSON)
                .and()
                .body(courier)
                .when()
                .post(COURIER_CREATING)
                .then()
                .statusCode(201);
        given()
                .contentType(ContentType.JSON)
                .and()
                .body(courier)
                .when()
                .post(COURIER_CREATING)
                .then()
                .statusCode(409)
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Checking request attributes - the password field")
    @Description("In order to create a courier, you need to pass all the required fields to the handle. Request without field the password")
    public void courierRequiredAttributePassword() {
        SortedMap<String, String> elements = new TreeMap();
        elements.put("login", RandomStringUtils.randomAlphabetic(10));
        elements.put("firstName", RandomStringUtils.randomAlphabetic(10));
        String registerRequestBody = convertMapToJson(elements);

        given()
                .contentType(ContentType.JSON)
                .and()
                .body(registerRequestBody)
                .when()
                .post(COURIER_CREATING)
                .then()
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Checking request attributes - the login field")
    @Description("In order to create a courier, you need to pass all the required fields to the handle. Request without the field login")
    public void courierRequiredAttributeLogin() {

        SortedMap<String, String> elements = new TreeMap();
        elements.put("password", RandomStringUtils.randomAlphabetic(10));
        elements.put("firstName", RandomStringUtils.randomAlphabetic(10));
        String registerRequestBody = convertMapToJson(elements);

        given().
                contentType(ContentType.JSON)
                .and()
                .body(registerRequestBody)
                .when()
                .post(COURIER_CREATING)
                .then().statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}

