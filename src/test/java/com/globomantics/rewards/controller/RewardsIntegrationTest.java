package com.globomantics.rewards.controller;

import com.globomantics.rewards.models.RewardsSummary;
import com.globomantics.rewards.models.exceptions.BadRequest;
import com.globomantics.rewards.service.RewardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RewardsIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@MockBean
	private RewardsService rewardsService;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void shouldReturnRewardPointsForCustomerForSpecificMonth() throws Exception {

		when(rewardsService.getRewardPoints(5L, "January"))
				.thenReturn(getRewardsSummary(5L, 1580d));

		this.mockMvc.perform(
				get("/api/v1/rewards?customerId=5&monthOfPurchase=January"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("1580.0")));
	}

	@Test
	void shouldReturnRewardPointsForCustomerForTotalPurchases() throws Exception {

		when(rewardsService.getRewardPoints(5L, "total"))
				.thenReturn(getRewardsSummary(6L, 80d));

		this.mockMvc.perform(
				get("/api/v1/rewards?customerId=5&monthOfPurchase=total"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("80.0")));
	}

	@Test
	void shouldThrowExceptionWhenCustomerIdDoesNotExist() throws Exception {

		when(rewardsService.getRewardPoints(5L, "total"))
				.thenThrow(new BadRequest("Customer with id: 5 does NOT exist"));

		this.mockMvc.perform(
				get("/api/v1/rewards?customerId=5&monthOfPurchase=total"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(
						containsString("Customer with id: 5 does NOT exist")));
	}

	@Test
	void shouldThrowExceptionWhenMonthOfPurchaseIsInCorrect() throws Exception {

		when(rewardsService.getRewardPoints(5L, "total"))
				.thenThrow(new BadRequest("monthOfPurchase: SOMETHING is NOT acceptable, " +
						"refer WHITE LIST in ReadMe.md file for acceptable values"));

		this.mockMvc.perform(
				get("/api/v1/rewards?customerId=5&monthOfPurchase=total"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(
						containsString("monthOfPurchase: SOMETHING is NOT acceptable")));
	}

	@Test
	void shouldThrow5XXException() throws Exception {

		when(rewardsService.getRewardPoints(5L, "total"))
				.thenThrow(new RuntimeException("Unhandled Error"));

		this.mockMvc.perform(
				get("/api/v1/rewards?customerId=5&monthOfPurchase=total"))
				.andExpect(status().is5xxServerError());

	}

	private RewardsSummary getRewardsSummary(Long customerId, Double rewardPoints){
		return new RewardsSummary(customerId, rewardPoints);
	}

}
