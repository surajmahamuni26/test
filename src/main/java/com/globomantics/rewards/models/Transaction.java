package com.globomantics.rewards.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "transaction")
@JsonIgnoreProperties({"customer"})
public class Transaction {

  private static List<String> months = Arrays.asList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
          "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "transactionId", nullable = false)
  private Long transactionId;

  @Column(name = "transactionTime", nullable = false)
  private LocalDateTime transactionTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "transactionMonth")
  private Month transactionMonth;

  @Column(name = "purchaseValue", nullable = false)
  private Double purchaseValue;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "customerId", referencedColumnName = "customerId")
  private Customer customer;

  public Transaction() {}

  public Transaction(LocalDateTime transactionTime, String transactionMonth, Double purchaseValue) {
    this.transactionTime = transactionTime;
    this.transactionMonth = Month.of(months.indexOf(transactionMonth) + 1);
    this.purchaseValue = purchaseValue;
  }

  public LocalDateTime getMonthOfPurchase() {
    return transactionTime;
  }

  public Double getPurchaseValue() {
    return purchaseValue;
  }

  public static void setMonths(List<String> months) {
    Transaction.months = months;
  }

  public void setTransactionId(Long transactionId) {
    this.transactionId = transactionId;
  }

  public void setTransactionTime(LocalDateTime transactionTime) {
    this.transactionTime = transactionTime;
  }

  public void setTransactionMonth(Month transactionMonth) {
    this.transactionMonth = transactionMonth;
  }

  public void setPurchaseValue(Double purchaseValue) {
    this.purchaseValue = purchaseValue;
  }

  public static List<String> getMonths() {
    return months;
  }

  public Long getTransactionId() {
    return transactionId;
  }

  public LocalDateTime getTransactionTime() {
    return transactionTime;
  }

  @JsonBackReference
  public Customer getCustomer() {
    return customer;
  }

  public Month getTransactionMonth() {
    return transactionMonth;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  @Override
  public String toString() {
    return "Transaction{" +
            "transactionId='" + transactionId +
            ", transactionTime=" + transactionTime +
            ", transactionMonth=" + transactionMonth +
            ", purchaseValue=" + purchaseValue +
            ", customer=" + customer +
            '}';
  }
}
