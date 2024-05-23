package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public class Pluto_jackpot {
    int id;
    UUID operator_id;
    boolean is_running;
    String name;
    Timestamp start_at;
    Timestamp end_at;
    Timestamp created_at;
    Timestamp updated_at;
    BigDecimal revenue_amount;
    public Pluto_jackpot(){}

    public Pluto_jackpot(int id, UUID operator_id, boolean is_running, String name, Timestamp start_at, Timestamp end_at, Timestamp created_at, Timestamp updated_at, BigDecimal revenue_amount) {
        this.id = id;
        this.operator_id = operator_id;
        this.is_running = is_running;
        this.name = name;
        this.start_at = start_at;
        this.end_at = end_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.revenue_amount = revenue_amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(UUID operator_id) {
        this.operator_id = operator_id;
    }

    public boolean isIs_running() {
        return is_running;
    }

    public void setIs_running(boolean is_running) {
        this.is_running = is_running;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStart_at() {
        return start_at;
    }

    public void setStart_at(Timestamp start_at) {
        this.start_at = start_at;
    }

    public Timestamp getEnd_at() {
        return end_at;
    }

    public void setEnd_at(Timestamp end_at) {
        this.end_at = end_at;
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

    public BigDecimal getRevenue_amount() {
        return revenue_amount;
    }

    public void setRevenue_amount(BigDecimal revenue_amount) {
        this.revenue_amount = revenue_amount;
    }
}
