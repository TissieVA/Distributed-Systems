package be.ua.fti.ei.restbank;

import java.util.HashMap;

public class Bank
{
    private HashMap<Integer, Account> database;

    public Bank()
    {
        this.database = new HashMap<>();
        this.database.put(0, new Account(0));

        Account ac1 = new Account(1);
        this.database.put(1, ac1);
        this.database.put(2, new SharedAccount(2,ac1));
    }

    public Account getAccount(int id)
    {
        return this.database.get(id);
    }

    private static Bank instance;
    public static Bank getInstance()
    {
        if(instance == null)
            instance = new Bank();

        return instance;
    }
}
