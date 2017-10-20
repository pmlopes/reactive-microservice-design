package com.acme.account;

import com.acme.account.impl.AccountServiceImpl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;

public class MainVerticle extends AbstractVerticle {

  private AccountServiceImpl service;

  @Override
  public void start(Future<Void> future) throws Exception {
    // TODO: (REFACTOR #12) create a config retriever

    // TODO: (REFACTOR #12) use the getConfig method to load the config and pass to the service
    service = new AccountServiceImpl(vertx, config());
    new ServiceBinder(vertx)
      .setAddress(AccountService.DEFAULT_ADDRESS)
      .register(AccountService.class, service);

    future.complete();
  }

  @Override
  public void stop() throws Exception {
    service.close();
  }
}
