package com.miola.mcr.Controllers;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.miola.mcr.Dao.Order_MenuItemRepository;
import com.miola.mcr.Entities.*;
import com.miola.mcr.Services.DiningTableService;
import com.miola.mcr.Services.MenuItemService;
import com.miola.mcr.Services.OrderService;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
@FxmlView
public class Orders implements Initializable {

    @FXML private MFXComboBox<String> cbMenuItems;
    @FXML private MFXButton btRemoveSelectedItem;
    @FXML private MFXButton btAddItem;
    @FXML private MFXLabel labelPrice;
    @FXML private MFXTextField tfPrice;
    @FXML private JFXTextArea taSpecialRequest;
    @FXML private JFXTextArea taComments;
    @FXML private MFXButton btSubmitOrder;
    @FXML private JFXTreeTableView<MenuItem> tbMenuItems;
    @FXML private JFXTreeTableView<Order>  tbOrders;
    private final MenuItemService menuItemService;
    private final OrderService orderService;
    private final DiningTableService diningTableService;
    private final Order_MenuItemRepository order_menuItemRepository;
    private ObservableList<MenuItem> menuItems;
    private TreeItem<MenuItem> rootMenuItems ;
    private ObservableList<Order> orders;
    @FXML
    private MFXTableView<Order> tableView;
    @FXML
    private MFXComboBox<String> cbOrderMenuItems ;
    private final FxWeaver fxWeaver;
    private Stage stage;

