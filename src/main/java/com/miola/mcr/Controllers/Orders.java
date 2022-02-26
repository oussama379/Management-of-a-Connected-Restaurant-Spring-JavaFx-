package com.miola.mcr.Controllers;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.miola.mcr.Dao.Order_MenuItemRepository;
import com.miola.mcr.Dao.UserRepository;
import com.miola.mcr.Entities.*;
import com.miola.mcr.Services.DiningTableService;
import com.miola.mcr.Services.MenuItemService;
import com.miola.mcr.Services.OrderService;
import com.miola.mcr.Services.UserService;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
@FxmlView
public class Orders implements Initializable {

    @FXML private MFXComboBox<String> cbMenuItems;
    @FXML private MFXComboBox<String> cbStaff;
    @FXML private MFXButton btRemoveSelectedItem;
    @FXML private MFXButton btAddItem;
    @FXML private MFXLabel labelPrice;
    @FXML private MFXTextField tfPrice;
    @FXML private JFXTextArea taSpecialRequest;
    @FXML private JFXTextArea taComments;
    @FXML private MFXButton btSubmitOrder;
    @FXML private MFXButton btCancelEdit;
    @FXML private JFXTreeTableView<MenuItem> tbMenuItems;
    @FXML private JFXTreeTableView<Order>  tbOrders;
    private final MenuItemService menuItemService;
    private final OrderService orderService;
    private final DiningTableService diningTableService;
    private final UserService userService;
    private final Order_MenuItemRepository order_menuItemRepository;
    private final UserRepository userRepository;
    private ObservableList<MenuItem> menuItems;
    private TreeItem<MenuItem> rootMenuItems ;
    private ObservableList<Order> orders;
    @FXML private MFXTableView<Order> tableView;
    @FXML private MFXTableView<String> tbStaffActivity;
    @FXML private MFXComboBox<String> cbOrderMenuItems ;

    // Dinning Area
    @FXML private FlowPane paneTables;
    @FXML private MFXScrollPane scrollPaneTables;
    @FXML private AnchorPane orderPane;
    private static final Map<Long, MFXButton> sensors = new HashMap<>();
    private static final Map<MFXButton, String> tables = new HashMap<>();
    private static final Map<MFXButton, Integer> tablesNumbers = new HashMap<>();
    private static final Map<Long, Boolean> sensorsLastValue = new HashMap<>();
    private static final Map<String, String> colors = new HashMap<>();
    private static boolean emptyMaps = true;
    private int selectedTable;

    private final FxWeaver fxWeaver;
    private Stage stage;
    private boolean isEdit;
    private Long idOrderEdit;

