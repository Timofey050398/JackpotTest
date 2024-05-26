import bodyclasses.response.CommonInfo;
import constants.ConfigConstants;
import connector.Connector;
import connector.PostgresConnector;
import org.junit.ClassRule;
import repository.jackpot.JackpotManager;
import variables.Variables;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Pluto_jackpot_participants;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.jackpot.JdbcJackpotRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

@DisplayName("Тестирование корректного распределения приза джекпота")
public class JackpotTest {


    private static BigDecimal jackpotAmount;
    private static List<Pluto_jackpot_participants> participants;
    @ClassRule
    public static EnvironmentSetup environmentSetup = new EnvironmentSetup();
    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = environmentSetup.getProperty("API_URL");
        //Выбрать сумму джекпота
        jackpotAmount = ConfigConstants.JACKPOT_AMOUNT;
        Connector sqlConnector = PostgresConnector.builder()
                .url(environmentSetup.getProperty("DB_URL"))
                .user(environmentSetup.getProperty("DB_USER"))
                .password(environmentSetup.getProperty("DB_PASSWORD"))
                .build();
        JdbcJackpotRepository jackpot = new JdbcJackpotRepository(sqlConnector);
        int jackpotId = jackpot.getActiveJackpotId();
        jackpot.updateRevenueAmountById(jackpotId,jackpotAmount);
        jackpot.endJackpotById(jackpotId);
        int minutes = Integer.parseInt(environmentSetup.getProperty("MINUTES_OF_WAITING_JACKPOT_END"));
        new JackpotManager(sqlConnector).waitForJackpotToStopRunning(jackpotId,minutes);
        participants = jackpot.getJackpotParticipantsListByJackpotId(jackpotId);
        Variables.poolRate = CommonInfo.getPoolRate();
    }
    @Test
    @DisplayName("Проверить, что весь пулл разыгран")
    public void compareAllPoolAmountIsPresented(){
        BigDecimal amountDiff = jackpotAmount.subtract(JackpotManager.sumOfParticipantsRevenueAmount(participants));
        double amountDiffUSDT = JackpotManager.getAmountUSDT(amountDiff);
        assertTrue("Amount difference (" + amountDiffUSDT + ") is not less than 5 with a tolerance of 0.1",
                JackpotManager.isAmountDiffLessThanExpected(amountDiffUSDT,5));
    }
    @Test
    @DisplayName("Проверить, что пользователи с 6 по n место получили приз, эквивалентно 5 USDT")
    public void comparePriseWithFiveUSDT(){
        List<Double> participantsAmount = new ArrayList<>();
        double expectedValue = 5.0;
        double tolerance = 0.2;
        for(Pluto_jackpot_participants participant :participants){
            if(participant.getPlace()>5 && participant.getRevenue_amount().compareTo(BigDecimal.ZERO) != 0){
                BigDecimal participantAmount = participant.getRevenue_amount();
                double participantAmountUSDT = JackpotManager.getAmountUSDT(participantAmount);
                participantsAmount.add(participantAmountUSDT);
            }
        }
        JackpotManager.comparePriseWithExpected(participantsAmount,expectedValue,tolerance);
    }
    @Test
    @DisplayName ("Проверить выигрыш первого места")
    public void compareFirstPlaceAmount(){
        JackpotManager.comparePlaceAmount(jackpotAmount,participants,1);
    }
    @Test
    @DisplayName ("Проверить выигрыш второго места")
    public void compareSecondPlaceAmount(){
        JackpotManager.comparePlaceAmount(jackpotAmount,participants,2);
    }
    @Test
    @DisplayName ("Проверить выигрыш третьего места")
    public void compareThirdPlaceAmount(){
        JackpotManager.comparePlaceAmount(jackpotAmount,participants,3);
    }
    @Test
    @DisplayName ("Проверить выигрыш четвертого места")
    public void compareFourPlaceAmount(){
        JackpotManager.comparePlaceAmount(jackpotAmount,participants,4);
    }
    @Test
    @DisplayName ("Проверить выигрыш пятого места")
    public void compareFivePlaceAmount(){
        JackpotManager.comparePlaceAmount(jackpotAmount,participants,5);
    }
    /* TO DO - заменить amount на случайно генерируемый(парадокс Пестицида)*/
    /* TO DO - Добавить тесты на изменение баланса джекпота за счет батчей */
}

