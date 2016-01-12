package com.lm.android.gankapp.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.activities.DetailActivity;
import com.lm.android.gankapp.adapters.FavoriteContentAdapter;
import com.lm.android.gankapp.interfaces.FavoriteDatasCallback;
import com.lm.android.gankapp.listener.OnContentItemClickListener;
import com.lm.android.gankapp.models.FavoriteModel;
import com.lm.android.gankapp.utils.Utils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import icepick.State;

public class FavoriteFragment extends BaseFragment {
    private static final String ARG_PARAM_CATEGORY = "category";
    private static final String ARG_PARAM_USERID = "userId";

    @State
    int mCategory;
    @State
    String userId;

    private boolean isLoading = false;
    private boolean isLoadAll = false;
    private String request_base_url;
    private ArrayList<FavoriteModel> datas;
    private FavoriteContentAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private RecyclerView.OnScrollListener scrollListener;

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
                DetailActivity.actionStart(getActivity(), itemData.getObjectId(), itemData.getUrl(), itemData.getDesc(), null, itemData.getType(), null);
            }
        });
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDatas(request_base_url, false);
            }
        };
        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                // lastVisibleItem >= totalItemCount - 10 表示剩下10个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 10 && dy > 0 && !isLoading && !isLoadAll) {
                    isLoading = true;
                    requestDatas(request_base_url, true);
                }
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
        recyclerView.addOnScrollListener(scrollListener);
        // 初次进入显示刷新动画
        swipeRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setRefreshing(true);
        requestDatas(request_base_url, false);
        return convertView;
    }

    /**
     * 请求数据操作
     *
     * @param url      请求的url
     * @param loadMore 是否为加载更多
     */
    private void requestDatas(String url, final boolean loadMore) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("type", Utils.requestCategory[mCategory]);
        OkHttpUtils.post().url(url).params(params).build().execute(new FavoriteDatasCallback() {
            @Override
            public void onError(Request request, Exception e) {
                isLoading = false;
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Utils.showToastShort(getActivity(), "Error! Please retry!");
            }

            @Override
            public void onResponse(ArrayList<FavoriteModel> response) {
                isLoading = false;
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (response == null) {
                    return;
                }
                if (response.size() < Utils.requestNum) {
                    isLoadAll = true;
                }
                if (loadMore) {
                    datas.addAll(response);
                } else {
                    datas = response;
                }
                adapter.refresh(datas);
            }
        });
    }
}
