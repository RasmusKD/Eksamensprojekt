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
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/favicon.ico").permitAll()
                        .requestMatchers("/",  "/login", "/register", "/omOs").permitAll()
                        .requestMatchers("/dashboard", "/success", "/cancel").authenticated()
                        .requestMatchers("/weekly-recipes", "/recipe").hasAnyRole("SUBSCRIBER", "EMPLOYEE", "ADMIN")
                        .requestMatchers("/recipe-creation", "/edit-recipe", "/recipe-success", "/add-ingredients", "/edit-ingredients").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers("/opret-medarbejder").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/stripe/create-checkout-session", "/stripe/webhook")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID")
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
    }
}
