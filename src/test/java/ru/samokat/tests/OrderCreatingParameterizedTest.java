package ru.samokat.tests;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.samokat.ConfigurationAndUtils;
import ru.samokat.classes.Order;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(Parameterized.class)
public class OrderCreatingParameterizedTest extends ConfigurationAndUtils {
    private final Order order;
    private final int statusCode;

    public OrderCreatingParameterizedTest(Order order, int statusCode) {
        this.order = order;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] orderParametersData() {
        return new Object[][]{{new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "6", "+7 900 300 35 35", 90, "2020-06-06", "Saske, come back to Konoha"), 201}, {new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("BLACK")), 201}, {new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "6", "+7 900 300 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("BLACK", "GREY")), 201}};
    }

    @Test
    public void cratingOrderUniversalTest() {
        given()
                .contentType(ContentType.JSON)
                .and()
                .body(order)
                .when()
                .post(ORDER_CREATING)
                .then()
                .statusCode(statusCode)
                .assertThat()
                .body("track", notNullValue());
    }

}
