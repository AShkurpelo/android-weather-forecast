package com.ashkurpelo.weatherforecast.recycler;


import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashkurpelo.weatherforecast.R;
import com.ashkurpelo.weatherforecast.model.DailyForecastMap;
import com.ashkurpelo.weatherforecast.model.Forecast;
import com.ashkurpelo.weatherforecast.model.ForecastByHour;
import com.ashkurpelo.weatherforecast.util.TextHelpers;

import java.util.List;

public class DailyRecyclerAdapter extends RecyclerView.Adapter<DailyRecyclerAdapter.DailyViewHolder> {

    private List<ForecastByHour> list;
    private Typeface iconTypeface;

    public DailyRecyclerAdapter(DailyForecastMap dailyForecastMap, Typeface iconTypeface) {
        this.iconTypeface = iconTypeface;
        list = dailyForecastMap.asList();
        list.remove(0);//remove today's entry
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        if(list == null) return;
        ForecastByHour fbh = list.get(position);
        holder.setData(fbh);
        holder.setTypeface(iconTypeface);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class DailyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDayName;
        private TextView txtDescription;
        private TextView txtIcon;
        private TextView txtMinTemp;
        private TextView txtMaxTemp;

        public DailyViewHolder(View v) {
            super(v);
            txtDayName = v.findViewById(R.id.il_txtDayName);
            txtDescription = v.findViewById(R.id.il_txtDescription);
            txtIcon = v.findViewById(R.id.il_txtIcon);
            txtMaxTemp = v.findViewById(R.id.il_txtTempMax);
            txtMinTemp = v.findViewById(R.id.il_txtTempMin);
        }

        public void setTypeface(Typeface typeface) {
            getTxtIcon().setTypeface(typeface);
        }

        public void setData(ForecastByHour fbh) {
            Context context = getTxtDayName().getContext();
            Forecast midday = fbh.getMiddayForecast();
            getTxtDayName().setText(TextHelpers.GetShortDayString(fbh.getMiddayForecast()));
            String icName = TextHelpers.GetIconName(midday);
            getTxtIcon().setText(icName);
            if (TextHelpers.IsIconYellow(icName))
                getTxtIcon().setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
            else getTxtIcon().setTextColor(ContextCompat.getColor(context, R.color.colorIcon));
            getTxtDescription().setText(midday.getWeatherDescription());
            getTxtMinTemp().setText(TextHelpers.GetTemperatureString(fbh.getMinTemp()));
            getTxtMaxTemp().setText(TextHelpers.GetTemperatureString(fbh.getMaxTemp()));
        }

        public TextView getTxtDayName() {
            return txtDayName;
        }

        public TextView getTxtDescription() {
            return txtDescription;
        }


        public TextView getTxtIcon() {
            return txtIcon;
        }


        public TextView getTxtMinTemp() {
            return txtMinTemp;
        }


        public TextView getTxtMaxTemp() {
            return txtMaxTemp;
        }

    }
}
