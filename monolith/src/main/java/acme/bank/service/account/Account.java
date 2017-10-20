package acme.bank.service.account;

/**
 * In our bank we like to keep things simple. An account is made of 2 fields:
 *
 * * id a unique string
 * * balance the current balance on the account.
 */
public class Account {

  /**
   * the account id
   */
  private String id;

  /**
   * the account balance
   */
  private int balance = 0;

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
