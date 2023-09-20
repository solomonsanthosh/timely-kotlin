package com.timelyserver.timelyserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.timelyserver.timelyserver.entity.TeamTask;
import com.timelyserver.timelyserver.entity.UserDetail;
import com.timelyserver.timelyserver.repository.UserRepository;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/user/{email}")
    ResponseEntity<?> getUser(@PathVariable String email) {

        System.out.println(email);
        Optional<UserDetail> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            return ResponseEntity.ok(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"user not found");
        }

    }
    @PostMapping("/api/users")
    List<UserDetail> getUsers(@RequestBody TeamTask task){

       
       
        List<UserDetail> users = userRepository.findAllByEmail(task.getMembers());
        
        System.out.println(users.get(0).getEmail());
        return users;
    }
    
    @PostMapping("/api/user")
    UserDetail postUser(@RequestBody UserDetail user) {
        return userRepository.save(user);
    }

    @PutMapping("/api/user/{email}/{token}")
    Optional<UserDetail> updateUserToken(@PathVariable String email,@PathVariable String token){
        return userRepository.findByEmail(email).map(user-> 
        {
            user.setToken(token);
            return userRepository.save(user);
        });
    };
   

}
