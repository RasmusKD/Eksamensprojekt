package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UseCaseCalculateCaloriesForFemaleTest {

    @InjectMocks
    private UseCase useCase;

    @Mock
    private DBRepository dbRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCalculateCaloriesForFemale() {
        User user = new User();
        user.setGender('F');
        user.setWeight(60.0);
        user.setHeight(165.0);
        user.setAge(30);
        user.setActivityLevel(1);
        user.setGoal(0);

        double calories = useCase.calculateCalories(user);
        assertEquals(1755.5, calories);
    }
}
