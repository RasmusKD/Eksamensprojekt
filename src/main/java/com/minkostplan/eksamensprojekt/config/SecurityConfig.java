package com.minkostplan.eksamensprojekt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Konfigurationsklasse for sikkerhedsindstillinger ved hjælp af Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Konfigurerer sikkerhedsfilterkæden.
     *
     * @param http HttpSecurity-objekt til konfiguration af sikkerhedsindstillinger.
     * @return Et konfigureret SecurityFilterChain-objekt.
     * @throws Exception Hvis der opstår en fejl under konfigurationen.
     */
    @Bean //vi tager security filterchain fra spring
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth //Den første auth er parameter, den anden sætter parameteret med vores kode og returnere det til første auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/favicon.ico").permitAll()
                        .requestMatchers("/", "/login", "/register", "/about-us", "/check-email").permitAll()
                        .requestMatchers("/weekly-recipes", "/dashboard", "/success", "/cancel").authenticated()
                        .requestMatchers("/favorite-recipes", "/recipe", "/recipe/**", "/shopping-list/**").hasAnyRole("SUBSCRIBER", "EMPLOYEE", "ADMIN")
                        .requestMatchers("/recipe-creation", "/edit-recipe", "/add-ingredients", "/edit-ingredients").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers("/opret-medarbejder").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf //bliver sat som en cookie i ens browser, stopper ondsindet request. brugeren for en token når de logger indt, skjules som cookie
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/stripe/create-checkout-session", "/stripe/webhook")
                )
                .formLogin(form -> form
                        .loginPage("/login")//vores login page
                        .loginProcessingUrl("/login")//vores login
                        .defaultSuccessUrl("/weekly-recipes", true) //hvis det det er succes går man til weekly
                        .failureUrl("/login?error=true") //hvis fejl får man fejlmeddelse
                        .permitAll() //alle er permittet
                )
                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID") //sted hvor cookies bliver gemt
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Konfigurerer password encoder ved hjælp af BCrypt.
     *
     * @return Et konfigureret PasswordEncoder-objekt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } //vi over skriver springs password encoder og bruger bcryptpassword encoder
}
