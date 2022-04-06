package ru.samokat;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderGetListParemeterizedTest {

    private final String endpoint;
    private final int statusCode;

    public OrderGetListParemeterizedTest(String endpoint, int statusCode) {
        this.endpoint = endpoint;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] orderParametersData() {
        return new Object[][]{
                {"/api/v1/orders?courierId=228", 200},
                {"/api/v1/orders?courierId=228&nearestStation=[\"1\", \"2\"]", 200},
                {"/api/v1/orders?limit=10&page=0", 200},
                {"/api/v1/orders?limit=10&page=0&nearestStation=[\"110\"]", 200}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void getOrderListDifferentEndpoints() {
        given()
                .header("Content-Type", "application/json")
                .get(endpoint)
                .then().statusCode(statusCode)
                .assertThat().body("orders", notNullValue());
    }

}
