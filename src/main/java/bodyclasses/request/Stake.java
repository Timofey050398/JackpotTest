package bodyclasses.request;
import bodyclasses.builders.HeaderBuilder;
import io.restassured.http.Headers;
import constants.APIConstants;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import repository.jackpot.JdbcJackpotRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Stake {
    private  String id;
    private  String userId;
    private  String amount;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String foreignId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Stake(String id, String userId, String amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }


    @Step("Send correct POST /api/v2.1/user/stake request")
    public static Response sendStake(Stake stake){
        Headers headers = HeaderBuilder.basicHeaders(stake);
        Response response =given()
                .headers(headers)
                .and()
                .body(stake)
                .when()
                .post(APIConstants.STAKE);
        return response;
    }
    @Step("Send correct POST /api/v2.1/user/stake request")
    public static Response sendStake(Stake stake, Headers headers){
        Response response =given()
                .headers(headers)
                .and()
                .body(stake)
                .when()
                .post(APIConstants.STAKE);
        return response;
    }
    @Step("Send correct POST /api/v2.1/user/unstake request")
    public static Response sendUnstake(Stake stake){
        Headers headers = HeaderBuilder.basicHeaders(stake);
        Response response =given()
                .headers(headers)
                .and()
                .body(stake)
                .when()
                .post(APIConstants.UNSTAKE);
        return response;
    }
    @Step("Send correct POST /api/v2.1/user/unstake request")
    public static Response sendUnstake(Stake stake, Headers headers){
        Response response =given()
                .headers(headers)
                .and()
                .body(stake)
                .when()
                .post(APIConstants.UNSTAKE);
        return response;
    }
    @Step("Проверить корректный ответ на стейк/анстейк")
    public void compareWithCorrectResponse(Response response, JdbcJackpotRepository jackpot){
        String foreignId = this.getUserId();
        String stakeAmount = jackpot.calculateUserStake(foreignId).toString();
        response.then()
                .assertThat()
                .body("status", equalTo("ok"))
                .and()
                .body("data.result", equalTo(stakeAmount))
                .and()
                .statusCode(201);
    }
    @Step("Проверить ответ в случае, если пользователя не существует")
    public static void compareClientNotFoundResponse(Response response){
        response.then()
                .assertThat()
                .body("status", equalTo("error"))
                .and()
                .body("error.code", equalTo(404))
                .and()
                .body("error.message", equalTo("Client with provided attribute has not found"));
    }
    @Step("Проверить ответ в случае, если сумма анстейка больше суммы застейканных средств")
    public static void compareAmountMoreThanAvailableResponse(Response response){
        response.then()
                .assertThat()
                .body("status", equalTo("error"))
                .and()
                .body("error.code", equalTo(409))
                .and()
                .body("error.message", equalTo("Amount user try to lock is more than available amount"));
    }
    @Step("Проверить ответ в случае, если переданная сумма 0 или отрицательное число")
    public static void compareAmountNotPositiveResponse(Response response){
        response.then()
                .assertThat()
                .body("status", equalTo("error"))
                .and()
                .body("error.code", equalTo(409))
                .and()
                .body("error.message", equalTo("Amount should be positive"));
    }
    @Step("Проверить ответ в случае, если переданная сумма 0 или отрицательное число")
    public static void compareIdAlreadyExistsResponse(Response response){
        response.then()
                .assertThat()
                .body("status", equalTo("error"))
                .and()
                .body("error.code", equalTo(409))
                .and()
                .body("error.message", equalTo("Request Id already exists"));
    }

}
