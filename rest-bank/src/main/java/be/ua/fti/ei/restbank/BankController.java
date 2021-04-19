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

}
