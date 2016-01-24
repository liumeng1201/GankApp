package com.lm.android.gankapp.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "FAVORITE_CONTENT".
 */
public class FavoriteContent {

    private Long id;
    /** Not-null value. */
    private String objectId;
    /** Not-null value. */
    private String contentObjectId;
    /** Not-null value. */
    private String type;
    /** Not-null value. */
    private String desc;
    /** Not-null value. */
    private String url;
    private long favoriteAt;
    private boolean showFavorite;

    public FavoriteContent() {
    }

    public FavoriteContent(Long id) {
        this.id = id;
    }

    public FavoriteContent(Long id, String objectId, String contentObjectId, String type, String desc, String url, long favoriteAt, boolean showFavorite) {
        this.id = id;
        this.objectId = objectId;
        this.contentObjectId = contentObjectId;
        this.type = type;
        this.desc = desc;
        this.url = url;
        this.favoriteAt = favoriteAt;
        this.showFavorite = showFavorite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getObjectId() {
        return objectId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /** Not-null value. */
    public String getContentObjectId() {
        return contentObjectId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContentObjectId(String contentObjectId) {
        this.contentObjectId = contentObjectId;
    }

    /** Not-null value. */
    public String getType() {
        return type;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setType(String type) {
        this.type = type;
    }

    /** Not-null value. */
    public String getDesc() {
        return desc;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /** Not-null value. */
    public String getUrl() {
        return url;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUrl(String url) {
        this.url = url;
    }

    public long getFavoriteAt() {
        return favoriteAt;
    }

    public void setFavoriteAt(long favoriteAt) {
        this.favoriteAt = favoriteAt;
    }

    public boolean getShowFavorite() {
        return showFavorite;
    }

    public void setShowFavorite(boolean showFavorite) {
        this.showFavorite = showFavorite;
    }

}
