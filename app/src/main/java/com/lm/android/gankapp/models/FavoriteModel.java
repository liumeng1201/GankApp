package com.lm.android.gankapp.models;

/**
 * Created by liumeng on 2016/1/12.
 */
public class FavoriteModel {
    private String contentObjectId;
    private String createdAt;
    private String desc;
    private long favoriteAt;
    private String objectId;
    private String updatedAt;
    private String url;
    private String type;
    private boolean showFavorite;

    public void setContentObjectId(String contentObjectId) {
        this.contentObjectId = contentObjectId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setFavoriteAt(long favoriteAt) {
        this.favoriteAt = favoriteAt;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentObjectId() {
        return contentObjectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public long getFavoriteAt() {
        return favoriteAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShowFavorite() {
        return showFavorite;
    }

    public void setShowFavorite(boolean showFavorite) {
        this.showFavorite = showFavorite;
    }
}
