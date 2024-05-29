package com.minkostplan.eksamensprojekt.Config;

import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Import(SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;  // MockMvc object for isoleret test

    @MockBean  // Laver en mock-bean for UserDetailsService
    private UserDetailsService userDetailsService;

    @BeforeEach  // Før hver test køres denne setup metode
    public void setup() {
        mockMvc = MockMvcBuilders  // Bygger MockMvc objekt
                .webAppContextSetup(context)  // setter application context op
                .apply(springSecurity())  // Bruger af springSecurity
                .build();

        // Mock user details service med forskellige roller
        Mockito.when(userDetailsService.loadUserByUsername("user"))
                .thenReturn(User.withUsername("user").password("password").roles("USER").build());
        Mockito.when(userDetailsService.loadUserByUsername("subscriber"))
                .thenReturn(User.withUsername("subscriber").password("password").roles("SUBSCRIBER").build());
        Mockito.when(userDetailsService.loadUserByUsername("employee"))
                .thenReturn(User.withUsername("employee").password("password").roles("EMPLOYEE").build());
        Mockito.when(userDetailsService.loadUserByUsername("admin"))
                .thenReturn(User.withUsername("admin").password("password").roles("ADMIN").build());
    }

    @Test  // Tester alle "public" urls, som alle skal kunne få adgang til uanset rolle
    void testPublicUrls() throws Exception {
        mockMvc.perform(get("/css/style.css")).andExpect(status().isOk());  // Test CSS file access
        mockMvc.perform(get("/")).andExpect(status().isOk());  // Test home page access
        mockMvc.perform(get("/login")).andExpect(status().isOk());  // Test login page access
        mockMvc.perform(get("/register")).andExpect(status().isOk());  // Test registration page access
        mockMvc.perform(get("/omOs")).andExpect(status().isOk());  // Test "about us" page access
    }

    @Test  // Tester opret-medarbejder, kun admin rollen kan se den
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminUrls() throws Exception {
        mockMvc.perform(get("/opret-medarbejder")).andExpect(status().isOk());
    }


    @Test  // Tester at logge ud og efterser redirection
    void testLogout() throws Exception {
        mockMvc.perform(get("/perform_logout")).andExpect(status().is3xxRedirection());
    }
}
