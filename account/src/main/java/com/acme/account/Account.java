package com.acme.account;

import io.vertx.core.json.JsonObject;

/**
 * In our bank we like to keep things simple. An account is made of 2 fields:
 *
 * * id a unique string
 * * balance the current balance on the account.
 */
// TODO: (REFACTOR #3) add the missing annotations for codegen
public class Account {

  /**
   * the account id
   */
  private String id;

  /**
   * the account balance
   */
  private int balance = 0;

  public Account() {
  }

  public Account(JsonObject json) {
    // TODO: (REFACTOR #3) convert from json to Account (Tip! codegen will help)
  }

  public JsonObject toJson() {
    final JsonObject json = new JsonObject();
    // TODO: (REFACTOR #3) convert from Account to json (Tip! codegen will help)
    return json;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }
}
