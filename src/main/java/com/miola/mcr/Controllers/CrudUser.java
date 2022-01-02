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
public class CrudUser implements Initializable {

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
        populateTable();

        Parent root = fxWeaver.loadView(CrudUserForm.class);
        Scene scene = new Scene(root);
        formWindow = new Stage();
        formWindow.initModality(Modality.APPLICATION_MODAL); // makes stage act as a modal
        formWindow.setResizable(false); // prevents resize and removes minimize and maximize buttons
        formWindow.initStyle(StageStyle.UNDECORATED);
        formWindow.setScene(scene);
        formWindow.setOnCloseRequest(ev -> {
            this.updateTable();
        });

        dialog = MFXDialogFactory.buildDialog(DialogType.INFO, "MFXDialog - Generic Dialog", null);
        dialog.setIsDraggable(true);
        dialog.setPrefSize(200, 100);
        dialog.setVisible(false);
    }

    private void populateTable() {
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

    public  void updateTable(){
        /* load data */
        ObservableList<User> users = FXCollections.observableArrayList(userService.getAllUsers());
        tableView.setItems(users);
    }

    /*
    * int alertType :
    *       0 -> Save Failure
    *       1 -> Save Success
    *       2 -> Delete Confirmation TODO Delete Confirmation
    *       3 -> Delete Success
    *       4 -> Validation Warning
    * */
    public void showAlter(int alertType){
        if (centerPane == null) {
            centerPane = (AnchorPane) ((BorderPane) tableView.getScene().getRoot()).getCenter();

            AnchorPane.setRightAnchor(dialog, 20.0);
            AnchorPane.setBottomAnchor(dialog, 20.0);
            centerPane.getChildren().add(dialog);
            dialog.setOverlayClose(true);
        }
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished( event -> dialog.close() );

        switch (alertType){
            case 0:
                MFXDialogFactory.convertToSpecific(DialogType.ERROR, dialog);
                dialog.setTitle("Save failed");
                break;
            case 1:
                MFXDialogFactory.convertToSpecific(DialogType.INFO, dialog);
                dialog.setTitle("Saved successfully");
                break;
            case 2:
                break;
            case 3:
                MFXDialogFactory.convertToSpecific(DialogType.INFO, dialog);
                dialog.setTitle("Deleted successfully");
                break;
            case 4:
                MFXDialogFactory.convertToSpecific(DialogType.WARNING, dialog);
                dialog.setTitle("Fields validation failed");
                break;
            default:
        }
        dialog.show();
        delay.play();
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
        List<User> selectedUsers = tableView.getSelectionModel().getSelectedItems();
        if (!selectedUsers.isEmpty()) {
            /* take first user in selected users */
            User userToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);

            /* show form */
            fxWeaver.getBean(CrudUserForm.class).fillData(userToBeEdit.getName(), userToBeEdit.getUsername(), userToBeEdit.getId(), userToBeEdit.getRoleName()); // send data to the form scene
            formWindow.close();
            // Specifies the owner Window (parent) for new window
//            Stage primaryStage = (Stage) btnDelete.getScene().getWindow();
//            formWindow.initOwner(primaryStage);
            formWindow.setTitle("Edit user");
//            formWindow.show();
            formWindow.showAndWait(); // blocks execution until the stage is closed
        }
    }

    @FXML
    public void add(ActionEvent event) {
        /* show form */
        formWindow.close();
        // Specifies the owner Window (parent) for new window
//        Stage primaryStage = (Stage) btnDelete.getScene().getWindow();
//        formWindow.initOwner(primaryStage);
        formWindow.setTitle("Add new user");
//        formWindow.show();
        formWindow.showAndWait(); // blocks execution until the stage is closed
    }

}
