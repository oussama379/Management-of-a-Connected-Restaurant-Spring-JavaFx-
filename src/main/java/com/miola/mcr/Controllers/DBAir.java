package com.miola.mcr.Controllers;

import com.miola.mcr.Services.DBAirService;
import com.miola.mcr.Services.DBEnergyService;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.TilesFXSeries;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.Dark;
import io.github.palexdev.materialfx.controls.MFXLabel;
import javafx.animation.AnimationTimer;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Pair;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;

@Component
@FxmlView
public class DBAir implements Initializable {

    @Autowired
    private FxWeaver fxWeaver;

    @FXML private HBox hbox1;
    @FXML private GridPane gpTiles;
    @FXML private MFXLabel lblTitle;
    @FXML private VBox vboxZones;

    public static StringProperty selectedZone;

    private static final Random RND = new Random();

    private long           lastTimerCall;
    private static AnimationTimer timer;
    private static int hourIndex = Integer.parseInt(DateTimeFormatter.ofPattern("HH").format(LocalDateTime.now()));

    private Tile areaChartTileTemp;
    private Tile barGaugeTileCO2;
    private Tile barGaugeTileVOC;
    private Tile smoothAreaChartTileCO2;
    private Tile smoothAreaChartTileVOC;

    private ChartData smoothChartData1;
    private ChartData       smoothChartData2;
    private ChartData       smoothChartData3;
    private ChartData       smoothChartData4;

    private static XYChart.Series<String, Number> series1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedZone = lblTitle.textProperty();
        if (DBAirService.ZoneData_Temp_Humi.size() > 0)
            selectedZone.setValue((String) DBAirService.ZoneData_Temp_Humi.keySet().toArray()[0]);
        /*
        * Tiles
        * */
        series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), 0));
        areaChartTileTemp = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .maxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
                .title("Temperature")
                .chartType(Tile.ChartType.AREA)
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(series1,
                        Tile.RED,
                        new LinearGradient(0, 0, 0, 1,
                                true, CycleMethod.NO_CYCLE,
                                new Stop  (0, Tile.LIGHT_RED),
                                new Stop(1, Color.TRANSPARENT))))
                .build();
        barGaugeTileCO2 = TileBuilder.create()
                .skinType(Tile.SkinType.BAR_GAUGE)
                .minValue(400)
                .maxValue(2500)
                .startFromZero(false)
                .threshold(1000)
                .thresholdVisible(false)
                .title("CO2 Level")
                .unit("PPM")
                .gradientStops(new Stop(0, Bright.BLUE),
                        new Stop(0.1, Bright.BLUE_GREEN),
                        new Stop(0.2, Bright.GREEN),
                        new Stop(0.3, Bright.GREEN_YELLOW),
                        new Stop(0.4, Bright.YELLOW),
                        new Stop(0.5, Bright.YELLOW_ORANGE),
                        new Stop(0.6, Bright.ORANGE),
                        new Stop(0.7, Bright.ORANGE_RED),
                        new Stop(0.8, Bright.RED),
                        new Stop(1.0, Dark.RED))
                .strokeWithGradient(true)
                .animated(true)
                .build();

        barGaugeTileVOC = TileBuilder.create()
                .skinType(Tile.SkinType.BAR_GAUGE)
                .minValue(400)
                .maxValue(2500)
                .startFromZero(false)
                .threshold(700)
                .thresholdVisible(false)
                .title("VOC Level")
                .unit("PPO")
                .gradientStops(new Stop(0, Bright.BLUE),
                        new Stop(0.1, Bright.BLUE_GREEN),
                        new Stop(0.2, Bright.GREEN),
                        new Stop(0.3, Bright.GREEN_YELLOW),
                        new Stop(0.4, Bright.YELLOW),
                        new Stop(0.5, Bright.YELLOW_ORANGE),
                        new Stop(0.6, Bright.ORANGE),
                        new Stop(0.7, Bright.ORANGE_RED),
                        new Stop(0.8, Bright.RED),
                        new Stop(1.0, Dark.RED))
                .strokeWithGradient(true)
                .animated(true)
                .build();

