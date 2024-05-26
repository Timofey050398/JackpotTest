package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public class Pluto_withdrawal_journal {
    UUID id;
    String hash;
    int status_id;
    BigDecimal amount;
    int transaction_fee;
    UUID user_id;
    UUID operator_id;
    String user_address;
    String operator_address;
    UUID request_id;
    Timestamp created_at;
    Timestamp updated_at;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTransaction_fee() {
        return transaction_fee;
    }

    public void setTransaction_fee(int transaction_fee) {
        this.transaction_fee = transaction_fee;
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

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getOperator_address() {
        return operator_address;
    }

    public void setOperator_address(String operator_address) {
        this.operator_address = operator_address;
    }

    public UUID getRequest_id() {
        return request_id;
    }

    public void setRequest_id(UUID request_id) {
        this.request_id = request_id;
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

    public Pluto_withdrawal_journal(UUID id, String hash, int status_id, BigDecimal amount, int transaction_fee, UUID user_id, UUID operator_id, String user_address, String operator_address, UUID request_id, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.hash = hash;
        this.status_id = status_id;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.user_id = user_id;
        this.operator_id = operator_id;
        this.user_address = user_address;
        this.operator_address = operator_address;
        this.request_id = request_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
