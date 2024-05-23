package model;

import lombok.Data;

@Data
public class Operator_client {
    String id;
    String foreign_id;
    String operator_id;
    String created_at;
    String updated_at;
    String address;
    String address_memo;
    long total_earned_amount;

    public Operator_client(String id, String foreign_id, String operator_id, String created_at, String updated_at, String address, String address_memo, long total_earned_amount) {
        this.id = id;
        this.foreign_id = foreign_id;
        this.operator_id = operator_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.address = address;
        this.address_memo = address_memo;
        this.total_earned_amount = total_earned_amount;
    }
    public Operator_client() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(String foreign_id) {
        this.foreign_id = foreign_id;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_memo() {
        return address_memo;
    }

    public void setAddress_memo(String address_memo) {
        this.address_memo = address_memo;
    }

    public long getTotal_earned_amount() {
        return total_earned_amount;
    }

    public void setTotal_earned_amount(long total_earned_amount) {
        this.total_earned_amount = total_earned_amount;
    }

}
