package com.lm.android.gankapp.component.infiniteindicator.indicator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lm.android.gankapp.component.infiniteindicator.salvage.RecyclingPagerAdapter;
import com.lm.android.gankapp.component.infiniteindicator.slideview.BaseSliderView;

import java.util.ArrayList;

public class RecyleAdapter extends RecyclingPagerAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<BaseSliderView> mSlederViews;
    private boolean isLoop = true;
    DataChangeListener mDataChangeListener;

    public RecyleAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mSlederViews = new ArrayList<BaseSliderView>();
    }

    public int getRealCount() {
        return mSlederViews.size();
    }

    public <T extends BaseSliderView> void addSlider(T slider) {
        mSlederViews.add(slider);
        notifyDataSetChanged();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    public int getPosition(int position) {
        return isLoop ? position % getRealCount() : position;
    }

    @Override
    public int getCount() {
        return isLoop ? getRealCount() * 100 : getRealCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        return ((BaseSliderView) mSlederViews.get(getPosition(position))).getView();
    }

    public <T extends BaseSliderView> void removeSlider(T slider) {
        if (mSlederViews.contains(slider)) {
            mSlederViews.remove(slider);
            notifyDataSetChanged();
        }
    }

    public void removeSliderAt(int position) {
        if (mSlederViews.size() < position) {
            mSlederViews.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeAllSliders() {
        mSlederViews.clear();
        notifyDataSetChanged();
    }

    /**
     * @return the is Loop
     */
    public boolean isLoop() {
        return isLoop;
    }

    /**
     * @param isLoop the is InfiniteLoop to set
     */
    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
        notifyDataSetChanged();
    }

}
