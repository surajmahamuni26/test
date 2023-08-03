package com.globomantics.rewards.repository;

import com.globomantics.rewards.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionHistory extends JpaRepository<Transaction, String> {

  @Override
  Optional<Transaction> findById(String s);
}