    @Autowired
    public Orders(MenuItemService menuItemService, OrderService orderService, DiningTableService diningTableService, UserService userService, Order_MenuItemRepository order_menuItemRepository, UserRepository userRepository, FxWeaver fxWeaver) {
        this.menuItemService = menuItemService;
        this.orderService = orderService;
        this.diningTableService = diningTableService;
        this.userService = userService;
        this.order_menuItemRepository = order_menuItemRepository;
        this.userRepository = userRepository;
        this.fxWeaver = fxWeaver;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (emptyMaps){
            emptyMaps = false;
            // Fill colors
            colors.put("empty", "-fx-background-color: Lavender; -fx-font-size: 20");
            colors.put("taken", "-fx-background-color: LightSalmon; -fx-font-size: 20");
            colors.put("served", "-fx-background-color: LightGreen; -fx-font-size: 20");
            colors.put("dirty", "-fx-background-color: Sienna; -fx-font-size: 20");

            // Fill tables
            for (DiningTable dt : diningTableService.getAllDiningTables()) {
                MFXButton b = new MFXButton("Table "+dt.getNumber(),200,100);
                FontIcon f = new FontIcon("fas-th-large");
                f.setIconSize(30);
                b.setGraphic(f);
                b.setContentDisplay(ContentDisplay.TOP);
                b.setStyle(colors.get("empty"));
                b.setOnAction(this::nextStateWaiter);
                tables.put(b, "empty");
                tablesNumbers.put(b, dt.getNumber());
                // Fill sensors
                for (Sensor s: dt.getSensors()) {
                    sensors.put(s.getId(), b);
                    sensorsLastValue.put(s.getId(), false);
                }
            }
        }
        paneTables.getChildren().addAll(tables.keySet());

        /*formWindow = new Stage();
        formWindow.setScene(new Scene(fxWeaver.loadView(Orders.class)));*/

        cbMenuItems.getItems().addAll(menuItemService.getAllMenuItemsNames());
        cbStaff.setPromptText("Select an Item");
        cbStaff.getItems().addAll(userService.getAllUsersNames());
        cbStaff.setPromptText("Select a member of staff");


        MFXTableColumn<String> activitiesColumn = new MFXTableColumn<>("Activities");
        activitiesColumn.setPrefWidth(1000);
        activitiesColumn.setRowCellFunction(string -> new MFXTableRowCell(string.toString()));
        tbStaffActivity.getTableColumns().addAll(activitiesColumn);


        cbStaff.selectedValueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldname, String newName) {
                ArrayList<String> activities = new ArrayList<>();
                User user = userRepository.findByUsername(newName);
                System.out.println(user);
                for(Order order : user.getOrders()){
                    for(Order_MenuItem order_menuItem : order.getOrder_menuItem()){
                        activities.add(user.getUsername() + ", added x"+order_menuItem.getQuantity()+" "
                                +order_menuItem.getMenuItem().getTitle()+" to table "+order.getDiningTableName()+" at : "+
                                order.getDate_time());
                    }
                }
                System.out.println(activities);
                ObservableList<String> activitiess = FXCollections.observableArrayList(activities);
                tbStaffActivity.setItems(activitiess);

            }
        });



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
        if(isEdit) {
            order.setId(idOrderEdit);
            order.setDiningTable(orderService.getOrderById(idOrderEdit).getDiningTable());
            isEdit = false;
            idOrderEdit = null;
        }else {
            order.setDiningTable(diningTableService.getDiningTableByNumber(selectedTable));
            MFXButton table = getKey(tablesNumbers, selectedTable);
            table.setStyle(colors.get("served"));
            tables.put(table, "served");
        }
        ArrayList<MenuItem> menuItemSet = new ArrayList<>();
        menuItems.forEach(menuItem -> {menuItemSet.add(menuItem);});
        //System.out.println(menuItemSet);
        order.setTotalPrice(Double.parseDouble(getTfPrice()));
        order.setComment(getTaComments());
        order.setSpecial_request(getTaSpecialRequest());
        order.setDate_time(String.valueOf(new Date()));
        order.setUser(userRepository.findById(Login.idCurrentUser).orElse(null));

        orderService.saveOrderItems(order, menuItemSet);

        menuItems.clear();
        rootMenuItems = new RecursiveTreeItem<>(menuItems, RecursiveTreeObject::getChildren);
        tbMenuItems.setRoot(rootMenuItems);

        orders = FXCollections.observableArrayList(orderService.getAllOrders());
        tableView.setItems(orders);


        taComments.clear();
        taSpecialRequest.clear();
        labelPrice.setText("");
        cbStaff.setPromptText("Select an Item");

        orderPane.setVisible(false);
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
        System.out.println(menuItems);
        if(!menuItems.isEmpty() || tbMenuItems.getSelectionModel().getSelectedItem() != null) {
            MenuItem M = tbMenuItems.getSelectionModel().getSelectedItems().get(0).getValue();
            menuItems.remove(M);

            rootMenuItems = new RecursiveTreeItem<>(menuItems, RecursiveTreeObject::getChildren);
            tbMenuItems.setRoot(rootMenuItems);
                double i = 0;
                for(MenuItem menuItem : menuItems)
                    i = i + menuItem.getPrice();
            labelPrice.setText(String.valueOf(i));
        }
        if (menuItems.isEmpty())
            labelPrice.setText("");
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
            labelPrice.setText(String.valueOf(orderToBeEdit.getTotalPrice()));
            taSpecialRequest.setText(orderToBeEdit.getSpecial_request());
            taComments.setText(orderToBeEdit.getComment());

            for(Order_MenuItem order_menuItem :  orderToBeEdit.getOrder_menuItem()){
                for(int i = 0; i < order_menuItem.getQuantity() ; i++){
                    menuItems.add(order_menuItem.getMenuItem());
                }
            }
            System.out.println(menuItems);
            idOrderEdit = orderToBeEdit.getId();
            isEdit = true;
        }
        orderPane.setVisible(true);
    }

    @FXML
    public void CancelEdit(ActionEvent event) {
        menuItems.clear();
        rootMenuItems = new RecursiveTreeItem<>(menuItems, RecursiveTreeObject::getChildren);
        tbMenuItems.setRoot(rootMenuItems);

        taComments.clear();
        taSpecialRequest.clear();
        labelPrice.setText("");

        isEdit = false;
        cbStaff.setPromptText("Select an Item");

        orderPane.setVisible(false);
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

    /*
     * Waiter function
     * */
    public void nextStateWaiter(ActionEvent actionEvent){
        MFXButton table = ((MFXButton)actionEvent.getSource());
        if (tables.get(table).equals("taken")){
//            table.setStyle(colors.get("served"));
//            tables.put(table, "served");
            orderPane.setVisible(true);
            selectedTable = tablesNumbers.get(table);
        }
        if (tables.get(table).equals("dirty")){
            table.setStyle(colors.get("empty"));
            tables.put(table, "empty");
        }
    }

    /*
     * Payload function
     * */
    public static void nextStatePayload(long id, boolean forced){
        MFXButton table = sensors.get(id);
        List<Map.Entry<Long, MFXButton>> sensorsOfTable = sensors.entrySet().stream().filter(longMFXButtonEntry -> longMFXButtonEntry.getValue() == table).toList();
        sensorsLastValue.put(id, forced);
        if (tables.get(table).equals("empty") && forced){
            table.setStyle(colors.get("taken"));
            tables.put(table, "taken");
        }
        boolean allSensorsAreFalse = true ;
        // To make sure all sensors has forced = false
        for (Map.Entry<Long, Boolean> s: sensorsLastValue.entrySet()) {
            if (sensorsOfTable.contains(Map.entry(s.getKey(), table))){
                if (s.getValue()) allSensorsAreFalse = false;
            }
        }
        if (tables.get(table).equals("served") && allSensorsAreFalse){
            table.setStyle(colors.get("dirty"));
            tables.put(table, "dirty");
        }
        if (tables.get(table).equals("taken") && allSensorsAreFalse){
            table.setStyle(colors.get("empty"));
            tables.put(table, "empty");
        }
    }

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
