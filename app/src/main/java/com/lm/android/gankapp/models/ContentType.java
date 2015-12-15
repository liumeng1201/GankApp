package com.lm.android.gankapp.models;

/**
 * Created by liumeng on 2015/12/15.
 */
public enum ContentType {
    NET(1), DB(2);

    private int type;
    private ContentType(int _type) {
        this.type = _type;
    }

    public int getType() {
        return this.type;
    }
}
