package com.globomantics.rewards.models;

public class RewardsSummary {
  private final Long customerId;
  private final Double rewardPoints;

  public RewardsSummary(Long customerId, Double rewardPoints) {
    this.customerId = customerId;
    this.rewardPoints = rewardPoints;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public Double getRewardPoints() {
    return rewardPoints;
  }
}
