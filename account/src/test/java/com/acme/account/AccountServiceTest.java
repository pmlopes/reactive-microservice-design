package com.acme.account;

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

/**
 * The test assumes you're running the database server in the background, please ensure that:
 *
 * docker-componse up hsqldb
 *
 * was executed.
 */
@RunWith(VertxUnitRunner.class)
public class AccountServiceTest {

  // keep a vertx instance to control deployments
  private Vertx vertx;

  @Before
  public void setUp(TestContext context) {
    // create a vertx instance
    vertx = Vertx.vertx();
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
    AccountService service = AccountService.createProxy(vertx, AccountService.DEFAULT_ADDRESS);
    final Async test = context.async();
    service.createAccount(0, createAccount -> {
      context.assertFalse(createAccount.failed());
      context.assertNotNull(createAccount.result());
      test.complete();
    });
  }

  @Test
  public void testGetAccount(TestContext context) {
    AccountService service = AccountService.createProxy(vertx, AccountService.DEFAULT_ADDRESS);
    final Async test = context.async();
    service.createAccount(10, createAccount -> {
      context.assertFalse(createAccount.failed());
      context.assertNotNull(createAccount.result());

      service.getAccount(createAccount.result(), getAccount -> {
        context.assertFalse(getAccount.failed());
        context.assertNotNull(getAccount.result());

        context.assertEquals(10, getAccount.result().getBalance());
        context.assertEquals(createAccount.result(), getAccount.result().getId());

        test.complete();
      });
    });
  }

  @Test
  public void testWireTransfer(TestContext context) {
    AccountService service = AccountService.createProxy(vertx, AccountService.DEFAULT_ADDRESS);
    final Async test = context.async();

    service.createAccount(100, createSourceAccount -> {
      context.assertFalse(createSourceAccount.failed());
      context.assertNotNull(createSourceAccount.result());

      service.createAccount(0, createTargeAccount -> {
        context.assertFalse(createTargeAccount.failed());
        context.assertNotNull(createTargeAccount.result());

        service.wireTransfer(createSourceAccount.result(), createTargeAccount.result(), 25, wireTransfer -> {
          context.assertFalse(createTargeAccount.failed());
          test.complete();
        });
      });
    });
  }
}
