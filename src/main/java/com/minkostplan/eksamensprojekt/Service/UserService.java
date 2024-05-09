package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import com.minkostplan.eksamensprojekt.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private DBRepository dBRepository;

    public void createUser(User user) {
        dBRepository.createUser(user);
    }
}