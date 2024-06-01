package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse for kalorieudregning for høj aktivitet i UseCase-klassen.
 */
public class UseCaseCalculateCaloriesForHighActivityTest {

    @InjectMocks
    private UseCase useCase;

    @Mock
    private DBRepository dbRepository;

    /**
     * Opsætning af tests. Initialiserer mocks og inject mocks i UseCase.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test for at sikre korrekt kalorieudregning for en bruger med høj aktivitetsniveau.
     */
    @Test
    public void testCalculateCaloriesForHighActivity() {
        User user = new User();
        user.setGender('M');
        user.setWeight(85.0);
        user.setHeight(175.0);
        user.setBirthday(LocalDate.of(1994, 5, 2));
        user.setActivityLevel(4);
        user.setGoal(0);

        double calories = useCase.calculateCalories(user);
        assertEquals(3325.0, calories);
    }
}
