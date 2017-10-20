package com.acme.transaction.impl;

import com.acme.transaction.Transaction;
import com.acme.transaction.TransactionService;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.serviceproxy.ServiceException;

import java.util.UUID;

import static com.acme.transaction.Transaction.Status.*;

public class TransactionServiceImpl implements TransactionService {

  private final JDBCClient db;
  // TODO: (REFACTOR #8) add a private variable of type AccountService

  public TransactionServiceImpl(Vertx vertx, JsonObject config) {
    // initialization...
    db = JDBCClient.createShared(
      vertx,
      new JsonObject()
        .put("url", "jdbc:hsqldb:hsql://" + config.getString("DBHOST") + "/" + config.getString("DBNAME"))
        .put("driver_class", "org.hsqldb.jdbc.JDBCDriver")
        .put("user", config.getString("DBUSER"))
        .put("password", config.getString("DBPASSWORD"))
        .put("max_pool_size", 30));

    // TODO: (REFACTOR #8) create a proxy using the default address (Tip! use the interface)
  }

  @Override
  public void getTransaction(String transactionId, Handler<AsyncResult<Transaction>> handler) {
    db.querySingleWithParams("SELECT id, source, target, amount, status  FROM transactions WHERE id = ?", new JsonArray().add(transactionId), query -> {
      if (query.failed()) {
        handler.handle(Future.failedFuture(query.cause()));
        return;
      }

      final JsonArray row = query.result();

      if (row == null) {
        handler.handle(ServiceException.fail(404, "Transaction Not Found: " + transactionId));
        return;
      }

      final Transaction transaction = new Transaction();
      transaction.setId(row.getString(0));
      transaction.setSource(row.getString(1));
      transaction.setTarget(row.getString(2));
      transaction.setAmount(row.getInteger(3));
      transaction.setStatus(Transaction.Status.valueOf(row.getString(4)));

      handler.handle(Future.succeededFuture(transaction));
    });
  }

  @Override
  public void createTransaction(String fromAccount, String toAccount, int amount, Handler<AsyncResult<String>> handler) {
    String id = UUID.randomUUID().toString();

    db.updateWithParams(
      "INSERT INTO transactions (id, source, target, amount, status) VALUES (?, ?, ?, ?, ?)",
      new JsonArray().add(id).add(fromAccount).add(toAccount).add(amount).add(PENDING),
      update -> {
        if (update.failed()) {
          handler.handle(Future.failedFuture(update.cause()));
          return;
        }

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setSource(fromAccount);
        transaction.setTarget(toAccount);
        transaction.setAmount(amount);
        transaction.setStatus(PENDING);

        // start processing the transaction
        processTransaction(transaction, processTransaction -> {
          if (processTransaction.failed()) {
            handler.handle(ServiceException.fail(503, "sorry, service unavailable"));
          } else {
            handler.handle(Future.succeededFuture(id));
          }
        });
      });
  }

  private void processTransaction(Transaction transaction, Handler<AsyncResult<UpdateResult>> done) {
//    // TODO: (REFACTOR #8) uncomment me!
//    accountService.wireTransfer(transaction.getSource(), transaction.getTarget(), transaction.getAmount(), wireTransfer -> {
//      if (wireTransfer.failed()) {
//        if (wireTransfer.cause() instanceof ServiceException) {
//          switch (((ServiceException) wireTransfer.cause()).failureCode()) {
//            case 404:
//              db.updateWithParams("UPDATE transactions SET status = ? WHERE id = ?", new JsonArray().add(ACCOUNT_NOT_FOUND).add(transaction.getId()), done);
//              return;
//            case 412:
//              db.updateWithParams("UPDATE transactions SET status = ? WHERE id = ?", new JsonArray().add(INSUFFICIENT_FUNDS).add(transaction.getId()), done);
//              return;
//          }
//        }
//        done.handle(Future.failedFuture(wireTransfer.cause()));
//      } else {
//        db.updateWithParams("UPDATE transactions SET status = ? WHERE id = ?", new JsonArray().add(CONFIRMED).add(transaction.getId()), done);
//      }
//    });
  }

  public void close() {
    // clean up...
  }
}
