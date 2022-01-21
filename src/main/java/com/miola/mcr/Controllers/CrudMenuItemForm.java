package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.MenuItem;
import com.miola.mcr.Services.DiningTableService;
import com.miola.mcr.Services.MenuItemService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.BindingUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudMenuItemForm implements Initializable {

    private final FxWeaver fxWeaver;
    private final MenuItemService menuItemService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXTextField tfTitle;

    @FXML
    private MFXTextField tfPrice;

    @FXML
    private MFXTextField tfDescription;



    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idMenuItem;

    @Autowired
    public CrudMenuItemForm(FxWeaver fxWeaver, MenuItemService menuItemService) {
        this.fxWeaver = fxWeaver;
        this.menuItemService = menuItemService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //tfState = new MFXTextField("empty");
        // Set Validation
        this.setFieldsValidators();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    void save(ActionEvent event) {
        boolean fieldsValidation = tfTitle.isValid() && tfPrice.isValid() && tfDescription.isValid();

        if (fieldsValidation){
            MenuItem menuItemToEditOrAdd = new MenuItem();
            menuItemToEditOrAdd.setTitle(getTitle());
            menuItemToEditOrAdd.setPrice(Double.parseDouble(getPrice()));
            menuItemToEditOrAdd.setDescription(getDescription());
            if (isAnEdit){
                menuItemToEditOrAdd.setId(this.idMenuItem);
                if (menuItemService.editMenuItem(menuItemToEditOrAdd)){
                    fxWeaver.getBean(CrudMenuItem.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudMenuItem.class).showAlter(0);
                }
            }else{
                if (menuItemService.saveMenuItem(menuItemToEditOrAdd)){
                    fxWeaver.getBean(CrudMenuItem.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudMenuItem.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudMenuItem.class).showAlter(4);
        }


    }

    public void fillData(Long id, String title, double price, String description){
        this.idMenuItem = id;
        tfTitle.setText(title);
        tfPrice.setText(String.valueOf(price));
        tfDescription.setText(description);
        isAnEdit = true;
    }


    public void closeWindow(){
        this.clearFields();
        this.isAnEdit = false;
        Stage formWindow = (Stage) (tfTitle.getScene().getWindow());
        formWindow.fireEvent(new WindowEvent(formWindow, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setFieldsValidators() {
        // Tile TextField : Required
        tfTitle.setValidated(true);
        tfTitle.getValidator().add(
                BindingUtils.toProperty(tfTitle.textProperty().isNotEmpty()),
                "Required"
        );

        tfPrice.setValidated(true);
        tfPrice.getValidator().add(
                BindingUtils.toProperty(tfPrice.textProperty().isNotEmpty()),
                "Required"
        );
    }

    public void clearFields(){
        tfTitle.clear();
        tfPrice.clear();
        tfDescription.clear();
    }


    public String getTitle() {
        return tfTitle.getText();
    }
    public String getPrice() {
        return tfPrice.getText();
    }
    public String getDescription() {
        return tfDescription.getText();
    }

}

