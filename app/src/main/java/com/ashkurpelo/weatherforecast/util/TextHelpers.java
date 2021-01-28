package com.ashkurpelo.weatherforecast.util;

import com.ashkurpelo.weatherforecast.model.Forecast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TextHelpers {
    public final static String DegreeSign = "\u00B0";
    public static String GetDayDateString(Forecast forecast){
        return FirstToUpperCase(GetDayString(forecast)) + ", " + GetDateString(forecast);
    }
    public static String FirstToUpperCase(String s){
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
    public static String GetTemperatureString(double temp){
        return Math.round(temp) + DegreeSign;
    }
    public static String GetIconName(Forecast f){
//        return  "i" + f.getWeatherIcon();
        int wid = f.getWeatherId();
        String _s = f.getWeatherIcon();
        boolean day = _s.substring(_s.length()-1).equals("d");
        switch (f.getWeatherId()/100){
            case 8:
                if(wid == 800)
                    return day ? "\uf00d" : "\uf02e";
                else if(wid == 801)
                    return day ? "\uf002" : "\uf086";
                else if(wid == 802)
                    return "\uf041";
                else return  "\uf013";
            case 2:
                return "\uf01e";
            case 3:
                return "\uf01a";
            case 5:
                if(wid < 502) return "\uf019";
                else return "\uf01a";
            case 6:
                return "\uf01b";
            default: return "?";
            //TODO: case 9
        }

    }
    public static boolean IsIconYellow(String ic){
        return ic.equals("\uf00d") || ic.equals("\uf02e");
    }
    public static String GetDayString(Forecast forecast){
        return forecast.getCalendar().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }
    public static String GetShortDayString(Forecast forecast){
        return forecast.getCalendar().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }
    public static String GetDateString(Forecast forecast) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM", Locale.getDefault());
        return format1.format(forecast.getCalendar().getTime());
    }
}
