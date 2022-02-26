package com.miola.mcr.Controllers;

import com.miola.mcr.Services.DBEnergyService;
import com.miola.mcr.Services.DeviceService;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.TilesFXSeries;
import eu.hansolo.tilesfx.skins.BarChartItem;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@FxmlView
public class DBEnergy implements Initializable {

    private final FxWeaver fxWeaver;
    private final DeviceService deviceService;


    @FXML private AnchorPane rootPane;
    @FXML private HBox hbox1;
    @FXML private HBox hbox2;
    @FXML private HBox hboxDevices;


    private static final Random RND = new Random();

    public static boolean thereIsChanges;
    private long           lastTimerCall;
    private AnimationTimer timer;
    private static int hourIndex = Integer.valueOf(DateTimeFormatter.ofPattern("HH").format(LocalDateTime.now()));

    private Tile highLowTile;
    private Tile areaChartTile;
    private Tile barChartTile;

    private BarChartItem    barChartItem1;
    private BarChartItem    barChartItem2;
    private BarChartItem    barChartItem3;
    private BarChartItem    barChartItem4;

    @Autowired
    public DBEnergy(FxWeaver fxWeaver, DeviceService deviceService) {
        this.fxWeaver = fxWeaver;
        this.deviceService = deviceService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
         * Tiles
         * */
        highLowTile = TileBuilder.create()
                .skinType(SkinType.HIGH_LOW)
                .title("CHANGE IN COST")
                .unit("MAD")
                .description("Today's cost")
                .text("Compared to yesterday")
                .referenceValue(DBEnergyService.consumptionCostYesterday)
                .value(0)
                .build();

        // AreaChart Data
        XYChart.Series<String, Number> series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data(String.valueOf(hourIndex-1)+"H", 0));
        areaChartTile = TileBuilder.create()
                .skinType(SkinType.SMOOTHED_CHART)
                .maxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
                .title("USAGE TILL NOW")
                .chartType(Tile.ChartType.AREA)
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(series1,
                        Tile.BLUE,
                        new LinearGradient(0, 0, 0, 1,
                                true, CycleMethod.NO_CYCLE,
                                new Stop(0, Tile.BLUE),
                                new Stop(1, Color.TRANSPARENT))))
                .build();

        // BarChart Items
        barChartItem1 = new BarChartItem("-", 0, Tile.RED);
        barChartItem2 = new BarChartItem("-", 0, Tile.ORANGE);
        barChartItem3 = new BarChartItem("-", 0, Tile.GREEN);
        if(DBEnergyService.deviceConsumptionList.size() > 0) {
            Map.Entry<String, Double> item = DBEnergyService.deviceConsumptionList.get(0);
            barChartItem1 = new BarChartItem(item.getKey(), item.getValue(), Tile.RED);
        }
        if(DBEnergyService.deviceConsumptionList.size() > 1) {
            Map.Entry<String, Double> item = DBEnergyService.deviceConsumptionList.get(1);
            barChartItem2 = new BarChartItem(item.getKey(), item.getValue(), Tile.ORANGE);
        }
        if(DBEnergyService.deviceConsumptionList.size() > 2) {
            Map.Entry<String, Double> item = DBEnergyService.deviceConsumptionList.get(2);
            barChartItem3 = new BarChartItem(item.getKey(), item.getValue(), Tile.GREEN);
        }
        barChartItem1.setFormatString("%.1f kWh");
        barChartItem2.setFormatString("%.1f kWh");
        barChartItem3.setFormatString("%.1f kWh");
        barChartTile = TileBuilder.create()
                .skinType(SkinType.BAR_CHART)
                .title("TOP 3 DEVICES BY CONSUMPTION")
                .text("")
                .barChartItems(barChartItem1, barChartItem2, barChartItem3)
                .decimals(0)
                .build();

        /*
         * Timer
         * */
        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastTimerCall + 1.2e+10) {
                    highLowTile.setValue(DBEnergyService.consumptionCostYesterday);
                    highLowTile.setValue(DBEnergyService.consumptionToday * DBEnergyService.costForKwH);
                    series1.getData().add(new XYChart.Data(hourIndex+"H", DBEnergyService.consumptionToday));
                    hourIndex++;

                    lastTimerCall = now;
                }
            }
        };

        List<Pane> devices = new ArrayList<>();
        for (Map.Entry<String, Double> e: DBEnergyService.deviceConsumptionList) {
            devices.add(fxWeaver.loadView(DBEnergyDevice.class));
            fxWeaver.getBean(DBEnergyDevice.class).fillData(e.getKey(), Math.round(e.getValue())+"KWh", deviceService.getDeviceByName(e.getKey()).getPower());
        }

        hbox1.getChildren().addAll(highLowTile, barChartTile);
        hbox2.getChildren().add(areaChartTile);
        hboxDevices.getChildren().addAll(devices);

        hbox1.setHgrow(highLowTile, Priority.ALWAYS);
        hbox1.setHgrow(barChartTile, Priority.ALWAYS);
        hbox2.setHgrow(areaChartTile, Priority.ALWAYS);
        timer.start();
    }
}
