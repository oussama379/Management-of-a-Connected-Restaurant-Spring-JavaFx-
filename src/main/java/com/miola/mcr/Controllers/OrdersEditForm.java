package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.MenuItem;
import com.miola.mcr.Entities.Order;
import com.miola.mcr.Services.DiningTableService;
import com.miola.mcr.Services.OrderService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.BindingUtils;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView
public class OrdersEditForm implements Initializable {

    private final FxWeaver fxWeaver;
    private final DiningTableService diningTableService;
    private final OrderService orderService;


    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXTextField tfIdOrder;

    @FXML
    private MFXComboBox<String> cbTable;

    @FXML
    private MFXTextField tfPrice;

    @FXML
    private MFXTextField tfDate;

    @FXML
    private MFXTextField tfSpecialRequest;

    @FXML
    private MFXTextField tfComment;

    @FXML
    private MFXComboBox<String> cbMenuItems;

    private ArrayList<MenuItem> menuItemSet ;



    @Autowired
    public OrdersEditForm(FxWeaver fxWeaver, DiningTableService diningTableService, OrderService orderService) {
        this.fxWeaver = fxWeaver;
        this.diningTableService = diningTableService;
        this.orderService = orderService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setFieldsValidators();
        tfIdOrder.setEditable(false);
        tfDate.setEditable(false);
    }

    @FXML
    void cancel(ActionEvent event) {
        this.closeWindow();
    }

    @FXML
    void save(ActionEvent event) {
        boolean fieldsValidation = tfPrice.isValid();
        if (fieldsValidation){
            Order orderToEdit = new Order();
            orderToEdit.setId(Long.valueOf(getTfIdOrder()));
            orderToEdit.setTotalPrice(Double.parseDouble(getTfPrice()));
            orderToEdit.setComment(getTfComment());
            orderToEdit.setSpecial_request(getTfSpecialRequest());
            orderToEdit.setDate_time(getTfDate());
            orderToEdit.setDiningTable(diningTableService.getDiningTableByNumber(Integer.parseInt(getCbTable())));
                if (orderService.saveOrderItems(orderToEdit, menuItemSet)){
                    fxWeaver.getBean(Orders.class);
                    this.closeWindow();
                }else{
                    fxWeaver.getBean(Orders.class);
                }
        }else{
            fxWeaver.getBean(Orders.class);
        }


    }

    public void fillData(Long id, String diningTableName, String date_time, double totalPrice,
                         String special_request, String comment, MFXComboBox<String> cbOrderMenuItems
            //, ArrayList<MenuItem> menuItemSetx
    ) {
        System.out.println(id);
        tfIdOrder.setText(String.valueOf(id));

        cbTable.getItems().setAll(diningTableService.getAllTablesNumbers());
        cbTable.setSelectedValue(diningTableName);
        cbTable.setPromptText(diningTableName);

        tfPrice.setText(String.valueOf(totalPrice));
        tfDate.setText(date_time);
        tfSpecialRequest.setText(special_request);
        tfComment.setText(comment);
        cbMenuItems.getItems().setAll(cbOrderMenuItems.getItems());
        cbMenuItems.setPromptText("See Menu Items");
    }


    public void closeWindow(){
        this.clearFields();
        Stage formWindow = (Stage) (tfIdOrder.getScene().getWindow());
        formWindow.fireEvent(new WindowEvent(formWindow, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setFieldsValidators() {

        tfPrice.setValidated(true);
        tfPrice.getValidator().add(
                BindingUtils.toProperty(tfPrice.textProperty().isNotEmpty()),
                "Required"
        );
    }

    public void clearFields(){
        tfIdOrder.clear();
        tfPrice.clear();
        tfDate.clear();
        tfSpecialRequest.clear();
        tfComment.clear();
    }


    public String getTfIdOrder() {
        return tfIdOrder.getText();
    }

    public String getCbTable() {
        return cbTable.getSelectedValue();
    }

    public String getTfPrice() {
        return tfPrice.getText();
    }

    public String getTfDate() {
        return tfDate.getText();
    }

    public String getTfSpecialRequest() {
        return tfSpecialRequest.getText();
    }

    public String getTfComment() {
        return tfComment.getText();
    }
}

