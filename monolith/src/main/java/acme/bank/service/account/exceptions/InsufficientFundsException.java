package acme.bank.service.account.exceptions;

/**
 * There are insufficient funds to perform the transfer
 */
public class InsufficientFundsException extends RuntimeException {

  public InsufficientFundsException(String accountId, int amount) {
    super("InsufficientFunds: [" + accountId + ", " + amount + "]");
  }
}
