package model;



import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public class Pluto_user_journal {
    int id;
    UUID user_id;
    UUID operator_id;
    int operation_type;
    BigDecimal amount;
    Timestamp created_at;
    Timestamp updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public UUID getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(UUID operator_id) {
        this.operator_id = operator_id;
    }

    public int getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(int operation_type) {
        this.operation_type = operation_type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Pluto_user_journal(int id, UUID user_id, UUID operator_id, int operation_type, BigDecimal amount, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.operator_id = operator_id;
        this.operation_type = operation_type;
        this.amount = amount;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

}