package com.acme.account;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ServiceProxyBuilder;

/**
 * The account service is a service responsible for the following actions:
 *
 * * getAccount - lookup all data related to an account from a database
 * * createAccount - create a new account on the database
 * * wireTransfer - update 2 account balances by moving funds across them
 */
@VertxGen
@ProxyGen
public interface AccountService {

  String DEFAULT_ADDRESS = "com.acme.account.service";

  /**
    * Method called to create a proxy (to consume the service).
    *
    * @param vertx   vert.x
    * @param address the address on the event bus where the service is served.
    * @return the proxy
    */
  static AccountService createProxy(Vertx vertx, String address) {
    return new ServiceProxyBuilder(vertx)
      .setAddress(address)
      .build(AccountService.class);
  }

  /**
   * Get an account from a database
   *
   * @param accountId the accountId to lookup
   */
  void getAccount(String accountId, Handler<AsyncResult<Account>> handler);

  /**
   * Create an account with the given initial balance.
   * @param initialBalance an initial balance
   */
  void createAccount(int initialBalance, Handler<AsyncResult<String>> handler);

  /**
   * Transfer funds between accounts.
   *
   * @param fromAccountId the source account id
   * @param toAccountId the destination account id
   * @param amount the amount to transfer
   */
  void wireTransfer(String fromAccountId, String toAccountId, int amount, Handler<AsyncResult<Void>> handler);
}
