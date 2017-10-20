package acme.bank.controller;

import acme.bank.service.transaction.Transaction;
import acme.bank.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET)
  public @ResponseBody
  Transaction getTransaction(HttpServletResponse response, @PathVariable String transactionId)
    throws IOException {

    if (transactionId == null) {
      response.sendError(400);
      return null;
    }

    return transactionService.getTransaction(transactionId);
  }

  @RequestMapping(value = "/transaction", method = RequestMethod.POST)
  public @ResponseBody
  void createTransaction(HttpServletResponse response, @RequestBody Transaction transaction)
    throws IOException {

    if (transaction == null) {
      response.sendError(400);
      return;
    }

    String transactionId = transactionService.createTransaction(
      transaction.getSource(),
      transaction.getTarget(),
      transaction.getAmount());

    response.addHeader("Location", "/transaction/" + transactionId);
    response.sendError(202);
  }
}
