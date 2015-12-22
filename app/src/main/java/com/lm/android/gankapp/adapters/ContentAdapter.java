package com.lm.android.gankapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.dao.DaoHelper;
import com.lm.android.gankapp.dao.ReadContentDao;
import com.lm.android.gankapp.interfaces.OnContentItemClickListener;
import com.lm.android.gankapp.models.ContentItemInfo;
import com.lm.android.gankapp.utils.ListUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.dao.query.Query;

/**
 * Created by liumeng on 2015/12/15.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'", Locale.CHINA);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private Context context;
    private ArrayList<ContentItemInfo> datas;
    private boolean bigImage;

    private OnContentItemClickListener itemClickListener;
    private ReadContentDao dao;

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

    public void setOnItemClickListener(OnContentItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        dao = DaoHelper.getDaoSession(context).getReadContentDao();
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
        holder.author.setText(getItemData(position).getWho());
        holder.time.setText(time);
        if (bigImage) {
            Glide.with(context).load(getItemData(position).getUrl()).centerCrop().crossFade().into(holder.image);
        } else {
            holder.title.setText(getItemData(position).getDesc());
        }
        Query query = dao.queryBuilder().where(ReadContentDao.Properties.ObjectId.eq(getItemData(position).getObjectId())).build();
        if (!ListUtils.isEmpty(query.list())) {
            if (!bigImage) {
                holder.title.setTextColor(context.getResources().getColor(R.color.medium_grey));
            }
            holder.author.setTextColor(context.getResources().getColor(R.color.medium_grey));
            holder.time.setTextColor(context.getResources().getColor(R.color.medium_grey));
        } else {
            if (!bigImage) {
                holder.title.setTextColor(context.getResources().getColor(R.color.dark_grey));
            }
            holder.author.setTextColor(context.getResources().getColor(R.color.dark_grey));
            holder.time.setTextColor(context.getResources().getColor(R.color.dark_grey));
        }
        holder.setOnClickListener(position);
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
        public TextView time;
        public TextView author;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(bigImage ? R.layout.listitem_content_image : R.layout.listitem_content_normal, parent, false));

            if (bigImage) {
                image = (ImageView) itemView.findViewById(R.id.list_avatar);
            } else {
                title = (TextView) itemView.findViewById(R.id.list_title);
            }
            time = (TextView) itemView.findViewById(R.id.list_time);
            author = (TextView) itemView.findViewById(R.id.list_author);
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
