package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock // simulering
    private DBRepository dbRepository;

    @Mock // simulering
    private UseCase useCase;

    @InjectMocks // @Mock elementer bliver injicieret i denne klasse
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    /**
     * Opsætter testdata før hver test.
     * Denne metode opretter en brugerobjekt med testdata, der bruges i de efterfølgende tests.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFirstName("Test");
        user.setLastName("User");
    }

    /**
     * Tester scenariet hvor brugeren findes i databasen.
     * Forventet resultat: Brugerens detaljer returneres korrekt og setCurrentUser kaldes én gang.
     */
    @Test
    void loadUserByUsername_UserExists() {
        // Arrange: Forberedelse af nødvendige objekter og mock
        // Mock databasekaldet for at returnere den oprettede bruger, når der søges efter emailen
        when(dbRepository.findByEmail("test@example.com")).thenReturn(user);

        // Udskriver status til konsollen før metodekaldet
        System.out.println("Henter brugerens detaljer fra databasen...");

        // Act: Udfør handlingen der skal testes
        // Kald metoden loadUserByUsername
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        // Udskriver resultatet af handlingen til konsollen
        System.out.println("Brugerens detaljer hentet: " + (userDetails != null ? "SUCCESS" : "FAIL"));

        // Assert: Verificer at resultatet er som forventet
        // Kontroller at brugerens detaljer er korrekt og at setCurrentUser blev kaldt én gang
        System.out.println("Verificerer brugerens detaljer...");
        assertNotNull(userDetails, "Brugerens detaljer bør ikke være null.");
        assertEquals("test@example.com", userDetails.getUsername(), "Brugerens email bør være test@example.com.");
        verify(useCase, times(1)).setCurrentUser(user);
        System.out.println("Brugerens detaljer er korrekte og setCurrentUser blev kaldt én gang.");
    }

    /**
     * Tester scenariet hvor brugeren ikke findes i databasen.
     * Forventet resultat: En UsernameNotFoundException kastes.
     */
    @Test
    void loadUserByUsername_UserNotFound() {
        // Arrange: Forberedelse af nødvendige objekter og mock
        // Mock databasekaldet for at returnere null, når der søges efter emailen
        when(dbRepository.findByEmail("test@example.com")).thenReturn(null);


        System.out.println("Forsøger at hente ikke-eksisterende bruger fra databasen...");

        // Act & Assert: Udfør handlingen der skal testes og verificer resultatet
        // Kald metoden loadUserByUsername og kontroller at UsernameNotFoundException kastes
        try {
            customUserDetailsService.loadUserByUsername("test@example.com");
            System.out.println("Fejl: Forventet UsernameNotFoundException blev ikke kastet.");
        } catch (UsernameNotFoundException e) {
            System.out.println("UsernameNotFoundException blev kastet som forventet.");
        }
    }

    /**
     * Test der bevidst fejler for at sikre testinfrastrukturen fungerer korrekt.
     */
    @Test
    void loadUserByUsername_IntentionalFailure() {
        // Arrange: Forberedelse af nødvendige objekter og mock
        // Mock databasekaldet for at returnere den oprettede bruger, når der søges efter emailen
        when(dbRepository.findByEmail("test@example.com")).thenReturn(user);


        System.out.println("Bevidst fejlsituation: Sætter userDetails til null...");

        // Act: Sætter userDetails til null for at simulere en fejl
        UserDetails userDetails = null;

        // Assert: Kontroller at brugerens detaljer ikke er null (dette vil fejle)
        System.out.println("Verificerer bevidst fejlsituation...");
        assertNotNull(userDetails, "Denne test vil fejle, fordi userDetails er null.");
        System.out.println("Denne linje bør ikke udskrives, da testen skal fejle før dette punkt.");
    }
}
