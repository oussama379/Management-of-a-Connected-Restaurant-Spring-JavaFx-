package com.miola.mcr;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class McrApplication {

    public static void main(String[] args) {

        //SpringApplication.run(McrApplication.class, args);
        Application.launch(JavaFxApplication.class, args);
    }

}
