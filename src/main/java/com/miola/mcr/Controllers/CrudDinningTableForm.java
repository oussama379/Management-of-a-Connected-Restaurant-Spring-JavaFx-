package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Services.DiningTableService;
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
public class CrudDinningTableForm implements Initializable {

    private final FxWeaver fxWeaver;
    private final DiningTableService diningTableService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXTextField tfNumber;

    private MFXTextField tfState;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idDinningTable;

    @Autowired
    public CrudDinningTableForm(FxWeaver fxWeaver, DiningTableService diningTableService) {
        this.fxWeaver = fxWeaver;
        this.diningTableService = diningTableService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfState = new MFXTextField("empty");
        // Set Validation
        this.setFieldsValidators();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    void save(ActionEvent event) {
        boolean fieldsValidation = tfNumber.isValid();
        if (fieldsValidation){
            DiningTable diningTableToEditOrAdd = new DiningTable();
            diningTableToEditOrAdd.setNumber(Integer.parseInt(getNumber()));
            diningTableToEditOrAdd.setState(getState());
            if (isAnEdit){
                diningTableToEditOrAdd.setId(this.idDinningTable);
                if (diningTableService.editDiningTable(diningTableToEditOrAdd)){
                    fxWeaver.getBean(CrudDinningTable.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudDinningTable.class).showAlter(0);
                }
            }else{
                if (diningTableService.saveDiningTable(diningTableToEditOrAdd)){
                    fxWeaver.getBean(CrudDinningTable.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudDinningTable.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudDinningTable.class).showAlter(4);
        }


    }

    public void fillData(int number, Long id, String state){
        this.idDinningTable = id;
        tfNumber.setText(String.valueOf(number));
        tfState.setText(state);
        isAnEdit = true;
    }


    public void closeWindow(){
        this.clearFields();
this.isAnEdit = false;
        Stage formWindow = (Stage) (tfNumber.getScene().getWindow());
        formWindow.fireEvent(new WindowEvent(formWindow, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setFieldsValidators() {
        // Tile TextField : Required
        tfNumber.setValidated(true);
        tfNumber.getValidator().add(
                BindingUtils.toProperty(tfNumber.textProperty().isNotEmpty()),
                "Required"
        );
    }

    public void clearFields(){
        tfNumber.clear();
    }


    public String getNumber() {
        return tfNumber.getText();
    }
    public String getState() {
        return tfState.getText();
    }

}

