package bodyclasses.request;

import constants.APIConstants;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class Stake {
    private  String foreignId;
    private  String amount;

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Stake(String foreignId, String amount) {
        this.foreignId = foreignId;
        this.amount = amount;
    }

    @Step("Send correct POST /api/v2.1/user/stake request")
    public static Response sendStake(Stake stake){
        Response response =given()
                .header("Content-type", "application/json")
                .header("x-rewind-api","M3jKf30r8KWrlLrlVd6VPfEYpFmmErxP")
                .header("x-rewind-signature","84e695d6dbc2f1cdf57bd1c9660504b954b4e73f38f9fded4574001b044877ad")
                .and()
                .body(stake)
                .when()
                .post(APIConstants.STAKE);
        return response;
    }
    @Step("Checks if the response is correct for a request with correct data")
    public static void compareWithCorrectResponse(Response response){
        response.then()
                .assertThat()
                .statusCode(201);
    }
}
