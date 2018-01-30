package com.larapin.kendaliatap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.larapin.kendaliatap.R;
import com.larapin.kendaliatap.entity.CuacaItems;
import com.larapin.kendaliatap.preference.UserPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by asus on 25/12/2017.
 */

public class CuacaAdapter extends RecyclerView.Adapter<CuacaAdapter.ViewHolder> {
    private ArrayList<CuacaItems> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    public static final String IMAGE_CUACA = "/hujan/images";
    private UserPreference mUserPreference;
    private String host;
    
    public CuacaAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public void setData(ArrayList<CuacaItems> items){
        mData = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // ini
        mUserPreference = new UserPreference(parent.getContext());
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.cuaca_items, parent, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // ini
        host = mUserPreference.getHost();
        Picasso.with(context).load("http://"+host+IMAGE_CUACA+mData.get(position).getCuaca()).into(holder.imageCuaca);
        holder.textViewWaktu.setText(mData.get(position).getWaktu());
        holder.textViewSuhu.setText(mData.get(position).getSuhu());
        holder.textViewKelembaban.setText(mData.get(position).getKelembaban());
        holder.textViewMode.setText(mData.get(position).getMode());
        holder.textViewAtap.setText(mData.get(position).getAtap());
    }

    @Override
    public int getItemViewType(int position){
        return 0;
    }

    public CuacaItems getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        //ini
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageCuaca;
        public TextView textViewWaktu;
        public TextView textViewSuhu;
        public TextView textViewKelembaban;
        public TextView textViewAtap;
        public TextView textViewMode;

        public ViewHolder(View itemView) {
            super(itemView);
            imageCuaca = (ImageView)itemView.findViewById(R.id.iv_cuaca);
            textViewWaktu = (TextView)itemView.findViewById(R.id.tv_waktu);
            textViewSuhu = (TextView)itemView.findViewById(R.id.tv_suhu);
            textViewKelembaban = (TextView)itemView.findViewById(R.id.tv_kelembaban);
            textViewAtap = (TextView)itemView.findViewById(R.id.tv_atap);
            textViewMode = (TextView)itemView.findViewById(R.id.tv_mode);
        }
    }

}
