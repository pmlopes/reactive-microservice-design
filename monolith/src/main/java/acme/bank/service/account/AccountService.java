package acme.bank.service.account;

import acme.bank.service.account.exceptions.AccountNotFoundException;
import acme.bank.service.account.exceptions.InsufficientFundsException;

/**
 * The account service is a service responsible for the following actions:
 *
 * * getAccount - lookup all data related to an account from a database
 * * createAccount - create a new account on the database
 * * wireTransfer - update 2 account balances by moving funds across them
 */
public interface AccountService {

  /**
   * Get an account from a database
   *
   * @param accountId the accountId to lookup
   * @return the account
   * @throws AccountNotFoundException when the accountId is not present on the database
   */
  Account getAccount(String accountId) throws AccountNotFoundException;

  /**
   * Create an account with the given initial balance.
   * @param initialBalance an initial balance
   * @return the newly generated account id
   */
  String createAccount(int initialBalance);

  /**
   * Transfer funds between accounts.
   *
   * @param fromAccountId the source account id
   * @param toAccountId the destination account id
   * @param amount the amount to transfer
   * @throws AccountNotFoundException when either the fromAccountId or toAccountId do not exist
   * @throws InsufficientFundsException when the fromAccount does not have enough funds
   */
  void wireTransfer(String fromAccountId, String toAccountId, int amount)
    throws AccountNotFoundException, InsufficientFundsException;
}
