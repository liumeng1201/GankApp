package com.lm.android.gankapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.listener.OnContentItemClickListener;
import com.lm.android.gankapp.models.UserInfoModel;
import com.lm.android.gankapp.utils.StringUtils;

import java.util.List;

/**
 * Created by liumeng on 2016/1/6.
 */
public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    public static final int USER_INFO_TYPE_AVATAR = 201;
    public static final int USER_INFO_TYPE_NORMAL_TEXT = 202;
    public static final int USER_INFO_TYPE_SNS_ACCOUNT = 203;

    private Context context;
    private List<UserInfoModel> userInfo;

    private OnContentItemClickListener itemClickListener;

    public UserInfoAdapter(List<UserInfoModel> userInfo) {
        this.userInfo = userInfo;
    }

    public void refresh(List<UserInfoModel> userInfo) {
        this.userInfo = userInfo;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnContentItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutRes = R.layout.listitem_userinfo_text;
        switch (viewType) {
            case USER_INFO_TYPE_AVATAR:
                layoutRes = R.layout.listitem_userinfo_avatar;
                break;
            case USER_INFO_TYPE_NORMAL_TEXT:
                layoutRes = R.layout.listitem_userinfo_text;
                break;
            case USER_INFO_TYPE_SNS_ACCOUNT:
                layoutRes = R.layout.listitem_userinfo_sns;
                break;
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(layoutRes, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        UserInfoModel item = getItemData(position);
        switch (getItemViewType(position)) {
            case USER_INFO_TYPE_AVATAR:
                Glide.with(context).load(item.getValue()).asBitmap().error(R.mipmap.default_avatar).centerCrop().into(new BitmapImageViewTarget(holder.avatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.avatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
                break;
            case USER_INFO_TYPE_NORMAL_TEXT:
                holder.title.setText(item.getTitle());
                if (StringUtils.isEmpty(item.getValue())) {
                    holder.content.setText("请填写" + item.getTitle());
                } else {
                    holder.content.setText(item.getValue());
                }
                break;
            case USER_INFO_TYPE_SNS_ACCOUNT:
                holder.title.setText(item.getTitle());
                String account = item.getValue();
                if (account.charAt(0) == '1') {
                    holder.wechat.setImageResource(R.drawable.ssdk_oks_skyblue_logo_wechat_checked);
                } else {
                    holder.wechat.setImageResource(R.drawable.ssdk_oks_skyblue_logo_wechat);
                }
                if (account.charAt(1) == '1') {
                    holder.qq.setImageResource(R.drawable.ssdk_oks_skyblue_logo_qq_checked);
                } else {
                    holder.qq.setImageResource(R.drawable.ssdk_oks_skyblue_logo_qq);
                }
                if (account.charAt(2) == '1') {
                    holder.weibo.setImageResource(R.drawable.ssdk_oks_skyblue_logo_sinaweibo_checked);
                } else {
                    holder.weibo.setImageResource(R.drawable.ssdk_oks_skyblue_logo_sinaweibo);
                }
                break;
        }
        holder.setOnClickListener(position);
    }

    @Override
    public int getItemViewType(int position) {
        return userInfo.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return userInfo.size();
    }

    public UserInfoModel getItemData(int position) {
        return userInfo.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView title;
        public TextView content;
        public ImageView wechat;
        public ImageView qq;
        public ImageView weibo;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            switch (type) {
                case USER_INFO_TYPE_AVATAR:
                    avatar = (ImageView) itemView.findViewById(R.id.user_info_avatar);
                    break;
                case USER_INFO_TYPE_NORMAL_TEXT:
                    title = (TextView) itemView.findViewById(R.id.user_info_title);
                    content = (TextView) itemView.findViewById(R.id.user_info_content);
                    break;
                case USER_INFO_TYPE_SNS_ACCOUNT:
                    title = (TextView) itemView.findViewById(R.id.user_info_title);
                    wechat = (ImageView) itemView.findViewById(R.id.user_info_sns_wechat);
                    qq = (ImageView) itemView.findViewById(R.id.user_info_sns_qq);
                    weibo = (ImageView) itemView.findViewById(R.id.user_info_sns_weibo);
                    break;
            }
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
