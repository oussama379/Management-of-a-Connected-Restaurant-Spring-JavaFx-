package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.cell.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @FXML
    private MFXTableView<User> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formWindow = new Stage();
        populateTable();
    }

    private void populateTable() {
        // dummy data to be deleted
        List<User> dummyData = List.of(
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
                );
        // TODO load data from database to this function "observableArrayList(Collection<User> C)"
        /* load data */
        ObservableList<User> data = FXCollections.observableArrayList(dummyData);

        /* create columns */
        MFXTableColumn<User> nameColumn = new MFXTableColumn<User>("Name", Comparator.comparing(User::getName));

        /* link columns with proprieties */
        nameColumn.setRowCellFunction(user -> new MFXTableRowCell(user.getName()));

        /* fill table */
        tableView.setItems(data);
        tableView.getTableColumns().addAll(nameColumn);
    }

    @FXML
    public void delete(ActionEvent event) {
        List<User> selectedUsers = tableView.getSelectionModel().getSelectedItems(); // gives a List<User> of selected users from tableView
        if (!selectedUsers.isEmpty()) {
            // TODO delete selected users
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
            fxWeaver.getBean(CrudUserForm.class).fillData(userToBeEdit.getName(), userToBeEdit.getUsername(), userToBeEdit.getPassword()); // send data to the form scene
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
