package com.miola.mcr;

import com.miola.mcr.Dao.UserRepository;
import com.miola.mcr.Entities.User;
import com.miola.mcr.Services.UserService;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class McrApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {

        //SpringApplication.run(McrApplication.class, args);
        Application.launch(JavaFxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User u = new User("ilyas","ilyas3","1234");
        userService.saveUser(u);
        User u2 = new User("oussama","oussama","1234");
        userService.saveUser(u2);
        User u1 = new User("ilyasxxx","ilyas3","1234");
        userService.saveUser(u1);

    }
}
