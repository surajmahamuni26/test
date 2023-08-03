package com.globomantics.rewards.service;

import com.globomantics.rewards.models.Customer;
import com.globomantics.rewards.models.Transaction;
import com.globomantics.rewards.models.exceptions.BadRequest;
import com.globomantics.rewards.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardsService rewardsService;

    @Test
    public void shouldThrowBadRequestWhenCustomerDoesNotExist() {

        when(customerRepository.findById(any())).thenReturn(Optional.empty());

        BadRequest badRequest =
                assertThrows(BadRequest.class, () -> rewardsService.getRewardPoints(5L, "TOTAL"));

        assertThat(badRequest.getMessage()).isEqualTo("Customer with id: 5 does NOT exist");
    }

    @Test
    public void shouldThrowBadRequestWhenMonthOfPurchaseIsInCorrect() {

        Transaction transaction1 = new Transaction(LocalDateTime.now(), "JANUARY", 150d);

        Customer customer = new Customer(5L, "Naresh");
        customer.setTransactions(singletonList(transaction1));

        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        BadRequest badRequest =
                assertThrows(BadRequest.class, () -> rewardsService.getRewardPoints(5L, "SOMETHING"));

        assertThat(badRequest.getMessage()).isEqualTo("monthOfPurchase: SOMETHING is NOT acceptable, " +
                "refer WHITE LIST in ReadMe.md file for acceptable values");
    }

    @Test
    public void shouldGiveRewardPointsForCustomerForSpecificMonth() throws BadRequest {

        Transaction transaction1 = new Transaction(LocalDateTime.now(), "JANUARY", 150d);

        Customer customer = new Customer(5L, "Naresh");
        customer.setTransactions(singletonList(transaction1));

        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        assertThat(rewardsService.getRewardPoints(5L, "January").getRewardPoints())
                .isEqualTo(150d);
    }

    @Test
    public void shouldGiveRewardPointsForCustomerForTotalPurchase() throws BadRequest {

        Transaction transaction1 = new Transaction(LocalDateTime.now(), "JANUARY", 7580d);

        Customer customer = new Customer(5L, "Naresh");
        customer.setTransactions(singletonList(transaction1));

        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        assertThat(rewardsService.getRewardPoints(5L, "Total").getRewardPoints())
                .isEqualTo(15010d);
    }

}