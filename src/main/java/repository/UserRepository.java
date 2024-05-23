package repository;
import model.Operator_client;

import java.util.List;

public interface UserRepository {
    Operator_client create(Operator_client operator_client);
    Operator_client getById(String id);
    List<Operator_client> getAllUsers();
    void deleteById(long id);
    List<String> getForeignIds();
}
