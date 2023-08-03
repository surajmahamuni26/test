package com.globomantics.rewards;

import com.globomantics.rewards.models.Customer;
import com.globomantics.rewards.models.Transaction;
import com.globomantics.rewards.repository.CustomerRepository;
import com.globomantics.rewards.repository.TransactionHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class RewardsApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(RewardsApplication.class);

  @Bean
  public ApplicationRunner runner(TransactionHistory transactionHistoryRepository,
                                  CustomerRepository customerRepository) {
    return args -> {
      Transaction transaction1 = new Transaction(LocalDateTime.now(), "JANUARY", 1000d);
      Transaction transaction2 = new Transaction(LocalDateTime.now(), "FEBRUARY", 10d);
      Transaction transaction3 = new Transaction(LocalDateTime.now(), "MARCH", 7580d);
      Transaction transaction4 = new Transaction(LocalDateTime.now(), "JANUARY", 10d);
      Transaction transaction5 = new Transaction(LocalDateTime.now(), "FEBRUARY", 10d);
      Transaction transaction6 = new Transaction(LocalDateTime.now(), "JANUARY", 300d);

      Customer customer1 = new Customer(1L, "Naresh");
      Customer customer2 = new Customer(2L, "Suresh");
      Customer customer3 = new Customer(3L, "Ramesh");

      customer1.setTransactions(new ArrayList<>());
      customer1.getTransactions().add(transaction1);
      customer1.getTransactions().add(transaction2);
      customer1.getTransactions().add(transaction3);

      customer2.setTransactions(new ArrayList<>());
      customer2.getTransactions().add(transaction4);
      customer2.getTransactions().add(transaction5);

      customer3.setTransactions(new ArrayList<>());
      customer3.getTransactions().add(transaction6);

      transaction1.setCustomer(customer1);
      transaction2.setCustomer(customer1);
      transaction3.setCustomer(customer1);

      transaction4.setCustomer(customer2);
      transaction5.setCustomer(customer2);

      transaction6.setCustomer(customer3);

      transactionHistoryRepository.saveAll(
          Arrays.asList(
              transaction1, transaction2, transaction3, transaction4, transaction5, transaction6));

      customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));

      customerRepository.findAll().forEach(customer -> LOGGER.info(customer.toString()));
      transactionHistoryRepository.findAll().forEach(transaction -> LOGGER.info(transaction.toString()));
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(RewardsApplication.class, args);
  }
}
