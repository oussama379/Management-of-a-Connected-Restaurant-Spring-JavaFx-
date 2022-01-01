package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.User;
import com.miola.mcr.Services.UserService;
import com.miola.mcr.Services.UserServiceImp;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.cell.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import io.github.palexdev.materialfx.controls.MFXTableView;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudUser implements Initializable {

    private final ConfigurableApplicationContext applicationContext;
    private final UserService userService;

    @FXML
    private MFXTableView<User> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;

    @Autowired
    public CrudUser(ConfigurableApplicationContext applicationContext, UserService userService) {
        this.applicationContext = applicationContext;
        this.userService = userService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formWindow = new Stage();
        populateTable();
    }

    private void populateTable() {
        // dummy data to be deleted
/*        List<User> dummyData = List.of(
                new User(1L, "Monsieur1", "user0", "123"),
                new User(2L, "Monsieur2", "user0", "123"),
                new User(0L, "Monsieur0", "user0", "123"),
                new User(3L, "Monsieur3", "user0", "123"),
                new User(4L, "Monsieur4", "user0", "123"),
                new User(5L, "Monsieur5", "user0", "123"),
                new User(6L, "Monsieur6", "user0", "123"),
                new User(7L, "Monsieur7", "user0", "123"),
                new User(8L, "Monsieur8", "user0", "123"),
                new User(9L, "Monsieur9", "user0", "123"),
                new User(1L, "Monsieur1", "user0", "123"),
                new User(2L, "Monsieur2", "user0", "123"),
                new User(0L, "Monsieur0", "user0", "123"),
                new User(3L, "Monsieur3", "user0", "123"),
                new User(4L, "Monsieur4", "user0", "123"),
                new User(5L, "Monsieur5", "user0", "123"),
                new User(6L, "Monsieur6", "user0", "123"),
                new User(7L, "Monsieur7", "user0", "123"),
                new User(8L, "Monsieur8", "user0", "123"),
                new User(9L, "Monsieur9", "user0", "123"),
                new User(1L, "Monsieur1", "user0", "123"),
                new User(2L, "Monsieur2", "user0", "123")
                );*/

        /* load data */
        //ObservableList<User> data = FXCollections.observableArrayList(dummyData);
        ObservableList<User> users = FXCollections.observableArrayList(userService.getAllUsers());

        /* create columns */
        MFXTableColumn<User> idColumn = new MFXTableColumn<User>("Id", Comparator.comparing(User::getId));
        MFXTableColumn<User> nameColumn = new MFXTableColumn<User>("Name", Comparator.comparing(User::getName));
        MFXTableColumn<User> usernameColumn = new MFXTableColumn<User>("Username", Comparator.comparing(User::getUsername));
        // TODO filter role
        MFXTableColumn<User> roleColumn = new MFXTableColumn<User>("Role", Comparator.comparing(User::getRoleName));

        /* link columns with proprieties */
        idColumn.setRowCellFunction(user -> new MFXTableRowCell(String.valueOf(user.getId())));
        nameColumn.setRowCellFunction(user -> new MFXTableRowCell(user.getName()));
        usernameColumn.setRowCellFunction(user -> new MFXTableRowCell(user.getUsername()));
        roleColumn.setRowCellFunction(user -> new MFXTableRowCell(user.getRoleName()));

        /* fill table */
        //tableView.setItems(data);
        tableView.setItems(users);
        tableView.getTableColumns().addAll(idColumn);
        tableView.getTableColumns().addAll(nameColumn);
        tableView.getTableColumns().addAll(usernameColumn);
        tableView.getTableColumns().addAll(roleColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        // TODO after deleting a user the view need to be reloaded
        List<User> selectedUsers = tableView.getSelectionModel().getSelectedItems(); // gives a List<User> of selected users from tableView
        if (!selectedUsers.isEmpty()) {
            for (User user : selectedUsers){
                    userService.deleteUserById(user.getId());
            }
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        List<User> selectedUsers = tableView.getSelectionModel().getSelectedItems();
        if (!selectedUsers.isEmpty()) {
            /* take first user in selected users */
            User userToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);

            /* show form */
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(CrudUserForm.class);
            Scene scene = new Scene(root);
            //System.out.print(userToBeEdit.getRoleName());
            fxWeaver.getBean(CrudUserForm.class).fillData(userToBeEdit.getName(), userToBeEdit.getUsername(), userToBeEdit.getId(), userToBeEdit.getRoleName()); // send data to the form scene
            formWindow.close();
            formWindow.setScene(scene);
            formWindow.setTitle("Edit user");
            formWindow.show();
        }
    }

    @FXML
    public void add(ActionEvent event) {
        /* show form */
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(CrudUserForm.class);
        Scene scene = new Scene(root);
        formWindow.close();
        formWindow.setScene(scene);
        formWindow.setTitle("Add new user");
        formWindow.show();
    }

}
