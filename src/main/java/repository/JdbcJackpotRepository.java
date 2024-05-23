package repository;

import constants.ConfigConstants;
import connector.Connector;
import variables.Variables;
import io.qameta.allure.Step;
import model.Pluto_jackpot_participants;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.assertEquals;

public class JdbcJackpotRepository {
    private final Connector connector;

    public JdbcJackpotRepository(Connector connector) {
        this.connector = connector;
    }
    public boolean isJackpotRunning(int jackpotId) {
        String sql = "SELECT is_running FROM pluto_jackpot WHERE id = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, jackpotId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_running");
                } else {
                    throw new SQLException("No jackpot found with id: " + jackpotId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if jackpot is running", e);
        }
    }
    @Step("Дождаться завершения джекпота")
    public void waitForJackpotToStopRunning(int jackpotId,long minutes) {
        final int checkIntervalMs = 5000; // Интервал проверки в миллисекундах
        final long maxWaitTimeMs = minutes * 60 * 1000; // Максимальное время ожидания в миллисекундах (сейчас в тесте 11 минут)
        long startTime = System.currentTimeMillis(); // Время начала проверки

        while (true) {
            if (!isJackpotRunning(jackpotId)) {
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

    @Step("Получить id активного джекпота")
    public int getActiveJackpotId() {
        String sql = "SELECT id FROM pluto_jackpot WHERE is_running = true";
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new RuntimeException("Нет активного джекпота");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении активного ID джекпота", e);
        }
    }
    @Step
    ("Изменить размер джекпота по jackpotId : {id}")
    public void updateRevenueAmountById(int id, BigDecimal newRevenueAmount) {
        String sql = "UPDATE pluto_jackpot SET revenue_amount = ? WHERE id = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, newRevenueAmount);
            pstmt.setInt(2, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Обновление не выполнено, возможно, нет записи с id: " + id);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Step
    ("Завершить джекпот по id : {id}")
    public void endJackpotById(int id) {
        String sql = "UPDATE pluto_jackpot SET end_at = ? WHERE id = ?";
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("GMT"));
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);

        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, timestamp);
            pstmt.setInt(2, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Обновление не выполнено, возможно, нет записи с id: " + id);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Step ("Получить размер джекпота по jackpotId : {jackpotId}")
    public BigDecimal getJackpotAmountById(int jackpotId){
        String sql = "SELECT revenue_amount FROM pluto_jackpot WHERE id = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Установка параметра в PreparedStatement
            pstmt.setInt(1, jackpotId);
            // Выполнение запроса
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("revenue_amount");
            } else {
                throw new RuntimeException("Нет джекпота c таким id");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении активного ID джекпота", e);
        }
    }
    @Step("Получить список участников джекпота по jackpotId : {jackpotId}")
    public List<Pluto_jackpot_participants> getJackpotParticipantsListByJackpotId(int jackpotId) {
        List<Pluto_jackpot_participants> jackpotParticipantsList = new ArrayList<>();


        String sql = "SELECT * FROM pluto_jackpot_participants WHERE jackpot_id = ? ORDER BY place ASC";

        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Установка параметра в PreparedStatement
            pstmt.setInt(1, jackpotId);

            // Выполнение запроса
            ResultSet rs = pstmt.executeQuery();

            // Обработка результатов запроса
            while (rs.next()) {


                // Создание объекта Jackpot и добавление его в список
                Pluto_jackpot_participants jackpotParticipant = new Pluto_jackpot_participants(
                        rs.getInt("id"),
                        UUID.fromString(rs.getString("user_id")),
                        rs.getInt("jackpot_id"),
                        UUID.fromString(rs.getString("operator_id")),
                        rs.getInt("tickets"),
                        rs.getBigDecimal("locked_amount"),
                        rs.getInt("place"),
                        rs.getBigDecimal("revenue_amount"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                jackpotParticipantsList.add(jackpotParticipant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jackpotParticipantsList;
    }

    @Step("Получить сумму выигрышей всех мест")
    public BigDecimal sumOfParticipantsRevenueAmount(List<Pluto_jackpot_participants> participants){
        BigDecimal summOfParticipantsRevenueAmount = BigDecimal.valueOf(0);
        for (Pluto_jackpot_participants participant : participants) {
            summOfParticipantsRevenueAmount = summOfParticipantsRevenueAmount.add(participant.getRevenue_amount());
        }
        return summOfParticipantsRevenueAmount;
    }
    @Step("Получить размер выигрыша по месту")
    public BigDecimal getRevenueAmountByPlace(List<Pluto_jackpot_participants> participants, int place) {
        for (Pluto_jackpot_participants participant : participants) {
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
        return Math.abs(amountDiffUSDT - expectedDiff) < threshold;
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
    public static void comparePlaceAmount(BigDecimal jackpotAmount, JdbcJackpotRepository jackpot,List<Pluto_jackpot_participants> participants, int place){
        double jackpotAmountFWD = JdbcJackpotRepository.setAmountToDecimal(jackpotAmount);
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
        BigDecimal actualAmountDecimal = jackpot.getRevenueAmountByPlace(participants,place);
        double actualAmount = JdbcJackpotRepository.setAmountToDecimal(actualAmountDecimal);
        JdbcJackpotRepository.comparePriseWithExpected(actualAmount,expectedAmount,5);
    }
    @Step("Получить значение в USDT")
    public static double getAmountUSDT(BigDecimal amountFWD){
        return JdbcJackpotRepository.setAmountToDecimal(amountFWD) * Variables.poolRate;
    }
}
