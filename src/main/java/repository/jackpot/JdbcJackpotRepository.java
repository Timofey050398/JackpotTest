package repository.jackpot;

import connector.Connector;
import io.qameta.allure.junit4.DisplayName;
import model.PlutoUserJournal;
import org.junit.Test;
import repository.user.JdbcUserRepository;
import io.qameta.allure.Step;
import model.PlutoJackpotParticipants;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcJackpotRepository extends JackpotRepository{

    public JdbcJackpotRepository(Connector connector) {
        super(connector);
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
    @Step("Изменить размер джекпота по jackpotId : {id}")
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
    @Step("Завершить джекпот по id : {id}")
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
    public List<PlutoJackpotParticipants> getJackpotParticipantsListByJackpotId(int jackpotId) {
        List<PlutoJackpotParticipants> jackpotParticipantsList = new ArrayList<>();


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
                PlutoJackpotParticipants jackpotParticipant = new PlutoJackpotParticipants(
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

    @Step("Установить всем записям дату стейка {days} дней назад")
    public void setCreatedAtDaysBefore(int days) {
        String sql = "UPDATE pluto_user_journal SET created_at = ? WHERE operation_type = 40";
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("GMT"));
        LocalDateTime yesterdayDateTime = currentDateTime.minus(days, ChronoUnit.DAYS);
        Timestamp timestamp = Timestamp.valueOf(yesterdayDateTime);

        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, timestamp);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Обновление не выполнено, возможно, нет записей с типом операции 40.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Step("Посчитать сумму стейка по пользователю с foreign_id : {foreign_id}")
    public BigDecimal calculateUserStake(String foreign_id) {
        String sql = "SELECT * FROM pluto_user_journal WHERE operation_type = 40 AND user_id = ?::uuid";
        JdbcUserRepository userRepository = new JdbcUserRepository(connector);
        String userId = userRepository.getIdByForeignId(foreign_id);
        BigDecimal userStakeAmount = BigDecimal.valueOf(0);

        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Установка параметра в PreparedStatement
            pstmt.setString(1, userId);

            // Выполнение запроса
            ResultSet rs = pstmt.executeQuery();

            // Обработка результатов запроса
            while (rs.next()) {
                // Создание объекта PlutoUserJournal и добавление его в список
                PlutoUserJournal userJournal = new PlutoUserJournal(
                        rs.getInt("id"),
                        UUID.fromString(rs.getString("user_id")),
                        UUID.fromString(rs.getString("operator_id")),
                        rs.getInt("operation_type"),
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                userStakeAmount = userStakeAmount.add(userJournal.getAmount());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userStakeAmount;
    }
}
