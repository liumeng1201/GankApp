package com.lm.android.gankapp.models;

/**
 * Created by liumeng on 2015/12/26.
 */
public class SharePlatItem {
    private int platImage;
    private String platName;

    public SharePlatItem(int platImage, String platName) {
        this.platImage = platImage;
        this.platName = platName;
    }

    public int getPlatImage() {
        return platImage;
    }

    public void setPlatImage(int platImage) {
        this.platImage = platImage;
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }
}
