package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Entities.User;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.DeviceService;
import com.miola.mcr.Services.RoleService;
import com.miola.mcr.Services.SensorService;
import com.miola.mcr.Services.ZoneService;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
@FxmlView
public class CrudZoneForm implements Initializable {


    private final ZoneService zoneService;
    private final RoleService roleService;




    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXFlowlessCheckListView<String> listRole ;

    /*@FXML
    private MFXFlowlessCheckListView<String> listDevice = new MFXFlowlessCheckListView<>();

    @FXML
    private MFXFlowlessCheckListView<String> listSensor = new MFXFlowlessCheckListView<>();*/

    @FXML
    private MFXTextField tfName;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idZone;


    @Autowired
    public CrudZoneForm(ZoneService zoneService, RoleService roleService) {
        this.zoneService = zoneService;
        this.roleService = roleService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> roles = FXCollections.observableArrayList(roleService.getAllRolesNames());
        //ObservableList<String> sensors = FXCollections.observableArrayList(sensorService.getAllSensorsNames());
        //ObservableList<String> devices = FXCollections.observableArrayList(deviceService.getAllDevicesNames());
        listRole.setItems(roles);
//        listRole.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //listSensor.setItems(sensors);
        //listSensor.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //listDevice.setItems(devices);
        //listDevice.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        //System.out.println(listRole.getSelectionModel().getCheckedItems().get(0));
        Set<Role> selectedRoles = new HashSet<>();
        for (int i = 0 ; i < listRole.getSelectionModel().getCheckedItems().size() ; i++ ){
            System.out.println(listRole.getSelectionModel().getCheckedItems().get(i));
            selectedRoles.add(roleService.findRoleByTitle(listRole.getSelectionModel().getCheckedItems().get(i)));
//            System.out.println(selectedRoles.toString());
        }

       /* Set<Sensor> selectedSensors = new HashSet<>();
        for (int i = 0 ; i < listSensor.getSelectionModel().getSelectedItems().size() ;i++ ){
            selectedSensors.add(sensorService.findSensorByName(listSensor.getSelectionModel().getSelectedItems().get(i)));
        }
*/

        if (isAnEdit){
            Zone zoneToEdit = new Zone(this.idZone, getName());
            zoneToEdit.setRoles(selectedRoles);
            try {
                zoneService.editZone(zoneToEdit);
            }catch (Exception e){
                // TODO show Error Msg
            }
        }else{
            Zone zoneToAdd = new Zone();
            zoneToAdd.setTitle(getName());
            zoneToAdd.setRoles(selectedRoles);
            try {
                zoneService.saveZone(zoneToAdd);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }

    }

    // TODO Add Role to this function ILYAS
    public void fillData(String title, Long idZone, Set<Role> roles){
        this.idZone = idZone;
        tfName.setText(title);
        isAnEdit = true;

        List<String> roleNames = new ArrayList<>();
        for (Role role : roles)
            roleNames.add(role.getTitle());
        for (int i = 0 ; i < listRole.getItems().size() ; i++ ){
            if (roleNames.contains(listRole.getItems().get(i))){
                listRole.getSelectionModel().check(i,listRole.getItems().get(i));
            }

        }

    }


    public String getName() {
        return tfName.getText();
    }



}
