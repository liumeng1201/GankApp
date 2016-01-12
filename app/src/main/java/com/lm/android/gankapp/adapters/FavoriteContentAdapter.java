package com.lm.android.gankapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.listener.OnContentItemClickListener;
import com.lm.android.gankapp.models.FavoriteModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liumeng on 2015/12/15.
 */
public class FavoriteContentAdapter extends RecyclerView.Adapter<FavoriteContentAdapter.ViewHolder> {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private Context context;
    private ArrayList<FavoriteModel> datas;

    private OnContentItemClickListener itemClickListener;

    /**
     * @param datas 要显示的数据
     */
    public FavoriteContentAdapter(ArrayList<FavoriteModel> datas) {
        this.datas = datas;
    }

    /**
     * @param datas 要显示的数据
     */
    public void refresh(ArrayList<FavoriteModel> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnContentItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        long time = getItemData(position).getFavoriteAt();
        Date date = new Date(time);
        holder.time.setText(sdf.format(date));
        holder.title.setText(getItemData(position).getDesc());
        holder.setOnClickListener(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public FavoriteModel getItemData(int position) {
        return datas.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView time;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.listitem_content_normal, parent, false));
            title = (TextView) itemView.findViewById(R.id.list_title);
            time = (TextView) itemView.findViewById(R.id.list_time);
        }

        public void setOnClickListener(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickListener(itemView, position);
                }
            });
        }
    }
}
