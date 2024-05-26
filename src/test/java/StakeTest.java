import bodyclasses.builders.HeaderBuilder;
import bodyclasses.request.Stake;
import com.github.javafaker.Faker;
import connector.Connector;
import connector.PostgresConnector;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import repository.jackpot.JdbcJackpotRepository;
import repository.user.JdbcUserRepository;

@DisplayName("Тесты метода api/v2.1/user/stake")
public class StakeTest {

    private static JdbcJackpotRepository jackpot;
    private static JdbcUserRepository user;
    Faker faker = new Faker();
    @ClassRule
    public static EnvironmentSetup environmentSetup = new EnvironmentSetup();
    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = environmentSetup.getProperty("API_URL");
        Connector sqlConnector = PostgresConnector.builder()
                .url(environmentSetup.getProperty("DB_URL"))
                .user(environmentSetup.getProperty("DB_USER"))
                .password(environmentSetup.getProperty("DB_PASSWORD"))
                .build();
        jackpot = new JdbcJackpotRepository(sqlConnector);
        user = new JdbcUserRepository(sqlConnector);
    }

    @Test
    @DisplayName("Проверка корректного ответа на запрос стейка")
    public void testCorrectStakeRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String amount = "1000000000000000000";
        Stake stake = new Stake(id,userId,amount);
        Response response = Stake.sendStake(stake);
        stake.compareWithCorrectResponse(response,jackpot);
    }
    @Test
    @DisplayName("Проверка корректного ответа на запрос стейка")
    public void testCreateUserByStakeRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String amount = "1000000000000000000";
        boolean isUserExisted = user.isUserExist(userId);
        Assert.assertFalse("Пользователь уже есть в системе",isUserExisted);
        Stake stake = new Stake(id,userId,amount);
        Stake.sendStake(stake);
        isUserExisted = user.isUserExist(userId);
        Assert.assertTrue("Пользователь не создался",isUserExisted);
    }
    @Test
    @DisplayName("Проверка ответа при запросе с уже существующим id")
    public void testExistedIdRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String amount = "1000000000000000000";
        Stake stake = new Stake(id,userId,amount);
        Stake.sendStake(stake);
        Response response = Stake.sendStake(stake);
        Stake.compareIdAlreadyExistsResponse(response);
    }
    @Test
    @DisplayName("Проверка ответа при запросе с суммой 0")
    public void testZeroAmountRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String amount = "00000000";
        Stake stake = new Stake(id,userId,amount);
        Response response = Stake.sendStake(stake);
        Stake.compareAmountNotPositiveResponse(response);
    }
    @Test
    @DisplayName("Проверка ответа при запросе с отрицательной суммой")
    public void testNegativeAmountRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String amount = "-1000000000000000000";
        Stake stake = new Stake(id,userId,amount);
        Response response = Stake.sendStake(stake);
        Stake.compareAmountNotPositiveResponse(response);
    }
    @Test
    @DisplayName("Проверка ответа на запрос стейка без x-rewind-api")
    public void testWithoutRewindApiRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String amount = "1000000000000000000";
        Stake stake = new Stake(id,userId,amount);
        Headers headers = HeaderBuilder.basicHeaders(stake);
        headers = HeaderBuilder.removeHeader(headers,"x-rewind-api");
        Response response = Stake.sendStake(stake,headers);
        HeaderBuilder.compareEmptyRewindAPIResponse(response);
    }
    @Test
    @DisplayName("Проверка ответа на запрос с пустым x-rewind-api")
    public void testEmptyRewindApiRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String amount = "1000000000000000000";
        Stake stake = new Stake(id,userId,amount);
        Headers headers = HeaderBuilder.basicHeaders(stake);
        headers = HeaderBuilder.updateHeader(headers,"x-rewind-api","");
        Response response = Stake.sendStake(stake,headers);
        HeaderBuilder.compareEmptyRewindAPIResponse(response);
    }
    @Test
    @DisplayName("Проверка ответа на запрос с некорректным x-rewind-api")
    public void testIncorrectRewindApiRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String incorrectHeader = faker.internet().password();
        String amount = "1000000000000000000";
        Stake stake = new Stake(id,userId,amount);
        Headers headers = HeaderBuilder.basicHeaders(stake);
        headers = HeaderBuilder.updateHeader(headers,"x-rewind-api",incorrectHeader);
        Response response = Stake.sendStake(stake,headers);
        HeaderBuilder.compareIncorrectRewindAPIResponse(response);
    }
    @Test
    @DisplayName("Проверка ответа на запрос стейка без x-rewind-signature")
    public void testWithoutRewindSignatureRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String amount = "1000000000000000000";
        Stake stake = new Stake(id,userId,amount);
        Headers headers = HeaderBuilder.basicHeaders(stake);
        headers = HeaderBuilder.removeHeader(headers,"x-rewind-signature");
        Response response = Stake.sendStake(stake,headers);
        HeaderBuilder.compareEmptySignatureResponse(response);
    }
    @Test
    @DisplayName("Проверка ответа на запрос с пустым x-rewind-signature")
    public void testEmptySignatureRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String amount = "1000000000000000000";
        Stake stake = new Stake(id,userId,amount);
        Headers headers = HeaderBuilder.basicHeaders(stake);
        headers = HeaderBuilder.updateHeader(headers,"x-rewind-signature","");
        Response response = Stake.sendStake(stake,headers);
        HeaderBuilder.compareEmptySignatureResponse(response);
    }
  /*  @Test
    @DisplayName("Проверка ответа на запрос с некорректным x-rewind-signature")
    public void testIncorrectSignatureRequest(){
        String id = faker.internet().uuid();
        String userId = faker.internet().uuid();
        String incorrectHeader = faker.internet().password();
        String amount = "1000000000000000000";
        Stake stake = new Stake(id,userId,amount);
        Headers headers = HeaderBuilder.basicHeaders(stake);
        headers = HeaderBuilder.updateHeader(headers,"x-rewind-signature",incorrectHeader);
        Response response = Stake.sendStake(stake,headers);
        HeaderBuilder.compareIncorrectSignatureResponse(response);
    }

   */
    /* TO DO Добавить проверки на корректность JSON-схемы
     ,проверку на передачу не цифровой стринги в amount,
      заменить amount на случайно генерируемый(парадокс Пестицида)*/
}
