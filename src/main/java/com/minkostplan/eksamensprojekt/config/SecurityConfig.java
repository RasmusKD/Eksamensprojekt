package com.minkostplan.eksamensprojekt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()  // Tillader alle at tilgå forsiden
                        .requestMatchers("/login", "/register").permitAll()  // Tillader alle at tilgå login og registrer
                        .anyRequest().authenticated()  // kræver authentication for alle andre requests
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Login url
                        .defaultSuccessUrl("/", true)  // TODO: Ændres til siden man kommer til efter login
                        .permitAll()  // Tillader alle at se login siden
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll());  // Går til forsiden efter logud
        return http.build();
    }
}
