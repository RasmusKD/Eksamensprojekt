package com.minkostplan.eksamensprojekt.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Repræsenterer en bruger i systemet med detaljer om brugerens oplysninger og sikkerhedsroller.
 */
public class User implements UserDetails {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate birthday;
    private char gender;
    private double weight;
    private double height;
    private int activityLevel;
    private int goal;
    private int employed;
    private boolean subscriber;

    /**
     * Standardkonstruktør.
     */
    public User() {
    }

    /**
     * Konstruktør med parametre til at initialisere alle felter.
     *
     * @param userId        brugerens ID
     * @param firstName     brugerens fornavn
     * @param lastName      brugerens efternavn
     * @param email         brugerens email
     * @param password      brugerens kodeord
     * @param birthday      brugerens fødselsdag
     * @param gender        brugerens køn
     * @param weight        brugerens vægt
     * @param height        brugerens højde
     * @param activityLevel brugerens aktivitetsniveau
     * @param goal          brugerens mål
     * @param employed      brugerens ansættelsesstatus
     * @param subscriber    om brugeren er abonnent
     */
    public User(int userId, String firstName, String lastName, String email, String password, LocalDate birthday, char gender, double weight, double height, int activityLevel, int goal, int employed, boolean subscriber) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.employed = employed;
        this.subscriber = subscriber;
    }

    /**
     * Henter brugerens autoriteter baseret på ansættelsesstatus og abonnementsstatus.
     * Autoriteter bruges til at bestemme en brugers adgangsniveau i systemet.
     *
     * @return en samling af autoriteter
     */
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

    /**
     * Henter brugerens kodeord.
     *
     * @return brugerens kodeord
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Henter brugerens brugernavn (email).
     *
     * @return brugerens email
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Kontrollerer om kontoen ikke er udløbet.
     *
     * @return true, da kontoen aldrig udløber
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Kontrollerer om kontoen ikke er låst.
     *
     * @return true, da kontoen aldrig er låst
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Kontrollerer om legitimationsoplysningerne ikke er udløbet.
     *
     * @return true, da legitimationsoplysningerne aldrig udløber
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Kontrollerer om kontoen er aktiveret.
     *
     * @return true, da kontoen altid er aktiveret
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getters og setters for alle felter

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

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public int getAge() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
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
