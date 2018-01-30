package com.larapin.kendaliatap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.larapin.kendaliatap.R;
import com.larapin.kendaliatap.entity.TeamItems;
import com.larapin.kendaliatap.preference.UserPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by asus on 24/12/2017.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    private ArrayList<TeamItems> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    public static final String IMAGE_TEAM="/hujan/foto";
    private UserPreference mUserPreference;
    private String host;

    public TeamAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<TeamItems> items){
        mData = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // ini
        mUserPreference = new UserPreference(parent.getContext());
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_items, parent, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // ini
        host = mUserPreference.getHost();
        Picasso.with(context).load("http://"+host+IMAGE_TEAM+mData.get(position).getFoto()).into(holder.imageTeam);
        holder.textViewNim.setText(mData.get(position).getNim());
        holder.textViewNama.setText(mData.get(position).getNama());
    }

    @Override
    public int getItemViewType(int position){
        return 0;
    }

    public TeamItems getItem(int position) {
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
        public ImageView imageTeam;
        public TextView textViewNim;
        public TextView textViewNama;

        public ViewHolder(View itemView) {
            super(itemView);
            imageTeam = (ImageView)itemView.findViewById(R.id.iv_foto);
            textViewNim = (TextView)itemView.findViewById(R.id.tv_nim);
            textViewNama = (TextView)itemView.findViewById(R.id.tv_nama);
        }
    }
}
