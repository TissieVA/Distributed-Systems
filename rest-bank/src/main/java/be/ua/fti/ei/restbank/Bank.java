package be.ua.fti.ei.restbank;

import java.util.HashMap;

public class Bank
{
    private HashMap<Integer, Account> database;

    public Bank()
    {
        this.database = new HashMap<>();
        this.database.put(0, new Account(0));
        this.database.put(1, new Account(1));
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
