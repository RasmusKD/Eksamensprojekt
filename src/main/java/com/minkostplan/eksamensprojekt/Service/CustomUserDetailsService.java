package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviceklasse til håndtering af brugerautentificering.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DBRepository dbRepository;

    @Autowired
    private UseCase useCase;
    /**
     * Indlæser brugerdata baseret på brugernavn (email).
     *
     * @param username brugernavnet (email) på den bruger, der forsøger at logge ind.
     * @return UserDetails objekt med brugerdata.
     * @throws UsernameNotFoundException hvis brugeren ikke findes i databasen.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dbRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Brugeren blev ikke fundet");
        }

        // Sæt currentUser i UseCase
        useCase.setCurrentUser(user);

        return user;
    }
}
