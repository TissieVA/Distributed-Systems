package be.ua.fti.ei.restbank;


public class Transaction
{
    private int accountId;
    private float money;

    public Transaction(){}

    public Transaction(int accountId, float money) {
        this.accountId = accountId;
        this.money = money;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
