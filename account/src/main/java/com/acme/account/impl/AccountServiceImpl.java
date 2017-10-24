package com.acme.account.impl;

import com.acme.account.Account;
import com.acme.account.AccountService;

import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.serviceproxy.ServiceException;

import java.util.UUID;

public class AccountServiceImpl implements AccountService {

  private final JDBCClient db;

  public AccountServiceImpl(Vertx vertx, JsonObject config) {
    // initialization...
    db = JDBCClient.createShared(
      vertx,
      new JsonObject()
        .put("url", "jdbc:hsqldb:hsql://" + config.getString("DBHOST") + "/" + config.getString("DBNAME"))
        .put("driver_class", "org.hsqldb.jdbc.JDBCDriver")
        .put("user", config.getString("DBUSER"))
        .put("password", config.getString("DBPASSWORD"))
        .put("max_pool_size", 30));
  }

  @Override
  public void getAccount(String accountId, Handler<AsyncResult<Account>> handler) {
    db.querySingleWithParams("SELECT id, balance FROM accounts WHERE id = ?", new JsonArray().add(accountId), query -> {
      if (query.failed()) {
        handler.handle(Future.failedFuture(query.cause()));
        return;
      }

      final JsonArray row = query.result();

      if (row == null) {
        handler.handle(ServiceException.fail(404, "Account Not Found: " + accountId));
        return;
      }

      final Account account = new Account();
      account.setId(row.getString(0));
      account.setBalance(row.getInteger(1));

      handler.handle(Future.succeededFuture(account));
    });
  }

  @Override
  public void createAccount(int initialBalance, Handler<AsyncResult<String>> handler) {
    String id = UUID.randomUUID().toString();

    db.updateWithParams("INSERT INTO accounts (id, balance) VALUES (?, ?)", new JsonArray().add(id).add(initialBalance), update -> {
      if (update.failed()) {
        handler.handle(Future.failedFuture(update.cause()));
        return;
      }

      handler.handle(Future.succeededFuture(id));
    });
  }

  private void startTransaction(Handler<AsyncResult<SQLConnection>> handler) {
    db.getConnection(getConnection -> {
      if (getConnection.failed()) {
        handler.handle(Future.failedFuture(getConnection.cause()));
      } else {
        final SQLConnection conn = getConnection.result();

        conn.setAutoCommit(false, setAutoCommit -> {
          if (setAutoCommit.failed()) {
            handler.handle(Future.failedFuture(setAutoCommit.cause()));
          } else {
            handler.handle(Future.succeededFuture(conn));
          }
        });
      }
    });
  }

  private void rollback(SQLConnection conn, Handler<AsyncResult<Void>> handler) {
    conn.rollback(rollback -> {
      if (rollback.failed()) {
        handler.handle(Future.failedFuture(rollback.cause()));
      } else {
        conn.close(close -> {
          if (close.failed()) {
            handler.handle(Future.failedFuture(close.cause()));
          } else {
            handler.handle(Future.succeededFuture());
          }
        });
      }
    });
  }

  private void commit(SQLConnection conn, Handler<AsyncResult<Void>> handler) {
    conn.commit(commit -> {
      if (commit.failed()) {
        handler.handle(Future.failedFuture(commit.cause()));
      } else {
        conn.close(close -> {
          if (close.failed()) {
            handler.handle(Future.failedFuture(close.cause()));
          } else {
            handler.handle(Future.succeededFuture());
          }
        });
      }
    });
  }

  private void rollbackAndReturn(SQLConnection conn, int code, String message, Handler<AsyncResult<Void>> handler) {
    rollback(conn, rollback -> {
      if (rollback.failed()) {
        handler.handle(Future.failedFuture(rollback.cause()));
      } else {
        handler.handle(ServiceException.fail(code, message));
      }
    });
  }

  private void rollbackAndReturn(SQLConnection conn, Throwable cause, Handler<AsyncResult<Void>> handler) {
    rollback(conn, rollback -> {
      if (rollback.failed()) {
        handler.handle(Future.failedFuture(rollback.cause()));
      } else {
        handler.handle(Future.failedFuture(cause));
      }
    });
  }

  @Override
  public void wireTransfer(String fromAccountId, String toAccountId, int amount, Handler<AsyncResult<Void>> handler) {
    startTransaction(startTransaction -> {
      if (startTransaction.failed()) {
        handler.handle(Future.failedFuture(startTransaction.cause()));
        return;
      }

      final SQLConnection conn = startTransaction.result();

      Future<JsonArray> getFromAccount = Future.future(f -> conn.querySingleWithParams("SELECT id, balance FROM accounts WHERE id = ?", new JsonArray().add(fromAccountId), f.completer()));
      Future<JsonArray> getToAccount = Future.future(f -> conn.querySingleWithParams("SELECT id, balance FROM accounts WHERE id = ?", new JsonArray().add(toAccountId), f.completer()));

      CompositeFuture.all(getFromAccount, getToAccount).setHandler(ar -> {
        if (ar.failed()) {
          rollbackAndReturn(conn, ar.cause(), handler);
          return;
        }

        JsonArray row1 = getFromAccount.result();
        JsonArray row2 = getToAccount.result();

        if (row1 == null || row2 == null) {
          rollbackAndReturn(conn, 404, "Account Not Found!", handler);
          return;
        }

        // verify if the account has enough funds
        if (row1.getInteger(1) < amount) {
          rollbackAndReturn(conn, 412, "Insufficient Funds: " + fromAccountId + " [" + amount + "]", handler);
          return;
        }

        Future<UpdateResult> updateFromAccount = Future.future(f -> conn.updateWithParams("UPDATE accounts SET balance = balance - ? WHERE id = ?", new JsonArray().add(amount).add(fromAccountId), f.completer()));
        Future<UpdateResult> updateToAccount = Future.future(f -> conn.updateWithParams("UPDATE accounts SET balance = balance + ? WHERE id = ?", new JsonArray().add(amount).add(toAccountId), f.completer()));

        CompositeFuture.all(updateFromAccount, updateToAccount).setHandler(ar2 -> {
          if (ar2.failed()) {
            rollbackAndReturn(conn, ar2.cause(), handler);
            return;
          }

          commit(conn, handler);
        });
      });
    });
  }

  public void close() {
    // clean up...
  }
}
