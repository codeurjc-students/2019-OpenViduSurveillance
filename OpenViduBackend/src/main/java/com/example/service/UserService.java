package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.security.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;

@Component
@CrossOrigin
@Service
public class UserService implements ApplicationRunner, UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {

    }

    public User getUser(String username){
        return userRepository.findByUserName(username);
    }

    public Boolean validUser(User user){
        return user.getPassword().equalsIgnoreCase(getUser(user.getUserName()).getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User u = getUser(s);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(u.getUserName(),u.getPassword(),roles);
        return userDetails;
    }
}
