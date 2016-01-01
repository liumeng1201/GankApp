package com.lm.android.gankapp.models;

/**
 * Created by liumeng on 2015/12/15.
 */
public enum ThirdPartyOptType {
    LOGIN(1), SHARE(2);

    private int type;
    ThirdPartyOptType(int _type) {
        this.type = _type;
    }

    public int getType() {
        return this.type;
    }
}
