package com.lm.android.gankapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lm.android.gankapp.R;
import com.umeng.fb.model.Reply;

import java.util.List;

/**
 * Created by liumeng on 2016/1/14.
 */
public class ConversionListAdapter extends RecyclerView.Adapter<ConversionListAdapter.ViewHolder> {
    public static final int CONVERSION_TYPE_USER = 301;
    public static final int CONVERSION_TYPE_DEVELOPER = 302;

    private List<Reply> conversion;

    public ConversionListAdapter(List<Reply> conversion) {
        this.conversion = conversion;
    }

    public void refresh(List<Reply> conversion) {
        this.conversion = conversion;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutRes = R.layout.layout_fb_devloper;
        switch (viewType) {
            case CONVERSION_TYPE_USER:
                layoutRes = R.layout.layout_fb_user;
                break;
            case CONVERSION_TYPE_DEVELOPER:
                layoutRes = R.layout.layout_fb_devloper;
                break;
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(layoutRes, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.content.setText(getItemData(position).content);
    }

    @Override
    public int getItemCount() {
        return conversion.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemData(position).type.equalsIgnoreCase(Reply.TYPE_DEV_REPLY)) {
            return CONVERSION_TYPE_DEVELOPER;
        }
        return CONVERSION_TYPE_USER;
    }

    public Reply getItemData(int position) {
        return conversion.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView content;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            switch (type) {
                case CONVERSION_TYPE_USER:
                    content = (TextView) itemView.findViewById(R.id.conversion_reply_user);
                    break;
                case CONVERSION_TYPE_DEVELOPER:
                    content = (TextView) itemView.findViewById(R.id.conversion_reply_developer);
                    break;
            }
        }
    }
}
