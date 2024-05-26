package bodyclasses.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.APIConstants;
import io.qameta.allure.Step;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static io.qameta.allure.util.ResultsUtils.bytesToHex;

public class GenerateHash {
    @Step("сгенерировать хеш заголовка x-rewind-signature")
    public static String generateHash(Object body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(body);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(APIConstants.SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKeySpec);

            byte[] hmacSha256 = sha256Hmac.doFinal(jsonString.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hmacSha256);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Step("сгенерировать хеш заголовка x-rewind-signature")
    public static String generateHash(List<Object> requestBody) {
        try {
            List<Object> body = requestBody;
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(body);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(APIConstants.SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKeySpec);
            byte[] hmacSha256 = sha256Hmac.doFinal(jsonString.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hmacSha256);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
