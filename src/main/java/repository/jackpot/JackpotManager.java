package repository.jackpot;

import connector.Connector;
import constants.ConfigConstants;
import io.qameta.allure.Step;
import model.PlutoJackpotParticipants;
import variables.Variables;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class JackpotManager extends JackpotRepository{
    private final JdbcJackpotRepository jdbcJackpotRepository;

    public JackpotManager(Connector connector) {
        super(connector);
        this.jdbcJackpotRepository = new JdbcJackpotRepository(connector);
    }
    @Step("Сгенерировать сумму джекпота")
    public static BigDecimal generateJackpotAmount(){
        Random random = new Random();
        int min = 30000;
        int max = 180000;
        int number = random.nextInt(max - min + 1) + min;
        String randomNumber = Integer.toString(number);
        randomNumber = randomNumber +  "000000000000000000";
        BigDecimal jackpotAmount = new BigDecimal(randomNumber);
        return jackpotAmount;
    }

    @Step("Дождаться завершения джекпота")
    public void waitForJackpotToStopRunning(int jackpotId,long minutes) {
        final int checkIntervalMs = 5000; // Интервал проверки в миллисекундах
        final long maxWaitTimeMs = minutes * 60 * 1000; // Максимальное время ожидания в миллисекундах (сейчас в тесте 11 минут)
        long startTime = System.currentTimeMillis(); // Время начала проверки

        while (true) {
            if (!jdbcJackpotRepository.isJackpotRunning(jackpotId)) {
                System.out.println("Jackpot with id " + jackpotId + " has stopped running.");
                break;
            }

            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) > maxWaitTimeMs) {
                System.out.println("Exceeded maximum wait time of 11 minutes for jackpot with id " + jackpotId + ".");
                break;
            }

            try {
                Thread.sleep(checkIntervalMs); // Ждем перед следующей проверкой
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted while waiting for jackpot to stop running", e);
            }
        }
    }
    @Step("Получить сумму выигрышей всех мест")
    public static BigDecimal sumOfParticipantsRevenueAmount(List<PlutoJackpotParticipants> participants){
        BigDecimal summOfParticipantsRevenueAmount = BigDecimal.valueOf(0);
        for (PlutoJackpotParticipants participant : participants) {
            summOfParticipantsRevenueAmount = summOfParticipantsRevenueAmount.add(participant.getRevenue_amount());
        }
        return summOfParticipantsRevenueAmount;
    }
    @Step("Получить размер выигрыша по месту {place}")
    public static BigDecimal getRevenueAmountByPlace(List<PlutoJackpotParticipants> participants, int place) {
        for (PlutoJackpotParticipants participant : participants) {
            if (participant.getPlace() == place) {
                return participant.getRevenue_amount();
            }
        }
        throw new RuntimeException("Нет пользователя с заданным местом " + place);
    }



    @Step("Проверка, что остаток от пулла {amountDiffUSDT} меньше 5 USDT")
    public static boolean isAmountDiffLessThanExpected(double amountDiffUSDT, double expectedDiff) {
        double threshold = 0.1;// Целевое значение

        // Проверка, что абсолютное значение разницы меньше порога
        return amountDiffUSDT < expectedDiff + threshold;
    }
    @Step("Проверить, что приз совпадает с ожидаемым : {expectedResult} с погрешностью {tolerance}")
    public static void comparePriseWithExpected(List<Double> actualResult, double expectedResult,double tolerance) {
        for (Double participantAmount : actualResult) {
            assertEquals("Value " + participantAmount + " is not within the tolerance of " + expectedResult,
                    expectedResult, participantAmount, tolerance);
        }
    }
    @Step("Проверить, что фактический приз {actualResult} совпадает с ожидаемым : {expectedResult} с погрешностью {tolerance}")
    public static void comparePriseWithExpected(double actualResult, double expectedResult,double tolerance) {
        assertEquals("Value " + actualResult + " is not within the tolerance of " + expectedResult,
                expectedResult, actualResult, tolerance);
    }
    @Step("Привести значение к значению с точностью")
    public static Double setAmountToDecimal(BigDecimal amount){
        return amount.divide(ConfigConstants.DECIMAL,18, RoundingMode.HALF_UP).doubleValue();
    }
    @Step("Убедиться, что приз места {place} совпадает с ожидаемым")
    public static void comparePlaceAmount(BigDecimal jackpotAmount, List<PlutoJackpotParticipants> participants, int place){
        double jackpotAmountFWD = setAmountToDecimal(jackpotAmount);
        double placePercent;
        switch (place) {
            case 1:
                placePercent = 0.25;
                break;
            case 2:
                placePercent = 0.12;
                break;
            case 3:
                placePercent = 0.07;
                break;
            case 4:
                placePercent = 0.03;
                break;
            case 5:
                placePercent = 0.02;
                break;
            default:
                throw new IllegalArgumentException("Invalid place: " + place);
        }
        double expectedAmount = jackpotAmountFWD * placePercent;
        BigDecimal actualAmountDecimal = getRevenueAmountByPlace(participants,place);
        double actualAmount = setAmountToDecimal(actualAmountDecimal);
        comparePriseWithExpected(actualAmount,expectedAmount,5);
    }
    @Step("Получить значение в USDT")
    public static double getAmountUSDT(BigDecimal amountFWD){
        return setAmountToDecimal(amountFWD) * Variables.poolRate;
    }

}
