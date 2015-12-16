package com.lm.android.gankapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.activities.DetailActivity;
import com.lm.android.gankapp.models.ContentItemInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liumeng on 2015/12/15.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'", Locale.CHINA);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private Context context;
    private ArrayList<ContentItemInfo> datas;
    private boolean bigImage;

    /**
     * @param datas 要显示的数据
     */
    public ContentAdapter(ArrayList<ContentItemInfo> datas) {
        this.datas = datas;
    }

    /**
     * @param datas    要显示的数据
     * @param bigImage 是否为大图片数据
     */
    public ContentAdapter(ArrayList<ContentItemInfo> datas, boolean bigImage) {
        this.datas = datas;
        this.bigImage = bigImage;
    }

    /**
     * @param datas 要显示的数据
     */
    public void refresh(ArrayList<ContentItemInfo> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    /**
     * @param datas    要显示的数据
     * @param bigImage 是否为大图片数据
     */
    public void refresh(ArrayList<ContentItemInfo> datas, boolean bigImage) {
        this.datas = datas;
        this.bigImage = bigImage;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String time = getItemData(position).getPublishedAt();
        try {
            Date date = df.parse(time);
            time = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (bigImage) {
            Glide.with(context).load(getItemData(position).getUrl()).centerCrop().crossFade().into(holder.image);
            holder.title.setText(String.format(context.getString(R.string.title_content_listitem_bigimage),
                    getItemData(position).getWho(),
                    time));
        } else {
            holder.title.setText(String.format(context.getString(R.string.title_content_listitem_normal),
                    getItemData(position).getDesc(),
                    getItemData(position).getWho(),
                    time));
        }
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
            super(inflater.inflate(bigImage ? R.layout.listitem_content_image : R.layout.listitem_content_normal, parent, false));

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
