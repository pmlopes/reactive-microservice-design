package com.acme.transaction;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ServiceProxyBuilder;

/**
 * The Transaction Service is responsible for:
 *
 * * looking up a transaction from the database
 * * orchestrate the process of transfer funds between accounts while keeping a record of all actions
 */
@VertxGen
@ProxyGen
public interface TransactionService {

  String DEFAULT_ADDRESS = "com.acme.transaction.service";

  /**
    * Method called to create a proxy (to consume the service).
    *
    * @param vertx   vert.x
    * @param address the address on the event bus where the service is served.
    * @return the proxy
    */
  static TransactionService createProxy(Vertx vertx, String address) {
    return new ServiceProxyBuilder(vertx)
      .setAddress(address)
      .build(TransactionService.class);
  }

  /**
   * Gets all information related to a Transaction given an id.
   * @param transactionId a Transaction id
   * @return the transaction
   */
  void getTransaction(String transactionId, Handler<AsyncResult<Transaction>> handler);

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
  void createTransaction(String fromAccount, String toAccount, int amount, Handler<AsyncResult<String>> handler);
}
