package com.example.petan.forecast_project_v2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.petan.forecast_project_v2.model.Forecast_day;
import com.example.petan.forecast_project_v2.R;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Forecast_day> mData;
    RequestOptions option;


    public RecycleViewAdapter(Context mContext, List<Forecast_day> mData) {
        this.mContext = mContext;
        this.mData = mData;
        option = new RequestOptions ().centerCrop ().placeholder (R.drawable.loading_shape).error (R.drawable.loading_shape);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from (mContext);
        //view = inflater.inflate (R.layout.forecast_day_item,parent,false);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_day_item, parent, false);
        return new MyViewHolder (view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_day.setText(mData.get(position).getDate ());
        holder.tv_temperature.setText (mData.get(position).getTemperature ());

        Glide.with (mContext).load (mData.get (position).getImage_url ()).apply (option).into(holder.img_forecast);

    }

    @Override
    public int getItemCount() {
        return mData.size ();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_day;
        TextView tv_temperature;
        ImageView img_forecast;

        public MyViewHolder(View itemView) {
            super (itemView);

            tv_day = itemView.findViewById (R.id.RCTV_day);
            tv_temperature = itemView.findViewById (R.id.RCTV_temperature);
            img_forecast = itemView.findViewById (R.id.RC_image);
        }

    }
}
