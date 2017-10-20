package com.acme.web;

import com.acme.account.Account;
import com.acme.account.AccountService;
import com.acme.transaction.Transaction;
import com.acme.transaction.TransactionService;
import io.vertx.core.AbstractVerticle;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GithubAuth;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    final AccountService accountService = AccountService.createProxy(vertx, AccountService.DEFAULT_ADDRESS);
    final TransactionService transactionService = TransactionService.createProxy(vertx, TransactionService.DEFAULT_ADDRESS);
    // the router will map http requests to handlers
    final Router app = Router.router(vertx);
    // enable parsing of http message bodies e.g.: json upload, multipart-form data
    app.post().handler(BodyHandler.create());

    // We need cookies and sessions
    app.route()
      .handler(CookieHandler.create());
    app.route()
      .handler(SessionHandler.create(LocalSessionStore.create(vertx)));
    // Simple auth service which uses a GitHub to authenticate the user
    OAuth2Auth authProvider =
      GithubAuth.create(vertx, System.getenv("CLIENTID"), System.getenv("CLIENTSECRET"));
    // We need a user session handler too to make sure
    // the user is stored in the session between requests
    app.route()
      .handler(UserSessionHandler.create(authProvider));

    // TODO: (REFACTOR #13) create a callback route

    // TODO: (REFACTOR #13) protect all handlers from this point forward

    app.post("/account")
      // TODO: (REFACTOR #9) validate the input body
      // validate the input data
      .handler(ctx -> {
        try {
          // save the parsed input in the request context
          ctx.put("initialBalance", ctx.getBodyAsJson().getInteger("balance", 0));
          // invoke the next handler
          ctx.next();
        } catch (RuntimeException e) {
          ctx.fail(e);
        }
      })
      // create an account, at this moment the input has been validated
      .handler(ctx -> {
        accountService.createAccount(ctx.get("initialBalance"), createAccount -> {
          if (createAccount.failed()) {
            ctx.fail(createAccount.cause());
          } else {
            ctx
              .put("created", createAccount.result())
              .next();
          }
        });
      })
      // TODO: (REFACTOR #9) handle the last as a 202 created response
      ;

    app.get("/account/:id")
      // validate the input
      .handler(MainVerticle::idValidation)
      // invoke the remote service and return the json response
      .handler(ctx -> {
        accountService.getAccount(ctx.get("id"), getAccount -> {
          if (getAccount.failed()) {
            ctx.fail(getAccount.cause());
          } else {
            Account account = getAccount.result();
            // pass the reply to the next handler
            ctx
              .put("reply", account == null ? null : account.toJson())
              .next();
          }
        });
      })
      .handler(MainVerticle::jsonReply);

    app.post("/transaction")
      // validate the input body
      .handler(MainVerticle::jsonBody)
      // validate the input data
      .handler(ctx -> {
        try {
          JsonObject body = ctx.getBodyAsJson();

          String source = body.getString("source");
          String target = body.getString("target");
          int amount = body.getInteger("amount", -1);

          if (source == null || target == null || amount < 0) {
            ctx.fail(400);
          } else {
            // save the parsed input in the request context
            ctx.put("source", source);
            ctx.put("target", target);
            ctx.put("amount", amount);

            // invoke the next handler
            ctx.next();
          }
        } catch (RuntimeException e) {
          ctx.fail(e);
        }
      })
      .handler(ctx -> {
        // TODO: (REFACTOR #10) use the service proxy with the variable initialBalance from the context
        // On success, put the response on a context under the name "created" and handle it
        // to the next handler
      })
      .handler(MainVerticle::created);

    app.get("/transaction/:id")
      // validate the input
      .handler(MainVerticle::idValidation)
      .handler(ctx -> {
        transactionService.getTransaction(ctx.get("id"), getTransaction -> {
          if (getTransaction.failed()) {
            ctx.fail(getTransaction.cause());
          } else {
            Transaction transaction = getTransaction.result();
            // pass the reply to the next handler
            ctx
              .put("reply", transaction == null ? null : transaction.toJson())
              .next();
          }
        });
      })
      .handler(MainVerticle::jsonReply);

    // Serve the static resources
    app.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(app::accept).listen(8080, res -> {
      if (res.failed()) {
        res.cause().printStackTrace();
      } else {
        System.out.println("Server listening at: http://localhost:8080/");
      }
    });
  }

  /**
   * Ensures that there is a HTTP JSON body in the request.
   * Will fail the request with status code 400 (Bad Request) if no body is present
   */
  private static void jsonBody(RoutingContext ctx) {
    try {
      JsonObject body = ctx.getBodyAsJson();
      if (body == null) {
        // no json body
        ctx.fail(400);
      }
      ctx.next();
    } catch (DecodeException e) {
      ctx.fail(e);
    }
  }

  /**
   * Ensures that there is a path param "id" in the request.
   * Will fail the request with status code 400 (Bad Request) if no "id" is present
   */
  private static void idValidation(RoutingContext ctx) {
    String id = ctx.pathParam("id");
    if (id == null) {
      // if the id is not present, return bad request
      ctx.fail(400);
    } else {
      ctx.put("id", id);
      ctx.next();
    }
  }

  /**
   * Will return status code 202 (Created) with the location header pointing to the newly created resource.
   */
  private static void created(RoutingContext ctx) {
    ctx.response()
      .putHeader("Location", ctx.normalisedPath() + "/" + ctx.get("created"))
      .setStatusCode(202)
      .end();
  }

  /**
   * Will return a JSON response with the correct headers or status code 404 (Not Found) if reply is null
   */
  private static void jsonReply(RoutingContext ctx) {
    JsonObject reply = ctx.get("reply");
    if (reply == null) {
      ctx.fail(404);
    } else {
      ctx.response()
        .putHeader("Content-Type", "application/json")
        .end(reply.encodePrettily());
    }
  }
}
