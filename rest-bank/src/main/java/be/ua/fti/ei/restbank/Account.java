package be.ua.fti.ei.restbank;

import java.util.concurrent.TimeUnit;

public class Account
{
    private float balance;
    private int accountNumber;
    private boolean lock;

    public Account(int accNumber)
    {
        this.balance = 0;
        this.accountNumber = accNumber;
        this.lock = false;
    }

    public void addMoney(float money)
    {
        balance += money;
    }

    public boolean getMoney(float money) {
        if(lock)
            return false; //Return error because account is in use

        lock = true;
        if(balance - money >= 0)
            balance -= money;

        try{
        Thread.sleep(5000);
        }
        catch (Exception e){}

        lock = false;

        return true;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}