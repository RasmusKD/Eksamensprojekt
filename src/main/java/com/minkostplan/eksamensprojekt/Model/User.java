package com.minkostplan.eksamensprojekt.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User implements UserDetails {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int age;
    private char gender;
    private double weight;
    private double height;
    private int activityLevel;
    private int goal;
    private int employed;
    private boolean subscriber;

    public User() {
    }

    public User(int userId, String firstName, String lastName, String email, String password, int age, char gender, double weight, double height, int activityLevel, int goal, int employed, boolean subscriber) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.employed = employed;
        this.subscriber = subscriber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (subscriber) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"));
        }
        switch (employed) {
            case 1:
                authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                break;
            case 2:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
        }
        return authorities;
    }
    public double getCaloricNeeds() {
        double bmr;
        if (this.gender == 'M') {
            bmr = (10 * this.weight) + (6.25 * this.height) - (5 * this.age) + 5;
        } else {
            bmr = (10 * this.weight) + (6.25 * this.height) - (5 * this.age) - 161;
        }

        double totalCalories = getTotalCalories(bmr);

        switch (this.goal) {
            case 0: // Weight loss
                totalCalories -= 500;
                break;
            case 1: // Weight gain
                totalCalories += 500;
                break;
            case 2: // Muscle gain
                totalCalories += 300; // Slight surplus for muscle gain
                break;
            case 3: // Maintain weight
                // No adjustment needed
                break;
        }

        return totalCalories;
    }

    private double getTotalCalories(double bmr) {
        double activityMultiplier;
        switch (this.activityLevel) {
            case 0:
                activityMultiplier = 1.2;
                break;
            case 1:
                activityMultiplier = 1.5;
                break;
            case 2:
                activityMultiplier = 1.7;
                break;
            case 3:
                activityMultiplier = 1.9;
                break;
            case 4:
                activityMultiplier = 2.4;
                break;
            default:
                activityMultiplier = 1.2;
        }

        double totalCalories = bmr * activityMultiplier;
        return totalCalories;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public int getEmployed() {
        return employed;
    }

    public void setEmployed(int employed) {
        this.employed = employed;
    }

    public boolean isSubscriber() {
        return subscriber;
    }

    public void setSubscriber(boolean subscriber) {
        this.subscriber = subscriber;
    }
}
