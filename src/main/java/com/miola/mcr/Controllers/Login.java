package com.miola.mcr.Controllers;


import com.miola.mcr.Dao.UserRepository;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Services.UserService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView
public class Login {

    private final ConfigurableApplicationContext applicationContext;
    private final UserService userService;
    private final UserRepository userRepository;
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

    public static Long idCurrentUser = 1L; // Admin
    public static Role roleCurrentUser;



    @Autowired
    public Login(ConfigurableApplicationContext applicationContext, UserService userService, UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @FXML
    void handleButtonAction(MouseEvent event) {
        if(userService.authenticate(getUsername(), getPassword()) != null){
            idCurrentUser = userRepository.findByUsername(getUsername()).getId();
            roleCurrentUser = userRepository.findByUsername(getUsername()).getRole();
            lblErrors.setText("Login Succefull.");
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Pane root = fxWeaver.loadView(MainScene.class);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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
