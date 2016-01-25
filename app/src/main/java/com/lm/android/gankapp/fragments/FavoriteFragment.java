package com.lm.android.gankapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.activities.DetailActivity;
import com.lm.android.gankapp.adapters.FavoriteContentAdapter;
import com.lm.android.gankapp.dao.FavoriteContent;
import com.lm.android.gankapp.dao.FavoriteContentDao;
import com.lm.android.gankapp.interfaces.FavoriteDatasCallback;
import com.lm.android.gankapp.listener.OnContentItemClickListener;
import com.lm.android.gankapp.models.FavoriteModel;
import com.lm.android.gankapp.services.SyncDataService;
import com.lm.android.gankapp.utils.ListUtils;
import com.lm.android.gankapp.utils.Utils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.Query;
import icepick.State;

public class FavoriteFragment extends BaseFragment {
    private static final String ARG_PARAM_CATEGORY = "category";
    private static final String ARG_PARAM_USERID = "userId";

    @State
    int mCategory;
    @State
    String userId;

    private String request_base_url;
    private ArrayList<FavoriteModel> datas;
    private FavoriteContentAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * @param category 数据类型，分为Android、iOS、前端等
     */
    public static FavoriteFragment newInstance(int category, String userId) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_CATEGORY, category);
        args.putString(ARG_PARAM_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getInt(ARG_PARAM_CATEGORY);
            userId = getArguments().getString(ARG_PARAM_USERID);
        }
        request_base_url = Utils.get_favorite_url;
        datas = new ArrayList<>();
        adapter = new FavoriteContentAdapter(datas);
        adapter.setOnItemClickListener(new OnContentItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                FavoriteModel itemData = adapter.getItemData(position);
                DetailActivity.actionStart(getActivity(), itemData.getContentObjectId(), itemData.getUrl(), itemData.getDesc(), null, itemData.getType(), null);
            }
        });
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDatas(request_base_url);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_content, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) convertView.findViewById(R.id.swiperefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.my_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 初次进入显示刷新动画
        swipeRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setRefreshing(true);
        requestDatas(request_base_url);
        return convertView;
    }

    /**
     * 请求数据操作
     *
     * @param url 请求的url
     */
    private void requestDatas(String url) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("type", Utils.requestCategory[mCategory]);
        OkHttpUtils.post().url(url).params(params).build().execute(new FavoriteDatasCallback() {
            @Override
            public void onError(Request request, Exception e) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                // 获取网络数据失败，改用本地数据库中的数据
                FavoriteContentDao favoriteDao = gankApplication.getDaoSession().getFavoriteContentDao();
                Query query = favoriteDao.queryBuilder().where(FavoriteContentDao.Properties.Type.eq(Utils.requestCategory[mCategory])).build();
                if (query != null) {
                    List<FavoriteContent> list = query.list();
                    if (ListUtils.isEmpty(list)) {
                    } else {
                        datas.clear();
                        for (FavoriteContent item : list) {
                            FavoriteModel model = new FavoriteModel();
                            model.setContentObjectId(item.getContentObjectId());
                            model.setObjectId(item.getObjectId());
                            model.setDesc(item.getDesc());
                            model.setUrl(item.getUrl());
                            model.setType(item.getType());
                            model.setShowFavorite(item.getShowFavorite());
                            model.setFavoriteAt(item.getFavoriteAt());
                            datas.add(model);
                        }
                        adapter.refresh(datas);
                    }
                }
            }

            @Override
            public void onResponse(ArrayList<FavoriteModel> response) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (response == null) {
                    return;
                }
                datas = response;
                adapter.refresh(datas);

                Intent intent = new Intent(getActivity(), SyncDataService.class);
                intent.putExtra(SyncDataService.ACTION_TYPE, SyncDataService.UPDATE_FAV_DB);
                intent.putExtra(SyncDataService.ACTION_DATA, new Gson().toJson(datas));
                getActivity().startService(intent);
            }
        });
    }
}
