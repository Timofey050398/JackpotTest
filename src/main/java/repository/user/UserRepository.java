package repository.user;
import model.OperatorClient;

import java.util.List;

public interface UserRepository {
    OperatorClient create(OperatorClient operator_client);
    OperatorClient getById(String id);
    List<OperatorClient> getAllUsers();
    void deleteById(long id);
    List<String> getForeignIds();
}
