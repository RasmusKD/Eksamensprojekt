package com.minkostplan.eksamensprojekt.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import com.stripe.exception.StripeException;
import com.stripe.model.SubscriptionCollection;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.SubscriptionUpdateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class StripeControllerTest {

    @Mock
    private UseCase useCase;

    @InjectMocks
    private StripeController stripeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateSubscriptionStatus() throws StripeException {
        // Mocking Stripe API response
        com.stripe.model.Subscription stripeSubscription1 = mock(com.stripe.model.Subscription.class);
        when(stripeSubscription1.getMetadata()).thenReturn(Map.of("userId", "1"));
        when(stripeSubscription1.getCancelAtPeriodEnd()).thenReturn(false);
        when(stripeSubscription1.getCurrentPeriodEnd()).thenReturn(Instant.now().getEpochSecond() - 1000);

        SubscriptionCollection subscriptionCollection = mock(SubscriptionCollection.class);
        when(subscriptionCollection.getData()).thenReturn(Arrays.asList(stripeSubscription1));

        // Mocking static method
        try (MockedStatic<com.stripe.model.Subscription> mockedSubscription = Mockito.mockStatic(com.stripe.model.Subscription.class)) {
            mockedSubscription.when(() -> com.stripe.model.Subscription.list(any(SubscriptionListParams.class))).thenReturn(subscriptionCollection);

            // Mocking useCase.getSubscriptionByUserId
            Subscription dbSubscription1 = new Subscription();
            dbSubscription1.setUserId(1);
            dbSubscription1.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
            when(useCase.getLatestSubscriptionByUserId(1)).thenReturn(dbSubscription1);

            // Execute the method
            stripeController.updateSubscriptionStatus();

            // Verify interactions and state changes
            verify(stripeSubscription1).update(any(SubscriptionUpdateParams.class));
            verify(useCase).updateUserSubscriptionStatus(1, true);
        }
    }
}
