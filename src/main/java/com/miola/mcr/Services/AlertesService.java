package com.miola.mcr.Services;

import com.miola.mcr.Entities.Alerte;
import com.miola.mcr.Entities.Category;
import com.miola.mcr.Entities.Sensor;
import javafx.scene.control.Alert;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AlertesService {

    public Set<Alerte> TestAlerts(Sensor S, double payloadValue){
        Set<Alerte> triggeredAlertes = new HashSet<>();
        Category sensorCategory = S.getCategory();
        Set<Alerte> CategoryAlertes = sensorCategory.getAlerts();

        for (Alerte alerte : CategoryAlertes){
            if(alerte.getFromTime().equals("00:00:00") || alerte.getToTime().equals("00:00:00")) {
                if(checkValue(payloadValue,alerte) != null) triggeredAlertes.add(checkValue(payloadValue,alerte));
            }else{
                if(checkTime(alerte.getFromTime(),alerte.getToTime()))
                    if(checkValue(payloadValue,alerte) != null) triggeredAlertes.add(checkValue(payloadValue,alerte));
            }
        }
        return triggeredAlertes;
    }

    public boolean checkTime(String fromTime, String toTime){
        try {
            String string1 = "20:11:13";
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(toTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


            String string2 = "14:49:00";
            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(fromTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);



            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            String someRandomTime = "01:00:00";
            Date d = new SimpleDateFormat("HH:mm:ss").parse(dateFormat.format(date));
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);


            Date x = calendar3.getTime();
            System.out.println("after : "+calendar2.getTime());
            System.out.println("before : "+calendar1.getTime());
            System.out.println("time : "+x);
            if (x.after(calendar2.getTime()) && x.before(calendar1.getTime())) {
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                System.out.println(true);
                return true;
            }else return false;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Alerte checkValue(Double payloadValue, Alerte alerte){
        switch (alerte.getOperator()) {
            case "Less than":
               if(payloadValue < alerte.getValue())
                    return alerte;
                break;
            case "Greater than":
                if(payloadValue > alerte.getValue())
                    return alerte;
                break;
            case "Less than or equal to":
                if(payloadValue <= alerte.getValue())
                    return alerte;
                break;
            case "Greater than or equal to":
                if(payloadValue >= alerte.getValue())
                    return alerte;
                break;
            case "Equal to":
                if(payloadValue == alerte.getValue())
                    return alerte;
                break;
            case "Unequal to":
                if(payloadValue != alerte.getValue())
                    return alerte;
                break;
            default:
                return null;
        }
        return null;
    }

}
