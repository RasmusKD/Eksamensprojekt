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
    private String  subscriptionId; // Add this line


    public User() {
    }

    public User(int userId, String firstName, String lastName, String email, String password, int age, char gender, double weight, double height, int activityLevel, int goal, int employed, boolean subscriber, String subscriptionId) {
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
        this.subscriptionId = subscriptionId; // Add this line
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
    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

}
