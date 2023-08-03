package com.globomantics.rewards.controller;

import com.globomantics.rewards.models.RewardsSummary;
import com.globomantics.rewards.models.exceptions.BadRequest;
import com.globomantics.rewards.service.RewardsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/rewards")
public class RewardsController {

  private final RewardsService rewardsService;

  public RewardsController(RewardsService rewardsService) {
    this.rewardsService = rewardsService;
  }

  @RequestMapping(method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public RewardsSummary getRewardPoints(
      @RequestParam Long customerId, @RequestParam String monthOfPurchase) throws BadRequest {
    return rewardsService.getRewardPoints(customerId, monthOfPurchase);
  }
}
