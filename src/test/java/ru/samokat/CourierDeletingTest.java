package ru.samokat;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierDeletingTest {

    //метод получения id курьера по его логину и паролю
    public String getCourierId(ArrayList<String> loginPass){
        String registerRequestBody = "{\"login\":\"" + loginPass.get(0) + "\","
                + "\"password\":\"" + loginPass.get(1) + "\"}";
        Id id = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier/login")
                .as(Id.class);
        return id.getId();
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check success deleting courier")
    @Description("Test failed cause incorrect documentation. It was discussed in the Slack")
    public void deletingCourierSuccess() {
        CourierRegisterScooter courier = new CourierRegisterScooter();
        String deleteRequestBody = "{\"id\":\"" +
                getCourierId(courier.registerNewCourierAndReturnLoginPassword())+ "\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(deleteRequestBody)
                .when()
                .delete("/api/v1/courier/:id")
                .then().statusCode(200).assertThat()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check deleting courier. Request without id")
    @Description("Test failed cause incorrect documentation - it was discussed in the Slack")
    public void deletingCourierWithoutId() {
        String deleteRequestBody = "{\"NOTid\":\"" + "notId" + "\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(deleteRequestBody)
                .when()
                .delete("/api/v1/courier/:id")
                .then().statusCode(400)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Check deleting courier. Request with id that does not exist")
    @Description("Test failed cause incorrect documentation - it was discussed in the Slack")
    public void deletingCourierDoNotExistId() {
        // id has string type so this is ok
        String deleteRequestBody = "{\"id\":\"" + "does_not_exist_id" + "\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(deleteRequestBody)
                .when()
                .delete("/api/v1/courier/:id")
                .then().statusCode(404)
                .assertThat()
                .body("message", equalTo("Курьера с таким id нет"));
    }

}
