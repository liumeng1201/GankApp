package com.lm.android.gankapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.listener.OnContentItemClickListener;
import com.lm.android.gankapp.models.FavoriteModel;
import com.lm.android.gankapp.utils.StringUtils;

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
    private boolean showFilterData = false;
    private String filter;

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
        this.showFilterData = false;
        this.filter = null;
        this.notifyDataSetChanged();
    }

    public void refresh(ArrayList<FavoriteModel> datas, String filter, boolean showFilterData) {
        this.datas = datas;
        this.showFilterData = showFilterData;
        this.filter = filter;
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
        if (getItemData(position).isShowFavorite()) {
            holder.itemView.setVisibility(View.VISIBLE);
            long time = getItemData(position).getFavoriteAt();
            Date date = new Date(time);
            holder.time.setText(sdf.format(date));
            if (showFilterData && !StringUtils.isEmpty(filter)) {
                SpannableStringBuilder styled = new SpannableStringBuilder(filter);
                styled.setSpan(new ForegroundColorSpan(Color.RED), 0, filter.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.title.setText(styled);
            } else {
                holder.title.setText(getItemData(position).getDesc());
            }
            holder.setOnClickListener(position);
        } else {
            holder.itemView.setVisibility(View.GONE);
        }
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
