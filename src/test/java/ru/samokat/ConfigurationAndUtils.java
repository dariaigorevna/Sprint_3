package ru.samokat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import org.junit.Before;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.SortedMap;

public class ConfigurationAndUtils {

    protected static final String COURIER_CREATING = "/api/v1/courier";
    protected static final String COURIER_LOGIN = "/api/v1/courier/login";
    protected static final String COURIER_DELETING = "/api/v1/courier/:id";
    protected static final String ORDER_CREATING = "/api/v1/orders";

    @Before
    public void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public String convertMapToJson(SortedMap<String, String> elements) {

        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>() {
        }.getType();
        String gsonString = gson.toJson(elements, gsonType);
        return gsonString;
    }
}
