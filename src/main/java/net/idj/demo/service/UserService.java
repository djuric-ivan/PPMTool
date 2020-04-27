package net.idj.demo.service;

import net.idj.demo.domain.User;
import net.idj.demo.exceptions.UserAlreadyExistException;
import net.idj.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public User saveUser(User newUser){
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        if(!userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            newUser.setUsername(newUser.getUsername());
        }else {
            throw new UserAlreadyExistException("User with " + newUser.getUsername() + " already exists");
        }

            //Username has to be unique (exception)
            //Make sure that password and confirmPassword match
            //We don't persist or show the confirmPassword
        return userRepository.save(newUser);
    }



}
