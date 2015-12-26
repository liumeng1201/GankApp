package com.lm.android.gankapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.interfaces.OnContentItemClickListener;
import com.lm.android.gankapp.models.SharePlatItem;

import java.util.List;

/**
 * Created by liumeng on 2015/12/26.
 */
public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ViewHolder> {
    private Context context;
    private List<SharePlatItem> platList;

    private OnContentItemClickListener itemClickListener;

    public ShareListAdapter(List<SharePlatItem> platList) {
        this.platList = platList;
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
        holder.image.setImageResource(getItem(position).getPlatImage());
        holder.name.setText(getItem(position).getPlatName());
        holder.setOnClickListener(position);
    }

    @Override
    public int getItemCount() {
        return platList.size();
    }

    public SharePlatItem getItem(int position) {
        return platList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.listitem_share, parent, false));
            image = (ImageView) itemView.findViewById(R.id.listitem_share_image);
            name = (TextView) itemView.findViewById(R.id.listitem_share_name);
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
