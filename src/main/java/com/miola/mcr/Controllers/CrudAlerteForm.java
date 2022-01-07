package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Alerte;
import com.miola.mcr.Services.AlerteService;
import com.miola.mcr.Services.CategoryService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
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
public class CrudAlerteForm implements Initializable {

    private final FxWeaver fxWeaver;
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
    private MFXTextField tfSeverity; // TODO Oussama : Severity is a ComboBox with 3 values

    @FXML
    private MFXTextField tfValue;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idAlerte;

    @Autowired
    public CrudAlerteForm(FxWeaver fxWeaver, AlerteService alerteService, CategoryService categoryService) {
        this.fxWeaver = fxWeaver;
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

        // Set Validation
        this.setFieldsValidators();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    void save(ActionEvent event) {
        boolean fieldsValidation = tfType.isValid() && cbCategory.isValid();
        if (fieldsValidation){
            Alerte alerteToEditOrAdd = new Alerte();
            alerteToEditOrAdd.setType(getType());
            alerteToEditOrAdd.setSeverity(getSeverity());
            // TODO ILYAS : THE VALUE MUST BE A DOUBLE(OR INT)
            alerteToEditOrAdd.setValue(Double.valueOf(getValue()));
            alerteToEditOrAdd.setOperator(getOperator());
            alerteToEditOrAdd.setCategory(categoryService.getCategoryByName(getCategory()));
            
            if (isAnEdit){
                alerteToEditOrAdd.setId(this.idAlerte);
                if (alerteService.editAlerte(alerteToEditOrAdd)){
                    fxWeaver.getBean(CrudAlerte.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudAlerte.class).showAlter(0);
                }
            }else{
                if (alerteService.saveAlerte(alerteToEditOrAdd)){
                    fxWeaver.getBean(CrudAlerte.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudAlerte.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudAlerte.class).showAlter(4);
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

    public void closeWindow(){
        this.clearFields();
this.isAnEdit = false;
        Stage formWindow = (Stage) (tfType.getScene().getWindow());
        formWindow.fireEvent(new WindowEvent(formWindow, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setFieldsValidators() {
        // Name TextField : Required
        tfType.setValidated(true);
        tfType.getValidator().add(
                BindingUtils.toProperty(tfType.textProperty().isNotEmpty()),
                "Required"
        );

        // Cat ComboBox : Required
        cbCategory.setValidated(true);
        cbCategory.getValidator().add(
                BindingUtils.toProperty(cbCategory.getSelectionModel().selectedIndexProperty().isNotEqualTo(-1)),
                "A value must be selected"
        );
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

    public void clearFields(){
        tfType.clear();
        tfSeverity.clear();
        tfValue.clear();
        cbOperator.getSelectionModel().clearSelection();
        cbCategory.getSelectionModel().clearSelection();
    }
}

