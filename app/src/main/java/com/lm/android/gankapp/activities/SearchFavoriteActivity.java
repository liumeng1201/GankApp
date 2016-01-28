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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.FavoriteContentAdapter;
import com.lm.android.gankapp.listener.OnContentItemClickListener;
import com.lm.android.gankapp.models.FavoriteModel;
import com.lm.android.gankapp.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by liumeng on 2016/1/28.
 */
public class SearchFavoriteActivity extends BaseActivityWithLoadingDialog {
    private EditText edtInput;
    private ArrayList<FavoriteModel> datas;
    private ArrayList<FavoriteModel> filterDatas;
    private FavoriteContentAdapter adapter;

    public static void actionStart(Context context, String datas) {
        Intent intent = new Intent(context, SearchFavoriteActivity.class);
        intent.putExtra("datas", datas);
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
        String data = getIntent().getStringExtra("datas");
        datas = new Gson().fromJson(data, new TypeToken<ArrayList<FavoriteModel>>() { }.getType());
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
}