    @Autowired
    public Orders(MenuItemService menuItemService, OrderService orderService, DiningTableService diningTableService, Order_MenuItemRepository order_menuItemRepository, FxWeaver fxWeaver) {
        this.menuItemService = menuItemService;
        this.orderService = orderService;
        this.diningTableService = diningTableService;
        this.order_menuItemRepository = order_menuItemRepository;
        this.fxWeaver = fxWeaver;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(Orders.class)));*/

        cbMenuItems.getItems().addAll(menuItemService.getAllMenuItemsNames());
        labelPrice.setText("00.00");

        JFXTreeTableColumn<MenuItem, String> titletColumn = new JFXTreeTableColumn<>("Item");
        titletColumn.setPrefWidth(100);
        titletColumn.setCellValueFactory(new TreeItemPropertyValueFactory<MenuItem, String>("title"));

        JFXTreeTableColumn<MenuItem, Double> priceColumn = new JFXTreeTableColumn<>("Price");
        priceColumn.setPrefWidth(100);
        priceColumn.setCellValueFactory(new TreeItemPropertyValueFactory<MenuItem, Double>("price"));

        JFXTreeTableColumn<MenuItem, String> DescriptioColumn = new JFXTreeTableColumn<>("Description");
        DescriptioColumn.setPrefWidth(150);
        DescriptioColumn.setCellValueFactory(new TreeItemPropertyValueFactory<MenuItem, String>("description"));

        menuItems = FXCollections.observableArrayList();

        rootMenuItems = new RecursiveTreeItem<>(menuItems, RecursiveTreeObject::getChildren);
        tbMenuItems.setRoot(rootMenuItems);
        tbMenuItems.setShowRoot(false);
        tbMenuItems.getSelectionModel().getSelectedCells();
        //tbMenuItems.getSelectionModel().

        tbMenuItems.getColumns().setAll(titletColumn, priceColumn, DescriptioColumn);

      //==========================================================================================\\

        populateTable();
        tableView.getSelectionModel().allowsMultipleSelection();

    }


    @FXML
    public void AddOrder(ActionEvent event) {
        Order order = new Order();
        ArrayList<MenuItem> menuItemSet = new ArrayList<>();
        menuItems.forEach(menuItem -> {menuItemSet.add(menuItem);});
        System.out.println(menuItemSet);
        order.setTotalPrice(Double.parseDouble(getTfPrice()));
        order.setComment(getTaComments());
        order.setSpecial_request(getTaSpecialRequest());
        order.setDate_time(String.valueOf(new Date()));
        // TODO ILYAS : PASSE SELECTED TABLE
        order.setDiningTable(diningTableService.getDiningTableByNumber(1));
        orderService.saveOrderItems(order, menuItemSet);
        menuItems.clear();
        orders = FXCollections.observableArrayList(orderService.getAllOrders());
        tableView.setItems(orders);

    }

    @FXML
    public void AddItem(ActionEvent event) {
        MenuItem M = menuItemService.getMenuItemByTitle(getCbMenuItems());
        if(M != null) {
            menuItems.add(M);
            if (!getTfPrice().isEmpty())
                labelPrice.setText(String.valueOf(Double.parseDouble(getTfPrice()) + M.getPrice()));
            else labelPrice.setText(String.valueOf(M.getPrice()));
        }
    }
    @FXML
    public void RemoveItem(ActionEvent event) {
        // TODO Fix Price
        if(tbMenuItems.getSelectionModel().selectedItemProperty().get() != null) {
            MenuItem M = tbMenuItems.getSelectionModel().selectedItemProperty().get().getValue();
            menuItems.remove(M);
            if (menuItems.isEmpty())
                labelPrice.setText("00.00");
            double i = 0;
            menuItems.forEach(menuItem -> {
                labelPrice.setText(String.valueOf(i + menuItem.getPrice()));
                System.out.println(menuItem);
            });
        }
    }

    @FXML
    public void DeleteOrder(ActionEvent event) {
        List<Order> selectedOrders = tableView.getSelectionModel().getSelectedItems(); // gives a List<Zone> of selected zones from tableView
        if (!selectedOrders.isEmpty()) {
            for (Order order : selectedOrders){
                orders.remove(order);
                order_menuItemRepository.deleteByOrder(order);
                orderService.deleteOrderById(order.getId());
            }
        }

    }
    @FXML
    public void EditOrder(ActionEvent event) {
        List<?> selectedItems = tableView.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty()) {
            Order orderToBeEdit = tableView.getSelectionModel().getSelectedItems().get(0);
            tfPrice.setText(String.valueOf(orderToBeEdit.getTotalPrice()));
            taSpecialRequest.setText(orderToBeEdit.getSpecial_request());
            taComments.setText(orderToBeEdit.getComment());

            for(Order_MenuItem order_menuItem :  orderToBeEdit.getOrder_menuItem()){
                for(int i = 0; i < order_menuItem.getQuantity() ; i++){
                    menuItems.add(order_menuItem.getMenuItem());
                }
            }

        }
          /*  fxWeaver.getBean(OrdersEditForm.class).fillData(orderToBeEdit.getId(), orderToBeEdit.getDiningTableName(), orderToBeEdit.getDate_time()
                , orderToBeEdit.getTotalPrice(), orderToBeEdit.getSpecial_request(), orderToBeEdit.getComment(), cbOrderMenuItems);*/



        /*formWindow.close();
        formWindow.setTitle("Edit Order");
        formWindow.showAndWait(); // blocks execution until the stage is closed*/

    }

    public String getCbMenuItems() {
        return cbMenuItems.getSelectedValue();
    }

    public String getTfPrice() {
        return labelPrice.getText();
    }

    public String getTaSpecialRequest() {
        return taSpecialRequest.getText();
    }

    public String getTaComments() {
        return taComments.getText();
    }


    public void populateTable() {
        /* load data */
        //ObservableList<User> data = FXCollections.observableArrayList(dummyData);
        orders = FXCollections.observableArrayList(orderService.getAllOrders());

        /* create columns */
        MFXTableColumn<Order> tableColumn = new MFXTableColumn<>("Table", Comparator.comparing(Order::getDiningTableName));
        MFXTableColumn<Order> dateColumn = new MFXTableColumn<>("Date", Comparator.comparing(Order::getDate_time));
        MFXTableColumn<Order> totalPriceColumn = new MFXTableColumn<>("Total Price", Comparator.comparing(Order::getTotalPrice));
        MFXTableColumn<Order> specialRequestColumn = new MFXTableColumn<>("Special Request", Comparator.comparing(Order::getSpecial_request));
        MFXTableColumn<Order> commentColumn = new MFXTableColumn<>("Comment", Comparator.comparing(Order::getComment));
        MFXTableColumn<Order> menuItemsColumn = new MFXTableColumn<>("Menu Items");


        /* link columns with proprieties */
        tableColumn.setRowCellFunction(order -> new MFXTableRowCell(order.getDiningTableName()));
        dateColumn.setRowCellFunction(order -> new MFXTableRowCell(order.getDate_time()));
        totalPriceColumn.setRowCellFunction(order -> new MFXTableRowCell(String.valueOf(order.getTotalPrice())));
        specialRequestColumn.setRowCellFunction(order -> new MFXTableRowCell(order.getSpecial_request()));
        commentColumn.setRowCellFunction(order -> new MFXTableRowCell(order.getComment()));

        menuItemsColumn.setRowCellFunction(order -> {
            MFXTableRowCell cell = new MFXTableRowCell("");
            cbOrderMenuItems = new MFXComboBox<>();
            for(Order_MenuItem order_menuItem : order.getOrder_menuItem())
                cbOrderMenuItems.getItems().add(order_menuItem.getQuantity()+"x "+order_menuItem.getMenuItem().getTitle());
            cbOrderMenuItems.setPromptText("See Menu Items");
            cell.setLeadingGraphic(cbOrderMenuItems);
            return cell;
        });



        /* fill table */
        tableView.setItems(orders);
        tableView.getTableColumns().addAll(tableColumn);
        tableView.getTableColumns().addAll(dateColumn);
        tableView.getTableColumns().addAll(totalPriceColumn);
        tableView.getTableColumns().addAll(specialRequestColumn);
        tableView.getTableColumns().addAll(commentColumn);
        tableView.getTableColumns().addAll(menuItemsColumn);

    }

}
