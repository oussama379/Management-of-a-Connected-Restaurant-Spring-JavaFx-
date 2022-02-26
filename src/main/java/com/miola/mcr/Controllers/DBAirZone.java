package com.miola.mcr.Controllers;

import io.github.palexdev.materialfx.controls.MFXLabel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@FxmlView
public class DBAirZone {

    @FXML private MFXLabel lblZone;
    @FXML private MFXLabel lblHum;
    @FXML private MFXLabel lblTemp;

//    private static List<MFXLabel> views = new ArrayList<>();
    public static Map<String, Pair<MFXLabel, MFXLabel>> views = new HashMap<>();

    public void fillData(String lblZone, double lblTemp, double lblHum){
        this.lblZone.setText(lblZone);
        this.lblTemp.setText(lblTemp+" °C");
        this.lblHum.setText(lblHum+" %");
        views.put(lblZone, new Pair<>(this.lblTemp,  this.lblHum));
    }

    public void updateData(String zoneName,double lblTemp, double lblHum){
        Pair<MFXLabel, MFXLabel> data = views.get(zoneName);
        data.getKey().setText(lblTemp+" °C");
        data.getValue().setText(lblHum+" %");
    }


    @FXML
    void selectThisZone(MouseEvent event) {
        DBAir.selectedZone.setValue(((MFXLabel) event.getSource()).getText());
    }
}
