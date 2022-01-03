package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Alerte;
import com.miola.mcr.Entities.User;
import com.miola.mcr.Services.AlerteService;
import com.miola.mcr.Services.CategoryService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView
public class CrudAlerteForm implements Initializable {

    private final AlerteService alerteService;
    private final CategoryService categoryService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXComboBox<String> cbOperator;

    @FXML
    private MFXComboBox<String> cbCategory;

    @FXML
    private MFXTextField tfType;

    @FXML
    private MFXTextField tfSeverity;

    @FXML
    private MFXTextField tfValue;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idAlerte;

    @Autowired
    public CrudAlerteForm(AlerteService alerteService, CategoryService categoryService) {
        this.alerteService = alerteService;
        this.categoryService = categoryService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cbCategory.getItems().addAll(categoryService.getAllCategoriesNames());
        cbOperator.getItems().addAll("Less than",
                "Greater than", "Less than or equal to",
                "Greater than or equal to",
                "Equal to", "Unequal to");
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        Alerte alerteToEditOrAdd = new Alerte();
        alerteToEditOrAdd.setType(getType());
        alerteToEditOrAdd.setSeverity(getSeverity());
        // TODO ILYAS : THE VALUE MUST BE A DOUBLE(OR INT)
        alerteToEditOrAdd.setValue(Double.valueOf(getValue()));
        alerteToEditOrAdd.setOperator(getOperator());
        alerteToEditOrAdd.setCategory(categoryService.getCategoryByName(getCategory()));

        if (isAnEdit){
            alerteToEditOrAdd.setId(this.idAlerte);
            try {
                alerteService.editAlerte(alerteToEditOrAdd);
                // TODO show Confirmation Msg
            }catch (Exception e){
                // TODO show Error Msg
            }
        }else{
           try {
               alerteService.saveAlerte(alerteToEditOrAdd);
               // TODO show Confirmation Msg
           }catch (Exception e){
               // TODO show Error Msg
           }
        }

    }


    public void fillData(String type, Long id, String severity, String value, String operator, String category){
        this.idAlerte = id;
        tfType.setText(type);
        tfSeverity.setText(severity);
        tfValue.setText(value);
        cbOperator.setSelectedValue(operator);
        cbOperator.setPromptText(operator);

        cbCategory.setSelectedValue(category);
        cbCategory.setPromptText(category);
        isAnEdit = true;
    }


    public String getType() {
        return tfType.getText();
    }

    public String getSeverity() {
        return tfSeverity.getText();
    }

    public String getValue() {
        return tfValue.getText();
    }

    public String getCategory() {
        return cbCategory.getSelectedValue();
    }

    public String getOperator() {
        return cbOperator.getSelectedValue();
    }
}

