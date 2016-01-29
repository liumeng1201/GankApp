package com.lm.android.gankapp.models;

/**
 * 收藏表
 * Created by liumeng on 2016/1/10.
 */
public class ContentItemFavorite extends BmobContentItemBase {
    private Long favoriteAt;
    private String userId;
    private boolean showFavorite;

    public ContentItemFavorite() {
    }

    public ContentItemFavorite(String desc, String type, String url, String contentObjectId, Long favoriteAt, String userId, boolean showFavorite) {
        setDesc(desc);
        setType(type);
        setUrl(url);
        setContentObjectId(contentObjectId);
        setFavoriteAt(favoriteAt);
        setUserId(userId);
        setShowFavorite(showFavorite);
    }

    public Long getFavoriteAt() {
        return favoriteAt;
    }

    public void setFavoriteAt(Long favoriteAt) {
        this.favoriteAt = favoriteAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isShowFavorite() {
        return showFavorite;
    }

    public void setShowFavorite(boolean showFavorite) {
        this.showFavorite = showFavorite;
    }
}
