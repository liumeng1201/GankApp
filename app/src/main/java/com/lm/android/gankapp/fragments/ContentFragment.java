package com.lm.android.gankapp.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.activities.DetailActivity;
import com.lm.android.gankapp.adapters.ContentAdapter;
import com.lm.android.gankapp.models.ContentCategory;
import com.lm.android.gankapp.models.ContentItemInfo;
import com.lm.android.gankapp.models.Utils;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;

import icepick.State;

public class ContentFragment extends BaseFragment {
    private static final String ARG_PARAM_TYPE = "type";
    private static final String ARG_PARAM_CATEGORY = "category";

    @State
    int mType;
    @State
    int mCategory;

    private boolean isLoading = false;
    private boolean isLoadAll = false;
    private int pageNum = 1;
    private String request_base_url;
    private ArrayList<ContentItemInfo> datas;
    private ContentAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private RecyclerView.OnScrollListener scrollListener;

    public ContentFragment() {
        // Required empty public constructor
    }

    /**
     * @param type     页面类型，分为网络数据和收藏数据
     * @param category 数据类型，分为Android、iOS、前端等
     */
    public static ContentFragment newInstance(int type, int category) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_TYPE, type);
        args.putInt(ARG_PARAM_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_PARAM_TYPE);
            mCategory = getArguments().getInt(ARG_PARAM_CATEGORY);
        }
        request_base_url = Utils.base_category_data_url + Utils.requestCategory[mCategory] + "/" + Utils.requestNum + "/";
        datas = new ArrayList<>();
        adapter = new ContentAdapter(datas, mCategory == ContentCategory.MEIZI.getType() ? true : false);
        adapter.setOnItemClickListener(new ContentAdapter.OnContentItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ContentItemInfo itemData = adapter.getItemData(position);
                DetailActivity.actionStart(getActivity(), itemData.getUrl(), itemData.getDesc());
            }
        });
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
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
        if (loadMore) {
            pageNum++;
        }
        url = url + pageNum;
        Logger.d(url);
        OkHttpUtils.get().url(url).build().execute(new DatasCallback() {
            @Override
            public void onError(Request request, Exception e) {
                isLoading = false;
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Toast.makeText(getActivity(), "Error! Please retry!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ArrayList<ContentItemInfo> response) {
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
                    adapter.refresh(datas, mCategory == ContentCategory.MEIZI.getType() ? true : false);
                } else {
                    datas = response;
                    adapter.refresh(datas, mCategory == ContentCategory.MEIZI.getType() ? true : false);
                }
            }
        });
    }

    class ResponseModel {
        boolean error;
        ArrayList<ContentItemInfo> results;
    }

    public abstract class DatasCallback extends Callback<ArrayList<ContentItemInfo>> {
        @Override
        public ArrayList<ContentItemInfo> parseNetworkResponse(Response response) throws IOException {
            String payload = response.body().string();
            ResponseModel result = new Gson().fromJson(payload, ResponseModel.class);
            return result.results;
        }
    }
}
