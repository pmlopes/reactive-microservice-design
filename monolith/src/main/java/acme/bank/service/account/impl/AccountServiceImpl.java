package acme.bank.service.account.impl;

import acme.bank.service.account.Account;
import acme.bank.service.account.exceptions.AccountNotFoundException;
import acme.bank.service.account.AccountService;
import acme.bank.service.account.exceptions.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

  @Autowired
  private JdbcTemplate jdbc;

  @Override
  public Account getAccount(String accountId) throws AccountNotFoundException {
    try {
      return jdbc.queryForObject("SELECT * FROM accounts WHERE id = ?", (rs, i) -> {
        Account acc = new Account();
        acc.setId(rs.getString("id"));
        acc.setBalance(rs.getInt("balance"));
        return acc;
      }, accountId);
    } catch (EmptyResultDataAccessException e) {
      throw new AccountNotFoundException(accountId);
    }
  }

  @Override
  public String createAccount(int initialBalance) {
    String id = UUID.randomUUID().toString();
    jdbc.update("INSERT INTO accounts (id, balance) VALUES (?, ?)", id, initialBalance);
    return id;
  }

  @Override
  @Transactional
  public void wireTransfer(String fromAccountId, String toAccountId, int amount)
    throws AccountNotFoundException, InsufficientFundsException {

    Account from = getAccount(fromAccountId);
    Account to = getAccount(toAccountId);

    // verify if the account has enough funds
    if (from.getBalance() < amount) {
      throw new InsufficientFundsException(from.getId(), amount);
    }

    jdbc.update("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, from.getId());
    jdbc.update("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, to.getId());
  }
}
