package repository.user;

import connector.Connector;
import io.qameta.allure.Step;
import model.OperatorClient;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

public class JdbcUserRepository implements UserRepository {
    private final Connector connector;

    public JdbcUserRepository(Connector connector){
        //NamedParameterJdbcTemplate
        this.connector = connector;
    }
    /*@Override
    public void create(User user) {
        template.update("INSERT INTO operator_clients VALUES(:foreign_id, :operator_id, :created_at, :updated_at, :address, :address_memo, :total_earned_amount)"
                , Map.of(
                        "foreign_id", user.getForeign_id(),
                        "operator_id", user.getOperator_id(),
                        "created_at", user.getCreated_at(),
                        "updated_at", user.getUpdated_at(),
                        "address", user.getAddress(),
                        "address_memo", user.getAddress_memo(),
                        "total_earned_amount",user.getTotal_earned_amount()
                )
        );
    }
*/
    @Override
    public OperatorClient create(OperatorClient operator_client) {
        OperatorClient insertedRecord = null; // Инициализируем значение вставленной записи значением по умолчанию
        String sqlInsert = "INSERT INTO operator_clients (foreign_id, operator_id, created_at, updated_at, address_memo, address, total_earned_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlSelect = "SELECT * FROM operator_clients WHERE id = ?"; // Запрос для выборки вставленной строки

        try (Connection connection = connector.getConnection();
             PreparedStatement statementInsert = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement statementSelect = connection.prepareStatement(sqlSelect)) {

            // Установка параметров для запроса на вставку
            statementInsert.setString(1, operator_client.getForeign_id());
            statementInsert.setObject(2, UUID.fromString(operator_client.getOperator_id()));

            LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("GMT"));
            Timestamp timestamp = Timestamp.valueOf(currentDateTime);

            statementInsert.setTimestamp(3, timestamp);
            statementInsert.setTimestamp(4, timestamp);
            statementInsert.setString(5, operator_client.getAddress_memo());
            statementInsert.setString(6, operator_client.getAddress());
            statementInsert.setLong(7, operator_client.getTotal_earned_amount());

            // Выполнение запроса на вставку
            statementInsert.executeUpdate();

            // Получение сгенерированного ключа
            try (ResultSet generatedKeys = statementInsert.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    UUID generatedId = (UUID) generatedKeys.getObject(1);
                    operator_client.setId(generatedId.toString()); // Установка сгенерированного ID в объекте оператора
                }
            }

            // Установка параметров для запроса на выборку
            statementSelect.setObject(1, UUID.fromString(operator_client.getId()));

            // Выполнение запроса на выборку
            try (ResultSet resultSet = statementSelect.executeQuery()) {
                if (resultSet.next()) {
                    // Создание объекта оператора на основе выбранных данных
                    insertedRecord = new OperatorClient(
                            resultSet.getString("id"),
                            resultSet.getString("foreign_id"),
                            resultSet.getString("operator_id"),
                            resultSet.getString("created_at"),
                            resultSet.getString("updated_at"),
                            resultSet.getString("address_memo"),
                            resultSet.getString("address"),
                            resultSet.getLong("total_earned_amount")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedRecord; // Возвращаем вставленную запись
    }
    public UUID createAndGetId(OperatorClient operator_client) {
        UUID generatedId = null; // Инициализируем значение ID значением по умолчанию
        String sql = "INSERT INTO operator_clients (foreign_id, operator_id, created_at, updated_at, address_memo, address, total_earned_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, operator_client.getForeign_id());
            statement.setObject(2, UUID.fromString(operator_client.getOperator_id()));

            // Получение текущего времени в формате LocalDateTime
            LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("GMT"));
            // Преобразование времени в Timestamp для SQL
            Timestamp timestamp = Timestamp.valueOf(currentDateTime);

            // Установка временных меток в SQL-запросе
            statement.setTimestamp(3, timestamp);
            statement.setTimestamp(4, timestamp);

            statement.setString(5, operator_client.getAddress_memo());
            statement.setString(6, operator_client.getAddress());
            statement.setLong(7, operator_client.getTotal_earned_amount());

            // Выполнение SQL-запроса на вставку данных
            statement.executeUpdate();

            // Получение сгенерированного ID в формате UUID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = (UUID) generatedKeys.getObject(1); // Получаем сгенерированный ID
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId; // Возвращаем сгенерированный ID
    }
    @Step("Проверить, существует ли пользователь с foreign_id : {foreign_id}")
    public boolean isUserExist(String foreignId) {
        OperatorClient insertedRecord = null;
        String sqlSelect = "SELECT * FROM operator_clients WHERE foreign_id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlSelect)) {

            statement.setString(1, foreignId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Создание объекта оператора на основе выбранных данных
                    insertedRecord = new OperatorClient(
                            resultSet.getString("id"),
                            resultSet.getString("foreign_id"),
                            resultSet.getString("operator_id"),
                            resultSet.getString("created_at"),
                            resultSet.getString("updated_at"),
                            resultSet.getString("address_memo"),
                            resultSet.getString("address"),
                            resultSet.getLong("total_earned_amount")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Проверка на null, чтобы избежать NullPointerException
        return insertedRecord != null && insertedRecord.getForeign_id().equals(foreignId);
    }
    @Step("Получить id пользователя с foreign_id : {foreign_id}")
    public String getIdByForeignId(String foreign_id) {
        OperatorClient insertedRecord = null;
        String sqlSelect = "SELECT * FROM operator_clients WHERE foreign_id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlSelect)) {

            statement.setString(1, foreign_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Создание объекта оператора на основе выбранных данных
                    insertedRecord = new OperatorClient(
                            resultSet.getString("id"),
                            resultSet.getString("foreign_id"),
                            resultSet.getString("operator_id"),
                            resultSet.getString("created_at"),
                            resultSet.getString("updated_at"),
                            resultSet.getString("address_memo"),
                            resultSet.getString("address"),
                            resultSet.getLong("total_earned_amount")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Проверка на null, чтобы избежать NullPointerException
        return insertedRecord.getId();
    }
    @Override
    public OperatorClient getById(String id) {
        return null;
    }

    @Override
    public List<OperatorClient> getAllUsers() {
        return null;
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public List<String> getForeignIds() {
        return null;
    }
}
