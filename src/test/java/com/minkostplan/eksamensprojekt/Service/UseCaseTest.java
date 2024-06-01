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
 * Generel testklasse for UseCase-klassen.
 */
public class UseCaseTest {

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
     * Test for at sikre korrekt kalorieudregning for en mandlig bruger med moderat aktivitetsniveau.
     */
    @Test
    public void testCalculateCalories() {
        User user = new User();
        user.setGender('M');
        user.setWeight(75.0);
        user.setHeight(180.0);
        user.setBirthday(LocalDate.of(1998, 10, 14));
        user.setActivityLevel(2);
        user.setGoal(0);

        double calories = useCase.calculateCalories(user);
        assertEquals(2336.75, calories);
    }
}
