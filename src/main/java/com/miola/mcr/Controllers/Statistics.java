package com.miola.mcr.Controllers;

import com.miola.mcr.Entities.Order;
import com.miola.mcr.Services.DBEnergyService;
import com.miola.mcr.Services.OrderService;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@Component
@FxmlView
public class Statistics implements Initializable {

    private final OrderService orderService;

    @FXML private HBox hbox1;
    @FXML private HBox hbox2;
    @FXML private HBox hbox3;

    private static final Random RND = new Random();
    private AnimationTimer timer;
    private long           lastTimerCall;

    private Tile textToday;
    private Tile textMonth;
    private Tile numberTileTodayOrders;
    private Tile numberTileTodayProfit;
    private Tile numberTileMonthOrders;
    private Tile numberTileMonthProfit;

    private Tile lineChartTileMonth;
    private Tile lineChartTileYear;

    private int todayCount;
    private double todayProfit;
    private int monthCount;
    private double monthProfit;

    private static List<Order> ordersList;
    private static List<Order> todayOrdersList;
    private static List<Order> monthOrdersList;
    private static Map<Integer, List<Order>> daysOfMonthOrdersList;
    private static Map<Integer, List<Order>> monthOfYearOrdersList;


    @Autowired
    public Statistics(OrderService orderService) {
        this.orderService = orderService;
        daysOfMonthOrdersList = new HashMap<>();
        monthOfYearOrdersList = new HashMap<>();
        refresh();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        textToday = TileBuilder.create()
//                .skinType(Tile.SkinType.TEXT)
//                .prefSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
//                .description("TODAY")
//                .descriptionAlignment(Pos.CENTER)
//                .build();
        numberTileTodayOrders = TileBuilder.create()
                .skinType(Tile.SkinType.NUMBER)
                .prefSize(200, Double.POSITIVE_INFINITY)
                .title("Today Orders")
                .value(todayCount)
                .unit("order")
                .build();
        numberTileTodayProfit = TileBuilder.create()
                .skinType(Tile.SkinType.NUMBER)
                .prefSize(200, Double.POSITIVE_INFINITY)
                .title("Today Profit")
                .value(todayProfit)
                .unit("DH")
                .build();

//        textMonth = TileBuilder.create()
//                .skinType(Tile.SkinType.TEXT)
//                .prefSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
//                .description("THIS MONTH")
//                .descriptionAlignment(Pos.CENTER)
//                .build();
        numberTileMonthOrders = TileBuilder.create()
                .skinType(Tile.SkinType.NUMBER)
                .prefSize(200, Double.POSITIVE_INFINITY)
                .title("This Month Orders")
                .value(monthCount)
                .unit("order")
                .build();
        numberTileMonthProfit = TileBuilder.create()
                .skinType(Tile.SkinType.NUMBER)
                .prefSize(200, Double.POSITIVE_INFINITY)
                .title("This Month Profit")
                .value(monthProfit)
                .unit("DH")
                .build();

        // LineChart Month Data
        XYChart.Series<String, Number> seriesDaysOfMonth = new XYChart.Series();
        seriesDaysOfMonth.setName("Inside");
        for (int i = 1; i < LocalDate.now().getDayOfMonth()+1; i++) {
            seriesDaysOfMonth.getData().add(new XYChart.Data(String.valueOf(i), daysOfMonthOrdersList.get(i).size()));
        }
        lineChartTileMonth = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .maxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
                .title("SmoothedChart Tile")
                .animated(true)
                .smoothing(false).dataPointsVisible(true)
                .series(seriesDaysOfMonth)
                .build();

        // LineChart Year Data
        XYChart.Series<String, Number> seriesMonthsOfYear = new XYChart.Series();
        seriesDaysOfMonth.setName("Inside");
        for (int i = 0; i < LocalDate.now().getMonthValue(); i++) {
            seriesMonthsOfYear.getData().add(new XYChart.Data(String.valueOf(i+1), monthOfYearOrdersList.get(i).size()));
        }
        lineChartTileYear = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .maxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
                .title("SmoothedChart Tile")
                .animated(true)
                .smoothing(false)
                .series(seriesMonthsOfYear)
                .build();

        /*
         * Timer
         * */
        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastTimerCall + 1.2e+10) {
                    refresh();
                    numberTileTodayOrders.setValue(todayCount);
                    numberTileTodayProfit.setValue(todayProfit);
                    numberTileMonthOrders.setValue(monthCount);
                    numberTileMonthProfit.setValue(monthProfit);

                    seriesDaysOfMonth.getData().get(LocalDate.now().getDayOfMonth()-1).setYValue(daysOfMonthOrdersList.get(LocalDate.now().getDayOfMonth()).size());
                    seriesMonthsOfYear.getData().get(LocalDate.now().getMonthValue()-1).setYValue(monthOfYearOrdersList.get(LocalDate.now().getMonthValue()-1).size());

                    lastTimerCall = now;
                }
            }
        };



        Separator separator = new Separator(Orientation.VERTICAL); separator.prefWidth(50);
        hbox1.getChildren().addAll(numberTileTodayOrders, numberTileTodayProfit, separator ,numberTileMonthOrders, numberTileMonthProfit);
        hbox1.setHgrow(numberTileTodayOrders, Priority.ALWAYS);
        hbox1.setHgrow(numberTileTodayProfit, Priority.ALWAYS);
        hbox1.setHgrow(numberTileMonthOrders, Priority.ALWAYS);
        hbox1.setHgrow(numberTileMonthProfit, Priority.ALWAYS);

        hbox2.getChildren().add(lineChartTileMonth);
        hbox2.setHgrow(lineChartTileMonth, Priority.ALWAYS);

        hbox3.getChildren().add(lineChartTileYear);
        hbox3.setHgrow(lineChartTileYear, Priority.ALWAYS);

        timer.start();
    }

    public void refresh(){
        Predicate<Order> todayPredicate = order -> {
            Calendar dateToday = Calendar.getInstance();
            dateToday.setTime(new Date());

            DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
            Date d = null;
            try {
                d = format.parse(order.getDate_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.get(Calendar.YEAR) == dateToday.get(Calendar.YEAR)
                    && c.get(Calendar.MONTH) == dateToday.get(Calendar.MONTH)
                    && c.get(Calendar.DAY_OF_MONTH) == dateToday.get(Calendar.DAY_OF_MONTH);
        };
        Predicate<Order> monthPredicate = order -> {
            Calendar dateToday = Calendar.getInstance();
            dateToday.setTime(new Date());

            DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
            Date d = null;
            try {
                d = format.parse(order.getDate_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.get(Calendar.YEAR) == dateToday.get(Calendar.YEAR)
                    && c.get(Calendar.MONTH) == dateToday.get(Calendar.MONTH);
        };

        try{
            ordersList = orderService.getAllOrders();
        }catch (Exception ignored){}
        todayOrdersList = ordersList.stream().filter(todayPredicate).toList();
        monthOrdersList = ordersList.stream().filter(monthPredicate).toList();

        todayCount  = todayOrdersList.size();
        todayProfit = 0;
        for (Order o: todayOrdersList) {
            todayProfit += o.getTotalPrice();
        }

        monthCount  = monthOrdersList.size();
        monthProfit = 0;
        for (Order o: monthOrdersList) {
            monthProfit += o.getTotalPrice();
        }

        for (int i = 1; i < 32; i++) {
            int finalI = i;
            Predicate<Order> p = order -> {
                Calendar dateToday = Calendar.getInstance();
                dateToday.setTime(new Date());

                DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
                Date d = null;
                try {
                    d = format.parse(order.getDate_time());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                return c.get(Calendar.YEAR) == dateToday.get(Calendar.YEAR)
                        && c.get(Calendar.MONTH) == dateToday.get(Calendar.MONTH)
                        && c.get(Calendar.DAY_OF_MONTH) == finalI;
            };
            daysOfMonthOrdersList.put(i, ordersList.stream().filter(p).toList());
        }

        for (int i = 0; i < 12; i++) {
            int finalI = i;
            Predicate<Order> p = order -> {
                Calendar dateToday = Calendar.getInstance();
                dateToday.setTime(new Date());

                DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
                Date d = null;
                try {
                    d = format.parse(order.getDate_time());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                return c.get(Calendar.YEAR) == dateToday.get(Calendar.YEAR)
                        && c.get(Calendar.MONTH) == finalI;
            };
            monthOfYearOrdersList.put(i, ordersList.stream().filter(p).toList());
        }

    }

}
