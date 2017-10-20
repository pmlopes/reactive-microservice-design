package acme.bank.controller;

import acme.bank.service.account.Account;
import acme.bank.service.account.exceptions.AccountNotFoundException;
import acme.bank.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AccountController {

  @Autowired
  private AccountService accountService;

  @RequestMapping(value = "/account/{accountId}", method = RequestMethod.GET)
  public @ResponseBody
  Account getAccount(HttpServletResponse response, @PathVariable String accountId) throws IOException {
    if (accountId == null) {
      response.sendError(400);
      return null;
    }

    try {
      return accountService.getAccount(accountId);
    } catch (AccountNotFoundException e) {
      response.sendError(404);
      return null;
    }
  }

  @RequestMapping(value = "/account", method = RequestMethod.POST)
  public @ResponseBody
  void createAccount(HttpServletResponse response, @RequestBody Account account) throws IOException {
    if (account == null) {
      response.sendError(400);
      return;
    }

    String accountId = accountService.createAccount(account.getBalance());

    response.addHeader("Location", "/account/" + accountId);
    response.setStatus(202);
  }
}
