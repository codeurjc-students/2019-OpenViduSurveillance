package com.example.demo;

import com.example.entity.User;
import com.example.openViduBackend.Application;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@ComponentScan(basePackages = {"com.example"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
class ApplicationTests {
}
