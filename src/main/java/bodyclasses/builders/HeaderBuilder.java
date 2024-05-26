package bodyclasses.builders;

import bodyclasses.request.Event;
import io.qameta.allure.Step;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import constants.APIConstants;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class HeaderBuilder {
    private static final String contentType = "application/json";
    private static final String apiKey = APIConstants.API_KEY;


    // Создание объекта заголовков и добавление в него всех трех заголовков
    @Step("Конструктор заголовка")
    public static Headers basicHeaders(Object body) {
        // Создание объекта заголовков и добавление в него всех трех заголовков
        return new Headers(
                new Header("Content-type", contentType),
                new Header("x-rewind-api", apiKey),
                new Header("x-rewind-signature", GenerateHash.generateHash(body))
        );
    }
    @Step("Конструктор заголовка")
    public static Headers basicHeadersWithList(List<Event> body) {
        // Создание объекта заголовков и добавление в него всех трех заголовков
        return new Headers(
                new Header("Content-type", contentType),
                new Header("x-rewind-api", apiKey),
                new Header("x-rewind-signature", GenerateHash.generateHash(body))
        );
    }
    @Step("Удаление заголовка {headerName}")
    public static Headers removeHeader(Headers headers, String headerName) {
        List<Header> headerList = new ArrayList<>(headers.asList());
        headerList.removeIf(header -> header.getName().equalsIgnoreCase(headerName));
        return new Headers(headerList);
    }

    @Step("Изменение значения заголовка {headerName} на {newValue}")
    public static Headers updateHeader(Headers headers, String headerName, String newValue) {
        List<Header> headerList = new ArrayList<>(headers.asList());
        headerList.removeIf(header -> header.getName().equalsIgnoreCase(headerName));
        headerList.add(new Header(headerName, newValue));
        return new Headers(headerList);
    }
    @Step("Проверить ответ в случае, если не передан X-rewind-API")
    public static void compareEmptyRewindAPIResponse(Response response){
        response.then()
                .assertThat()
                .body("status", equalTo(500))
                .and()
                .body("code", equalTo(1111))
                .and()
                .body("message", equalTo("Api key required in headers"));
    }
    @Step("Проверить ответ в случае, если передан не верный x-rewind-API")
    public static void compareIncorrectRewindAPIResponse(Response response){
        response.then()
                .assertThat()
                .body("status", equalTo(404))
                .and()
                .body("code", equalTo(1110))
                .and()
                .body("message", equalTo("Operator with provided attribute has not exist"));
    }
    @Step("Проверить ответ в случае, если не передан x-rewind-signature")
    public static void compareEmptySignatureResponse(Response response){
        response.then()
                .assertThat()
                .body("status", equalTo(500))
                .and()
                .body("code", equalTo(1112))
                .and()
                .body("message", equalTo("Signature required in headers"));
    }
  /*  @Step("Проверить ответ в случае, если передан не верный x-rewind-signature")
    public static void compareIncorrectSignatureResponse(Response response){
        response.then()
                .assertThat()
                .body("status", equalTo(404)) //?
                .and()
                .body("code", equalTo(1110)) //?
                .and()
                .body("message", equalTo("Operator with provided attribute has not exist")); //?
    }
    */
}
