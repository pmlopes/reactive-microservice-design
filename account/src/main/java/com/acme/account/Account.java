package com.acme.account;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * In our bank we like to keep things simple. An account is made of 2 fields:
 *
 * * id a unique string
 * * balance the current balance on the account.
 */
@DataObject(generateConverter = true)
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
    AccountConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    final JsonObject json = new JsonObject();
    AccountConverter.toJson(this, json);
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