//        smoothChartData1 = new ChartData("Item 1", RND.nextDouble() * 25, Tile.BLUE);
//        smoothChartData2 = new ChartData("Item 2", RND.nextDouble() * 25, Tile.BLUE);
//        smoothChartData3 = new ChartData("Item 3", RND.nextDouble() * 25, Tile.BLUE);
//        smoothChartData4 = new ChartData("Item 4", RND.nextDouble() * 25, Tile.BLUE);
//
//        smoothAreaChartTileCO2 = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
//                .minValue(400)
//                .maxValue(2500)
//                .title("SmoothAreaChart Tile")
//                .unit("Unit")
//                .text("Test")
////                .chartType(Tile.ChartType.LINE)
////                .dataPointsVisible(true)
//                .chartData(smoothChartData1, smoothChartData2, smoothChartData3, smoothChartData4)
//                .tooltipText("")
//                .animated(true)
//                .build();
//
//        smoothAreaChartTileVOC = TileBuilder.create().skinType(Tile.SkinType.SMOOTH_AREA_CHART)
//                .minValue(400)
//                .maxValue(2500)
//                .title("SmoothAreaChart Tile")
//                .unit("Unit")
//                .text("Test")
////                .chartType(Tile.ChartType.LINE)
////                .dataPointsVisible(true)
//                .chartData(smoothChartData1, smoothChartData2, smoothChartData3, smoothChartData4)
//                .tooltipText("")
//                .animated(true)
//                .build();

        /*
         * Timer
         * */
        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastTimerCall + 3_500_000_000L) {
                    if (DBAirService.ZoneData_Temp_Humi.size() > 0) {
                        series1.getData().add(new XYChart.Data(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), Double.parseDouble(DBAirService.ZoneData_Temp_Humi.get(selectedZone.get()).get(0))));
//                        hourIndex++;
                        if (series1.getData().size() > 10) series1.getData().remove(0);
                        if (hourIndex > 23) hourIndex = 0;

                        barGaugeTileCO2.setValue(Double.parseDouble(DBAirService.ZoneData_co2_voc.get(selectedZone.get()).get(0)));
                        barGaugeTileVOC.setValue(Double.parseDouble(DBAirService.ZoneData_co2_voc.get(selectedZone.get()).get(1)));

                        //                    smoothChartData1.setValue(smoothChartData2.getValue());
                        //                    smoothChartData2.setValue(smoothChartData3.getValue());
                        //                    smoothChartData3.setValue(smoothChartData4.getValue());
                        //                    smoothChartData4.setValue(RND.nextDouble() * 2500);

                        Set<Map.Entry<String, ArrayList<String>>> zones = DBAirService.ZoneData_Temp_Humi.entrySet();
                        for (Map.Entry<String, ArrayList<String>> e : zones) {
                            fxWeaver.getBean(DBAirZone.class).updateData(e.getKey(), Math.round(Double.parseDouble(e.getValue().get(0))), Math.round(Double.parseDouble(e.getValue().get(1))));
                        }

                        gpTiles.setVisible(true);
                        hbox1.setVisible(true);
                    }

                    lastTimerCall = now;
                }
            }
        };

        barGaugeTileCO2.getStyleClass().add("hbox-border");
        barGaugeTileVOC.getStyleClass().add("hbox-border");
        gpTiles.add(barGaugeTileCO2, 0, 0);
        gpTiles.add(barGaugeTileVOC, 1, 0);
//        gpTiles.add(smoothAreaChartTileCO2, 0, 1);
//        gpTiles.add(smoothAreaChartTileVOC, 1, 1);
        hbox1.getChildren().add(areaChartTileTemp);
        hbox1.setHgrow(areaChartTileTemp, Priority.ALWAYS);

        gpTiles.setVisible(false);
        hbox1.setVisible(false);
        timer.start();

        List<Pane> list = new ArrayList<>();
        Set<Map.Entry<String, ArrayList<String>>> zones = DBAirService.ZoneData_Temp_Humi.entrySet();
        for (Map.Entry<String, ArrayList<String>> e: zones) {
            list.add(fxWeaver.loadView(DBAirZone.class));
            fxWeaver.getBean(DBAirZone.class).fillData(e.getKey(), Math.round(Double.parseDouble(e.getValue().get(0))), Math.round(Double.parseDouble(e.getValue().get(1))));
        }
        vboxZones.getChildren().addAll(list);
    }

}
