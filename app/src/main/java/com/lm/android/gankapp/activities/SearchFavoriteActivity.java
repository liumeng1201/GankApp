package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.FavoriteContentAdapter;
import com.lm.android.gankapp.dao.FavoriteContent;
import com.lm.android.gankapp.dao.FavoriteContentDao;
import com.lm.android.gankapp.listener.OnContentItemClickListener;
import com.lm.android.gankapp.models.FavoriteModel;
import com.lm.android.gankapp.utils.ListUtils;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Created by liumeng on 2016/1/28.
 */
public class SearchFavoriteActivity extends BaseActivityWithLoadingDialog {
    private EditText edtInput;
    private ArrayList<FavoriteModel> datas;
    private ArrayList<FavoriteModel> filterDatas;
    private FavoriteContentAdapter adapter;

    public static void actionStart(Context context, int type) {
        Intent intent = new Intent(context, SearchFavoriteActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initLoadingDialog() {
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_search_favorite);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        edtInput = (EditText) findViewById(R.id.search_input);

        filterDatas = new ArrayList<>();
        int type = getIntent().getIntExtra("type", -1);
        datas = getDatas(type);
        if (datas == null) {
            datas = new ArrayList<>();
        }
        adapter = new FavoriteContentAdapter(datas);
        adapter.setOnItemClickListener(new OnContentItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                FavoriteModel itemData = adapter.getItemData(position);
                DetailActivity.actionStart(context, itemData.getContentObjectId(), itemData.getUrl(), itemData.getDesc(), null, itemData.getType(), null);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (!StringUtils.isEmpty(content)) {
                    filterDatas.clear();
                    for (FavoriteModel item : datas) {
                        if (item.getDesc().contains(content)) {
                            filterDatas.add(item);
                        }
                    }
                    adapter.refresh(filterDatas, content, true);
                } else {
                    adapter.refresh(datas);
                }
            }
        });
    }

    private ArrayList<FavoriteModel> getDatas(int type) {
        FavoriteContentDao dao = gankApplication.getDaoSession().getFavoriteContentDao();
        Query query = dao.queryBuilder().where(FavoriteContentDao.Properties.Type.eq(Utils.requestCategory[type])).build();
        if (query != null) {
            List<FavoriteContent> list = query.list();
            if (!ListUtils.isEmpty(list)) {
                ArrayList<FavoriteModel> favoriteList = new ArrayList<>();
                for (FavoriteContent item : list) {
                    if (item.getShowFavorite()) {
                        FavoriteModel model = new FavoriteModel();
                        model.setFavoriteAt(item.getFavoriteAt());
                        model.setShowFavorite(item.getShowFavorite());
                        model.setType(item.getType());
                        model.setContentObjectId(item.getContentObjectId());
                        model.setDesc(item.getDesc());
                        model.setUrl(item.getUrl());
                        favoriteList.add(model);
                    }
                }
                return favoriteList;
            }
        }
        return null;
    }
}
