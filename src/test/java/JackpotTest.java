import bodyclasses.response.CommonInfo;
import constants.APIConstants;
import constants.ConfigConstants;
import constants.DBConstants;
import connector.Connector;
import connector.PostgresConnector;
import variables.Variables;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Pluto_jackpot_participants;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.JdbcJackpotRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static repository.JdbcJackpotRepository.isAmountDiffLessThanExpected;

@DisplayName("Тестирование корректного распределения приза джекпота")
public class JackpotTest {

    private static BigDecimal jackpotAmount;
    private static List<Pluto_jackpot_participants> participants;
    private static JdbcJackpotRepository jackpot;
    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = APIConstants.BASE_URL;
        //Выбрать сумму джекпота
        jackpotAmount = ConfigConstants.JACKPOT_AMOUNT;
        Connector sqlConnector = PostgresConnector.builder()
                .url(DBConstants.DB_URL)
                .user(DBConstants.DB_USER)
                .password(DBConstants.DB_PASSWORD)
                .build();
        jackpot = new JdbcJackpotRepository(sqlConnector);
        int jackpotId = jackpot.getActiveJackpotId();
        Variables.jackpotId = jackpotId;
        jackpot.updateRevenueAmountById(jackpotId,jackpotAmount);
        jackpot.endJackpotById(jackpotId);
        jackpot.waitForJackpotToStopRunning(jackpotId,11);
        participants = jackpot.getJackpotParticipantsListByJackpotId(jackpotId);
        Variables.poolRate = CommonInfo.getPoolRate();
    }
    @Test
    @DisplayName("Проверить, что весь пулл разыгран")
    public void compareAllPoolAmountIsPresented(){
        BigDecimal amountDiff = jackpotAmount.subtract(jackpot.sumOfParticipantsRevenueAmount(participants));
        double amountDiffUSDT = JdbcJackpotRepository.getAmountUSDT(amountDiff);
        assertTrue("Amount difference (" + amountDiffUSDT + ") is not less than 5 with a tolerance of 0.1",
                isAmountDiffLessThanExpected(amountDiffUSDT,5));
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
                double participantAmountUSDT = JdbcJackpotRepository.getAmountUSDT(participantAmount);
                participantsAmount.add(participantAmountUSDT);
            }
        }
        JdbcJackpotRepository.comparePriseWithExpected(participantsAmount,expectedValue,tolerance);
    }
    @Test
    @DisplayName ("Проверить выигрыш первого места")
    public void compareFirstPlaceAmount(){
        JdbcJackpotRepository.comparePlaceAmount(jackpotAmount,jackpot,participants,1);
    }
    @Test
    @DisplayName ("Проверить выигрыш второго места")
    public void compareSecondPlaceAmount(){
        JdbcJackpotRepository.comparePlaceAmount(jackpotAmount,jackpot,participants,2);
    }
    @Test
    @DisplayName ("Проверить выигрыш третьего места")
    public void compareThirdPlaceAmount(){
        JdbcJackpotRepository.comparePlaceAmount(jackpotAmount,jackpot,participants,3);
    }
    @Test
    @DisplayName ("Проверить выигрыш четвертого места")
    public void compareFourPlaceAmount(){
        JdbcJackpotRepository.comparePlaceAmount(jackpotAmount,jackpot,participants,4);
    }
    @Test
    @DisplayName ("Проверить выигрыш пятого места")
    public void compareFivePlaceAmount(){
        JdbcJackpotRepository.comparePlaceAmount(jackpotAmount,jackpot,participants,5);
    }
}

