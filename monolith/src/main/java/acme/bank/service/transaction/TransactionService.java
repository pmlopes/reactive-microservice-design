package acme.bank.service.transaction;

import acme.bank.service.transaction.exceptions.TransactionNotFoundException;

/**
 * The Transaction Service is responsible for:
 *
 * * looking up a transaction from the database
 * * orchestrate the process of transfer funds between accounts while keeping a record of all actions
 */
public interface TransactionService {

  /**
   * Gets all information related to a Transaction given an id.
   * @param transactionId a Transaction id
   * @return the transaction
   * @throws TransactionNotFoundException when the given id is not present on the database
   */
  Transaction getTransaction(String transactionId) throws TransactionNotFoundException;

  /**
   * Create a transaction is an orchestration process.
   *
   * * register a transaction start on the database
   * * delegate to the wireTransfer from the account service to perform the transfer
   * * update the transaction given the response from the account service
   *
   * @param fromAccount the source account id
   * @param toAccount the destination account id
   * @param amount the amount
   * @return the new transaction id
   */
  String createTransaction(String fromAccount, String toAccount, int amount);
}
