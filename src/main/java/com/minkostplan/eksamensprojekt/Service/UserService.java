package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import com.minkostplan.eksamensprojekt.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private DBRepository dBRepository;
    private User currentUser; // Holds the reference to the logged-in user

    public void createUser(User user) {
        dBRepository.createUser(user);
    }

    public User login(String email, String password) {
        currentUser = dBRepository.login(email, password);  // Set currentUser
        return currentUser;
    }
    public void logout() {
        if (currentUser != null) {
            System.out.println("Logging out user: " + currentUser.getEmail());
            currentUser = null;
        } else {
            System.out.println("No user currently logged in.");
        }
        System.out.println("User has been logged out.");
    }

    public boolean isUserLoggedIn() {
        return this.currentUser != null;
    }
}
