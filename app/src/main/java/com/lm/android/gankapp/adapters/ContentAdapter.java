package com.lm.android.gankapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.activities.DetailActivity;
import com.lm.android.gankapp.models.ContentItemInfo;

import java.util.ArrayList;

/**
 * Created by liumeng on 2015/12/15.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ContentItemInfo> datas;

    public ContentAdapter(ArrayList<ContentItemInfo> datas) {
        this.datas = datas;
    }

    public void refresh(ArrayList<ContentItemInfo> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Glide.with(context).load(getItemData(position).getUrl()).centerCrop().crossFade().into(holder.image);
        holder.title.setText(String.format(context.getString(R.string.title_content_listitem),
                getItemData(position).getDesc(),
                getItemData(position).getWho(),
                getItemData(position).getPublishedAt()));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public ContentItemInfo getItemData(int position) {
        return datas.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.listitem_content, parent, false));

            image = (ImageView) itemView.findViewById(R.id.list_avatar);
            title = (TextView) itemView.findViewById(R.id.list_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
