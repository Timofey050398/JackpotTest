package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public class Pluto_jackpot_participants {
    int id;
    UUID user_id;
    int jackpot_id;
    UUID operator_id;
    int tickets;
    BigDecimal locked_amount;
    int place;
    BigDecimal  revenue_amount;
    Timestamp created_at;
    Timestamp updated_at;
    public Pluto_jackpot_participants(){}

    public Pluto_jackpot_participants(int id, UUID user_id, int jackpot_id, UUID operator_id, int tickets, BigDecimal locked_amount, int place, BigDecimal revenue_amount, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.jackpot_id = jackpot_id;
        this.operator_id = operator_id;
        this.tickets = tickets;
        this.locked_amount = locked_amount;
        this.place = place;
        this.revenue_amount = revenue_amount;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

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

    public int getJackpot_id() {
        return jackpot_id;
    }

    public void setJackpot_id(int jackpot_id) {
        this.jackpot_id = jackpot_id;
    }

    public UUID getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(UUID operator_id) {
        this.operator_id = operator_id;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public BigDecimal getLocked_amount() {
        return locked_amount;
    }

    public void setLocked_amount(BigDecimal locked_amount) {
        this.locked_amount = locked_amount;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public BigDecimal getRevenue_amount() {
        return revenue_amount;
    }

    public void setRevenue_amount(BigDecimal revenue_amount) {
        this.revenue_amount = revenue_amount;
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
}
