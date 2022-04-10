package ru.samokat.tests;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.samokat.ConfigurationAndUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderGetListParemeterizedTest extends ConfigurationAndUtils {

    private final String endpoint;
    private final int statusCode;

    public OrderGetListParemeterizedTest(String endpoint, int statusCode) {
        this.endpoint = endpoint;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] orderParametersData() {
        return new Object[][]{
                {ORDER_CREATING + "?courierId=228", 200},
                {ORDER_CREATING + "?courierId=228&nearestStation=[\"1\", \"2\"]", 200},
                {ORDER_CREATING + "?limit=10&page=0", 200},
                {ORDER_CREATING + "?limit=10&page=0&nearestStation=[\"110\"]", 200}
        };
    }

    @Test
    public void getOrderListDifferentEndpoints() {
        given()
                .contentType(ContentType.JSON)
                .get(endpoint)
                .then()
                .statusCode(statusCode)
                .assertThat()
                .body("orders", notNullValue());
    }

}
