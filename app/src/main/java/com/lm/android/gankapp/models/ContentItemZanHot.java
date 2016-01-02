package com.lm.android.gankapp.models;

/**
 * 文章点赞表
 * Created by liumeng on 2016/1/2.
 */
public class ContentItemZanHot extends BmobContentItemBase {
    private Integer count;

    public ContentItemZanHot() {
    }

    public ContentItemZanHot(String who, String publishedAt, String desc, String type, String url, String contentObjectId) {
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
