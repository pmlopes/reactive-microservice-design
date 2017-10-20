package acme.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This is the Acme Bank application.
 *
 * The Acme Bank is a virtual bank where bank employees can create accounts, check their state and
 * initiate transactions between accounts.
 *
 * This application is what we will refer to as the monolith.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
