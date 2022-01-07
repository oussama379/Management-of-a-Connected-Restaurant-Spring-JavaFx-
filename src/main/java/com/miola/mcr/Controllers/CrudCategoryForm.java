package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Category;
import com.miola.mcr.Services.CategoryService;
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
public class CrudCategoryForm implements Initializable {

    private final FxWeaver fxWeaver;
    private final CategoryService categoryService;

    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXTextField tfTitle;

    @FXML
    private MFXTextField tfDescription;

    private Boolean isAnEdit = false ; // to know difference in save function

    private Long idCategory;

    @Autowired
    public CrudCategoryForm(FxWeaver fxWeaver, CategoryService categoryService) {
        this.fxWeaver = fxWeaver;
        this.categoryService = categoryService;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set Validation
        this.setFieldsValidators();
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    void save(ActionEvent event) {
        Category CategoryToEditOrAdd = new Category();
        CategoryToEditOrAdd.setTitle(getTitle());
        CategoryToEditOrAdd.setDescription(getDescription());

        boolean fieldsValidation = tfTitle.isValid();
        if (fieldsValidation){
            if (isAnEdit){
                CategoryToEditOrAdd.setId(this.idCategory);
                if (categoryService.editCategory(CategoryToEditOrAdd)){
                    fxWeaver.getBean(CrudCategory.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudCategory.class).showAlter(0);
                }
            }else{
                if (categoryService.saveCategory(CategoryToEditOrAdd)){
                    fxWeaver.getBean(CrudCategory.class).showAlter(1);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(CrudCategory.class).showAlter(0);
                }
            }
        }else{
            fxWeaver.getBean(CrudCategory.class).showAlter(4);
        }
    }

    public void fillData(String title, Long id, String description){
        this.idCategory = id;
        tfTitle.setText(title);
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
        // Name TextField : Required
        tfTitle.setValidated(true);
        tfTitle.getValidator().add(
                BindingUtils.toProperty(tfTitle.textProperty().isNotEmpty()),
                "Required"
        );
    }

    public void clearFields(){
        tfTitle.clear();
        tfDescription.clear();
    }

    public String getTitle() {
        return tfTitle.getText();
    }
    public String getDescription() {
        return tfDescription.getText();
    }

}

