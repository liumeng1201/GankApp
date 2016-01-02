package com.lm.android.gankapp.models;

/**
 * 热门收藏表
 * Created by liumeng on 2016/1/2.
 */
public class ContentItemCollectHot extends BmobContentItemBase {
    private Integer count;

    public ContentItemCollectHot() {
    }

    public ContentItemCollectHot(String who, String publishedAt, String desc, String type, String url, String contentObjectId) {
        setWho(who);
        setPublishedAt(publishedAt);
        setDesc(desc);
        setType(type);
        setUrl(url);
        setContentObjectId(contentObjectId);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
