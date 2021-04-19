package be.ua.fti.ei.restbank;

public class SharedAccount extends Account
{

    Account acc;

    public SharedAccount(int accNumber, Account acc) {
        super(accNumber);
        this.acc = acc;
    }

    @Override
    public void addMoney(float money) {
        acc.addMoney(money);
    }

    @Override
    public boolean getMoney(float money) {
        return acc.getMoney(money);
    }

    @Override
    public float getBalance() {
        return acc.getBalance();
    }

    @Override
    public void setBalance(float balance) {
        acc.setBalance(balance);
    }
}
