package acme.bank.service.transaction.impl;

import acme.bank.service.account.exceptions.AccountNotFoundException;
import acme.bank.service.account.AccountService;
import acme.bank.service.account.exceptions.InsufficientFundsException;
import acme.bank.service.transaction.Transaction;
import acme.bank.service.transaction.exceptions.TransactionNotFoundException;
import acme.bank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static acme.bank.service.transaction.Transaction.Status.*;

@Service
public class TransactionServiceImpl implements TransactionService {

  @Autowired
  private JdbcTemplate jdbc;

  @Autowired
  private AccountService accountService;

  @Override
  public Transaction getTransaction(String transactionId) {
    try {
      return jdbc.queryForObject("SELECT * FROM transactions WHERE id = ?", (rs, i) -> {
        Transaction tx = new Transaction();
        tx.setId(rs.getString("id"));
        tx.setSource(rs.getString("source"));
        tx.setTarget(rs.getString("target"));
        tx.setAmount(rs.getInt("amount"));
        tx.setStatus(Transaction.Status.valueOf(rs.getString("status")));
        return tx;
      }, transactionId);
    } catch (EmptyResultDataAccessException e) {
      throw new TransactionNotFoundException(transactionId);
    }
  }

  @Override
  public String createTransaction(String fromAccount, String toAccount, int amount) {
    String id = UUID.randomUUID().toString();

    jdbc.update(
      "INSERT INTO transactions (id, source,  target, amount, status) VALUES (?, ?, ?, ?, ?)",
      id,
      fromAccount,
      toAccount,
      amount,
      PENDING.name());

    Transaction transaction = new Transaction();
    transaction.setId(id);
    transaction.setSource(fromAccount);
    transaction.setTarget(toAccount);
    transaction.setAmount(amount);
    transaction.setStatus(PENDING);

    // start processing the transaction
    processTransaction(transaction);

    return id;
  }

  @Transactional
  void processTransaction(Transaction transaction) {
    try {
      // perform the transfer
      accountService
        .wireTransfer(transaction.getSource(), transaction.getTarget(), transaction.getAmount());

      // update the transaction state
      jdbc.update(
        "UPDATE transactions SET status = ? WHERE id = ?",
        CONFIRMED.name(),
        transaction.getId());

    } catch (AccountNotFoundException e) {
      jdbc.update(
        "UPDATE transactions SET status = ? WHERE id = ?",
        ACCOUNT_NOT_FOUND.name(),
        transaction.getId());

    } catch (InsufficientFundsException e) {
      jdbc.update(
        "UPDATE transactions SET status = ? WHERE id = ?",
        INSUFFICIENT_FUNDS.name(),
        transaction.getId());
    }
  }
}
