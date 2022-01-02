package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.DiningTable;
import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Entities.User;
import com.miola.mcr.Services.DiningTableService;
import com.miola.mcr.Services.RoleService;
import com.miola.mcr.Services.UserService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

@Component
@FxmlView
public class CrudDinningTableForm implements Initializable {

    private final DiningTableService diningTableService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXTextField tfNumber;

    @FXML
    private MFXTextField tfState;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idDinningTable;

    @Autowired
    public CrudDinningTableForm(DiningTableService diningTableService) {
        this.diningTableService = diningTableService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        DiningTable diningTableToEditOrAdd = new DiningTable();
        diningTableToEditOrAdd.setNumber(Integer.parseInt(getNumber()));
        diningTableToEditOrAdd.setState(getState());
        if (isAnEdit){
            try {
                diningTableToEditOrAdd.setId(this.idDinningTable);
                diningTableService.editDiningTable(diningTableToEditOrAdd);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }else{
           try {
               diningTableService.saveDiningTable(diningTableToEditOrAdd);
               // TODO show Confirmation Msg
           }catch (Exception e){
               // TODO show Error Msg
           }
        }

    }


    public void fillData(int number, Long id, String state){
        this.idDinningTable = id;
        tfNumber.setText(String.valueOf(number));
        tfState.setText(state);
        isAnEdit = true;
    }


    public String getNumber() {
        return tfNumber.getText();
    }
    public String getState() {
        return tfState.getText();
    }

}

