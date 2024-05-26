package bodyclasses.request;

public class Withdrawal {
    private  String address;
    private  String userId;
    private  String amount;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Withdrawal(String userId, String amount, String address) {
        this.userId = userId;
        this.amount = amount;
        this.address = address;
    }
}
