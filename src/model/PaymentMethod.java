package model;
public class PaymentMethod {
    private int amount;
    private int password;
    private String cardNumber;
    public PaymentMethod(int amount) {
        this.amount = amount;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getCardNumber() {
        return cardNumber;
    }
    public int getPassword() {
        return password;
    }
    public void setPassword(int password) {
        this.password = password;
    }
}
