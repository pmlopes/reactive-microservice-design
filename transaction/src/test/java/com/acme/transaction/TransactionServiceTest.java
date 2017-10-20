package com.acme.transaction;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(VertxUnitRunner.class)
public class TransactionServiceTest {

  // keep a vertx instance to control deployments
  private Vertx vertx;

  @Before
  public void setUp(TestContext context) {
    // create a vertx instance
    vertx = Vertx.vertx();

    // TODO: (REFACTOR #8) mock the account service
    // create an eventbus consumer on the default address
    // always reply with a empty JsonObject response

    // deploy the current verticle
    vertx.deployVerticle(
      new MainVerticle(),
      new DeploymentOptions()
        // provide custom configuration
        .setConfig(new JsonObject()
          .put("DBUSER", "SA")
          .put("DBPASSWORD", "")
          .put("DBHOST", "localhost:9001")
          .put("DBNAME", "monolith")),
      context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    // by closing the vertx instance we close all related services
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testCreateAccount(TestContext context) {
    TransactionService service = TransactionService.createProxy(vertx, TransactionService.DEFAULT_ADDRESS);
    final Async test = context.async();
    service.createTransaction("fromAccount", "toAccount", 10, createTransaction -> {
      context.assertFalse(createTransaction.failed());
      context.assertNotNull(createTransaction.result());
      test.complete();
    });
  }
}
