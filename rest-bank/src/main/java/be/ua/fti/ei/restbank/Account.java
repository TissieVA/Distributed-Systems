package be.ua.fti.ei.restbank;

public class Account
{
    private float balance;
    private int accountNumber;

    public Account(int accNumber)
    {
        this.balance = 0;
        this.accountNumber = accNumber;
    }

    public void addMoney(float money)
    {
        balance += money;
    }

    public void getMoney(float money)
    {
        if(balance - money >= 0)
            balance -= money;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
