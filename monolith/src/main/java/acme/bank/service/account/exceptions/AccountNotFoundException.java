package acme.bank.service.account.exceptions;

/**
 * Account does not exist
 */
public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(String accountId) {
    super("AccountNotFound: [" + accountId + "]");
  }
}
