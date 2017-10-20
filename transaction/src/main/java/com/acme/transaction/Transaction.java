package com.acme.transaction;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * A Transaction represents a transfer between accounts.
 */
@DataObject(generateConverter = true)
public class Transaction {

  /**
   * A transaction can be in one of this states
   */
  public enum Status {
    PENDING,
    CONFIRMED,
    INSUFFICIENT_FUNDS,
    ACCOUNT_NOT_FOUND
  }

  /**
   * The transaction id
   */
  private String id;

  /**
   * The source account id
   */
  private String source;

  /**
   * The target account id
   */
  private String target;

  /**
   * The amount to transfer
   */
  private Integer amount;

  /**
   * the current status
   */
  private Status status;

  public Transaction() {

  }

  public Transaction(JsonObject json) {
    TransactionConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    final JsonObject json = new JsonObject();
    TransactionConverter.toJson(this, json);
    return json;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
