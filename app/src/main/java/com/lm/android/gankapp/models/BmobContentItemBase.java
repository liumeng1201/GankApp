package com.lm.android.gankapp.models;

import cn.bmob.v3.BmobObject;

/**
 * Created by liumeng on 2016/1/2.
 */
public class BmobContentItemBase extends BmobObject {
    private String who;
    private String publishedAt;
    private String desc;
    private String type;
    private String url;
    private String contentObjectId;

    public void setWho(String who) {
        this.who = who;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getContentObjectId() {
        return contentObjectId;
    }

    public void setContentObjectId(String contentObjectId) {
        this.contentObjectId = contentObjectId;
    }
}
