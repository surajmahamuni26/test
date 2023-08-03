package com.globomantics.rewards.service;

import com.globomantics.rewards.models.Customer;
import com.globomantics.rewards.models.RewardsSummary;
import com.globomantics.rewards.models.Transaction;
import com.globomantics.rewards.models.exceptions.BadRequest;
import com.globomantics.rewards.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.lang.Math.floor;
import static java.util.stream.Collectors.toList;

@Service
public class RewardsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RewardsService.class);
  private static final int MULTIPLIER = 2;
  private static List<String> WHITE_LIST = Arrays.asList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
          "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER", "TOTAL");

  private final CustomerRepository repository;

  public RewardsService(CustomerRepository repository) {
    this.repository = repository;
  }

  public RewardsSummary getRewardPoints(Long customerId, String monthOfPurchase) throws BadRequest {
    String transactionMonth = monthOfPurchase.toUpperCase(Locale.ROOT);
    List<Transaction> customerTransactions = getTransactionsForRewardCalculation(customerId, transactionMonth);

    double customerPurchaseValue =
        customerTransactions.stream().mapToDouble(Transaction::getPurchaseValue).sum();

    double rewardPoints = get2XRewards(customerPurchaseValue) + get1XRewards(customerPurchaseValue);
    return new RewardsSummary(customerId, rewardPoints);
  }

  private double get1XRewards(double customerPurchaseValue) {
    return customerPurchaseValue >= 100
        ? 50
        : (customerPurchaseValue > 50 ? (customerPurchaseValue - 50) : 0d);
  }

  private double get2XRewards(double customerPurchaseValue) {
    if (customerPurchaseValue <= 100) {
      return 0d;
    }

    double purchaseValue = Double.parseDouble(String.format("%.0f", customerPurchaseValue));

    double multiple =  floor(purchaseValue / 100);
    double remainder = floor(purchaseValue % 100);
    return ((multiple - 1) * 100 * MULTIPLIER) + (remainder * MULTIPLIER);
  }

  private List<Transaction> getTransactionsForRewardCalculation(Long customerId, String transactionMonth)
          throws BadRequest {

    Optional<Customer> customer = repository.findById(customerId);

    if(!customer.isPresent()) {
      throw new BadRequest(String.format("Customer with id: %s does NOT exist", customerId));
    }

    if(!WHITE_LIST.contains(transactionMonth)) {
      throw new BadRequest(String.format("monthOfPurchase: %s is NOT acceptable, " +
              "refer WHITE LIST in ReadMe.md file for acceptable values", transactionMonth));
    }

    List<Transaction> customerTransactions = customer.get().getTransactions();
    LOGGER.info(String.format("%d transactions are made by customer %s in %s",
            customerTransactions.size(), customerId, transactionMonth));

    if ("TOTAL".equals(transactionMonth)) {
      return customerTransactions;
    }
    return customerTransactions.stream()
            .filter(transaction -> transaction.getTransactionMonth().name().equals(transactionMonth))
            .collect(toList());

  }
}
