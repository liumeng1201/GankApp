package com.lm.android.gankapp.models;

/**
 * Created by liumeng on 2015/12/15.
 */
public class ContentItemInfo {
    /**
     * who : mthli
     * publishedAt : 2015-12-15T05:01:30.531Z
     * desc : 水漫效果的 loading 动画
     * type : Android
     * url : https://github.com/lopspower/CircularFillableLoaders
     * used : true
     * objectId : 566f7dbc60b215d68be0ec6c
     * createdAt : 2015-12-15T02:41:00.558Z
     * updatedAt : 2015-12-15T05:01:32.330Z
     */
    private String who;
    private String publishedAt;
    private String desc;
    private String type;
    private String url;
    private boolean used;
    private String objectId;
    private String createdAt;
    private String updatedAt;

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

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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

    public boolean isUsed() {
        return used;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
