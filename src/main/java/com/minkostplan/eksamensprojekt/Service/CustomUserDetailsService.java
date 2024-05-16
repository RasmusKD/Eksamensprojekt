package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DBRepository dbRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dbRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Brugeren blev ikke fundet");
        }

        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.password(user.getPassword());
        builder.roles("USER"); // TODO: Bruger rolle skift til at kende forskel på subscriber/employee

        return builder.build();
    }
}
