package com.miola.mcr.Controllers;


import com.miola.mcr.Services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@FxmlView
public class Login {

    @FXML
    private Button btnFB;

    @FXML
    private Label btnForgot;

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;

    @FXML
    private Label lblErrors;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @Autowired
    private UserService userService;

    @FXML
    void handleButtonAction(MouseEvent event) {
        if(userService.authenticate(getUsername(), getPassword()) != null){
            lblErrors.setText("Login Succefull. ");
            System.out.print(userService.getUser());
        }else{
            lblErrors.setText("Login Failed.");
        }
    }
    public String getPassword() {
        return txtPassword.getText();
    }

    public String getUsername() {
        return txtUsername.getText();
    }
}
