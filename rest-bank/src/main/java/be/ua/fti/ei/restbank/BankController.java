package be.ua.fti.ei.restbank;

import org.springframework.web.bind.annotation.*;

@RestController
public class BankController {

    @GetMapping("/balance/{id}")
    float getBalance(@PathVariable Integer id)
    {
        return Bank.getInstance().getAccount(id).getBalance();
    }


    @PostMapping("/money/add")
    float addMoney(@RequestBody Transaction tr)
    {
        Bank.getInstance().getAccount(tr.getAccountId()).addMoney(tr.getMoney());
        return Bank.getInstance().getAccount(tr.getAccountId()).getBalance();
    }

    @PostMapping("/money/withdraw")
    String withdrawMoney(@RequestBody Transaction tr)
    {
        if(Bank.getInstance().getAccount(tr.getAccountId()).getMoney(tr.getMoney()))
            return "OK";
        else
            return "Account in use, try again later";
    }

}
