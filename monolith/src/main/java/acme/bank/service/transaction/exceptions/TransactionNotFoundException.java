package acme.bank.service.transaction.exceptions;

/**
 * Represents a missing transaction exception
 */
public class TransactionNotFoundException extends RuntimeException {
  public TransactionNotFoundException(String transactionId) {
    super("TransactionNotFound: [" + transactionId + "]");
  }
}
