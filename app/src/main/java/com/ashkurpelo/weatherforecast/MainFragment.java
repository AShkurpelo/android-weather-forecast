package com.ashkurpelo.weatherforecast;


import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ashkurpelo.weatherforecast.model.Forecast;
import com.ashkurpelo.weatherforecast.model.GeoData;
import com.ashkurpelo.weatherforecast.recycler.DailyRecyclerAdapter;
import com.ashkurpelo.weatherforecast.model.ForecastByHour;
import com.ashkurpelo.weatherforecast.util.TextHelpers;
import com.ashkurpelo.weatherforecast.util.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    ImageButton btnUpdate;
    ImageButton btnAbout;
    TextView txtLargeIcon;
    TextView txtLargeTemp;
    TextView txtCity;
    TextView txtDescription;
    TextView txtDayDate;

    TextView txtTempMorning;
    TextView txtTempDay;
    TextView txtTempEvening;
    TextView txtTempNight;

    TextView txtHumidity;
    TextView txtWind;
    TextView icHumidity;
    TextView icWind;
    TextView txtMaxTemp;
    TextView txtMinTemp;
    TextView icMaxTemp;
    TextView icMinTemp;

    TextView ic_morning;
    TextView ic_day;
    TextView ic_evening;
    TextView ic_night;

    TextView txtLastUpdate;

    Typeface typefaceIcons;


    RecyclerView recyclerView;
    JsonParser jsonParser;
    private GeoData geoData;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        typefaceIcons = Typeface.createFromAsset(inflater.getContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    void setDefaultIcon(TextView target) {
        target.setTypeface(typefaceIcons);
        target.setText(R.string.i_sunny);
    }

    void setIcon(TextView target, int strId) {
        target.setTypeface(typefaceIcons);
        target.setText(strId);
    }

    void setIcon(TextView target, String str) {
        target.setTypeface(typefaceIcons);
        target.setText(str);
        if (TextHelpers.IsIconYellow(str)) {
            target.setTextColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
        } else {
            target.setTextColor(ContextCompat.getColor(getContext(), R.color.colorIcon));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBtnClick(v);
            }
        };
        btnUpdate = (ImageButton) view.findViewById(R.id.btnRefresh);
        btnUpdate.setOnClickListener(listener);
        btnAbout = (ImageButton) view.findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(listener);

        txtLargeIcon = (TextView) view.findViewById(R.id.txtLargeIcon);
        setDefaultIcon(txtLargeIcon);
        txtLargeTemp = (TextView) view.findViewById(R.id.txtLargeTemp);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        txtCity = (TextView) view.findViewById(R.id.txtCityName);
        txtDayDate = (TextView) view.findViewById(R.id.txtDayDate);

        ic_morning = (TextView) view.findViewById(R.id.ic_morning);
        ic_day = (TextView) view.findViewById(R.id.ic_day);
        ic_evening = (TextView) view.findViewById(R.id.ic_evening);
        ic_night = (TextView) view.findViewById(R.id.ic_night);
        setDefaultIcon(ic_morning);
        setDefaultIcon(ic_day);
        setDefaultIcon(ic_evening);
        setDefaultIcon(ic_night);

        txtTempMorning = (TextView) view.findViewById(R.id.txt_temp_morning);
        txtTempDay = (TextView) view.findViewById(R.id.txt_temp_day);
        txtTempEvening = (TextView) view.findViewById(R.id.txt_temp_evening);
        txtTempNight = (TextView) view.findViewById(R.id.txt_temp_night);

        txtHumidity = (TextView) view.findViewById(R.id.txtHumidity);
        txtWind = (TextView) view.findViewById(R.id.txtWind);
        icHumidity = (TextView) view.findViewById(R.id.ic_humidity_lbl);
        icWind = (TextView) view.findViewById(R.id.ic_wind_lbl);
        setIcon(icHumidity, R.string.i_humidity);
        setIcon(icWind, R.string.i_wind);

        txtMaxTemp = (TextView) view.findViewById(R.id.txtMaxTemp);
        txtMinTemp = (TextView) view.findViewById(R.id.txtMinTemp);
        icMaxTemp = (TextView) view.findViewById(R.id.ic_max_temp);
        icMinTemp = (TextView) view.findViewById(R.id.ic_min_temp);
        setIcon(icMaxTemp, R.string.i_max_temp);
        setIcon(icMinTemp, R.string.i_min_temp);

        txtLargeTemp.setText("18" + getString(R.string.celcius));

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(lm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                lm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        txtLastUpdate = (TextView) view.findViewById(R.id.txtLastUpdate);


    }


    @Override
    public void onResume() {

        if (jsonParser != null) updateView();
        super.onResume();
    }

    public void updateView(JsonParser jsonParser, GeoData geo) {
        setData(jsonParser, geo);
        updateView();
        
    }

    public void updateView() {
        if (jsonParser == null ||
                txtLargeTemp == null ||
                jsonParser.getForecastByDay().isEmpty()) {
            Log.e(TAG, "updateView() failed");
            return;
        }
        ForecastByHour fbh = jsonParser.getForecastByDay().firstEntry().getValue();
        Forecast current = fbh.firstEntry().getValue();
        if (current.getWeatherId() == 800) {
            txtLargeIcon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
        } else {
            txtLargeIcon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGrey));
        }
        setIcon(txtLargeIcon, TextHelpers.GetIconName(current));
        txtLargeTemp.setText(TextHelpers.GetTemperatureString(current.getTemperature()));
        if (geoData != null) {
            txtCity.setText(geoData.locationName);
            Log.d(TAG, "Location name (localized): " + geoData.locationNameLocalized);
            Log.d(TAG, "Location name: " + geoData.locationName);
        }
        else Log.d(TAG, "GeoData is null");
        txtDescription.setText(TextHelpers.FirstToUpperCase(current.getWeatherDescription()));
        txtDayDate.setText(TextHelpers.GetDayDateString(current));

        setTodayTemperatures(fbh);

        txtHumidity.setText(current.getHumidity() + "");
        String windStr = Math.round(current.getWind_speed() * 100) / 100.0 + "m/s";
        txtWind.setText(windStr);

        DailyRecyclerAdapter adapter = new DailyRecyclerAdapter(jsonParser.getForecastByDay(), typefaceIcons);
        recyclerView.setAdapter(adapter);

        if (geoData != null && geoData.lastUpdate != null) {
            String lastUpdateString = "Последнее обновление: ";
            SimpleDateFormat format1 = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());
            lastUpdateString += format1.format(geoData.lastUpdate.getTime());
            txtLastUpdate.setText(lastUpdateString);
        }
    }

    private void setTodayTemperatures(ForecastByHour fbh) {
        //morning
        if (fbh.size() >= 7) {
            Forecast f = fbh.get(6);
            txtTempMorning.setText(TextHelpers.GetTemperatureString(f.getTemperature()));
            setIcon(ic_morning, TextHelpers.GetIconName(f));
        } else {
            txtTempMorning.setText("-");
            ic_morning.setVisibility(View.INVISIBLE);
        }
        //day
        if (fbh.size() >= 5) {
            Forecast f = fbh.get(12);
            txtTempDay.setText(TextHelpers.GetTemperatureString(f.getTemperature()));
            setIcon(ic_day, TextHelpers.GetIconName(f));
        } else if (fbh.size() >= 4) {
            Forecast f = fbh.get(15);
            txtTempDay.setText(TextHelpers.GetTemperatureString(f.getTemperature()));
            setIcon(ic_day, TextHelpers.GetIconName(f));
        } else {
            txtTempDay.setText("-");
            ic_day.setVisibility(View.INVISIBLE);
        }
        //evening
        if (fbh.size() >= 3) {
            Forecast f = fbh.get(18);
            txtTempEvening.setText(TextHelpers.GetTemperatureString(f.getTemperature()));
            setIcon(ic_evening, TextHelpers.GetIconName(f));
        } else if (fbh.size() >= 2) {
            Forecast f = fbh.get(21);
            txtTempEvening.setText(TextHelpers.GetTemperatureString(f.getTemperature()));
            setIcon(ic_evening, TextHelpers.GetIconName(f));
        } else {
            txtTempEvening.setText("-");
            ic_evening.setVisibility(View.INVISIBLE);
        }

        double minTemp = fbh.getMinTemp();
        if (jsonParser.getForecastByDay().size() > 1) {
            Forecast f = jsonParser.getForecastByDay().get(1).firstEntry().getValue();
            double temperature = f.getTemperature();
            txtTempNight.setText(TextHelpers.GetTemperatureString(temperature));
            setIcon(ic_night, TextHelpers.GetIconName(f));
            if (temperature < minTemp) minTemp = temperature;
        } else {
            txtTempNight.setText("-");
            ic_night.setVisibility(View.INVISIBLE);
        }

        txtMaxTemp.setText(TextHelpers.GetTemperatureString(fbh.getMaxTemp()));
        txtMinTemp.setText(TextHelpers.GetTemperatureString(minTemp));
    }

    public void setData(JsonParser jsonParser, GeoData geo) {
        this.jsonParser = jsonParser;
        this.geoData = geo;
    }
}
