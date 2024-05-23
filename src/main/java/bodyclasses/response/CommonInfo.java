package bodyclasses.response;

import constants.APIConstants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CommonInfo {
    @Step("Send GET /api/v1/protocol/common-info")
    public static Response sendGetICommonInfo(){
        Response response = given()
                .get(APIConstants.COMMON_INFO);
        return response;
    }
    @Step("Получить pool rate")
    public static double getPoolRate(){
        Response response = given()
                .get(APIConstants.COMMON_INFO);
        String poolRate = response.path("poolRate");
        return Double.parseDouble(poolRate);
    }

}
