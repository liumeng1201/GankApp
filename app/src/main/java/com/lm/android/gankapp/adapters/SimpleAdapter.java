package com.lm.android.gankapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.listener.OnContentItemClickListener;

import java.util.List;

/**
 * Created by liumeng on 2016/1/9.
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
    private List<String> datas;
    private OnContentItemClickListener itemClickListener;

    public SimpleAdapter(List<String> datas) {
        this.datas = datas;
    }

    public void refresh(List<String> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void setItemClickListener(OnContentItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_simple, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(getItemData(position));
        holder.setOnItemClickListener(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public String getItemData(int position) {
        return datas.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview);
        }

        public void setOnItemClickListener(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickListener(itemView, position);
                }
            });
        }
    }
}
