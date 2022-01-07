package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.User;
import com.miola.mcr.Services.UserService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.base.AbstractMFXDialog;
import io.github.palexdev.materialfx.controls.cell.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.controls.enums.DialogType;
import io.github.palexdev.materialfx.controls.factories.MFXDialogFactory;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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
public class CrudUser extends Crud implements Initializable {

    private final UserService userService;
    private final FxWeaver fxWeaver;

    @FXML
    private MFXTableView<User> tableView;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    private Stage formWindow;
    private Pane centerPane = null;
    private AbstractMFXDialog dialog;

    @Autowired
    public CrudUser(UserService userService, FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
        this.userService = userService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        System.out.println(resourceBundle);
        populateTable();

        formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(CrudUserForm.class)));
        dialog = MFXDialogFactory.buildDialog(DialogType.INFO, "MFXDialog - Generic Dialog", null);

        super.initialize(formWindow, dialog);
    }

    public void populateTable() {
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
        List<User> selectedUsers = tableView.getSelectionModel().getSelectedItems(); // gives a List<User> of selected users from tableView
        if (!selectedUsers.isEmpty()) {
            for (User user : selectedUsers){
                    userService.deleteUserById(user.getId());
            }
            this.showAlter(3);
            this.updateTable();
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        edit(this.getClass() ,tableView, formWindow);
    }

    @FXML
    public void add(ActionEvent event) {
        super.add(formWindow);
    }

    public  void updateTable(){
        /* load data */
        ObservableList<User> users = FXCollections.observableArrayList(userService.getAllUsers());
        tableView.setItems(users);
    }

    public void showAlter(int alertType){
        super.showAlter(alertType ,centerPane, tableView, dialog);
    }
}
