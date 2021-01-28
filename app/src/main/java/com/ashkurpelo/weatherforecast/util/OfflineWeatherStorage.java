package com.ashkurpelo.weatherforecast.util;


import android.content.Context;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class OfflineWeatherStorage {
    private static String fileName = "weather.json";

    @Nullable
    public static String Read(Context context){
        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName);
            if(!f.exists()) return null;
            //check whether file exists
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            e.fillInStackTrace();
            return null;
        }
    }

    public static void Write(Context context, String data){
        try {
            FileWriter file = new FileWriter(context.getFilesDir().getPath() + "/" + fileName);
            file.write(data);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }
}
