package com.ashkurpelo.weatherforecast;

import com.ashkurpelo.weatherforecast.model.GeoData;
import com.ashkurpelo.weatherforecast.mvp.MvpPresenter;
import com.ashkurpelo.weatherforecast.mvp.MvpView;
import com.ashkurpelo.weatherforecast.util.JsonParser;

public class MainContract {
    private MainContract(){}

    interface View extends MvpView{
        void alertDialogNoGPS();
        void aboutDialog();
        void requestGeoUpdate();
        void requestWeatherUpdate(GeoData geo);
        void updateView(JsonParser jsonParser);
        void saveWeatherLocally(String weatherJson);
        void tryRestoreWeather();
    }

    interface Presenter extends MvpPresenter<View>{
        void onCreated();
        void onResume();
        void onStop();

        void onGpsNotAvailable();
        void onGeoResult(GeoData geoData);
        void onBtnClick(int id);
        void onWeatherResult(String res, boolean fromOfflineStorage);

    }
}
