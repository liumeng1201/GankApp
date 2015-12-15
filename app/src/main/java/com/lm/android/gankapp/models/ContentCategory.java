package com.lm.android.gankapp.models;

/**
 * Created by liumeng on 2015/12/15.
 */
public enum ContentCategory {
    ANDROID(1), IOS(2), WEB(3), EXPAND(4), VIDEO(5), MEIZI(6);

    private int type;
    private ContentCategory(int _type) {
        this.type = _type;
    }

    public int getType() {
        return this.type;
    }
}
