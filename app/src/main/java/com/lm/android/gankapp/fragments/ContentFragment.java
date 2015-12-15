package com.lm.android.gankapp.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.adapters.ContentAdapter;
import com.lm.android.gankapp.models.ContentItemInfo;
import com.lm.android.gankapp.models.Utils;
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

    private int pageNum = 1;
    private ArrayList<ContentItemInfo> datas;
    private ContentAdapter adapter;

    public ContentFragment() {
        // Required empty public constructor
    }

    /**
     * @param type     页面类型，分为网络数据和收藏数据
     * @param category 数据类型，分为Android、iOS、前端等
     * @return
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
        datas = new ArrayList<>();
        adapter = new ContentAdapter(datas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_content, container, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String url = Utils.base_category_data_url + Utils.requestCategory[mCategory] + "/" + Utils.requestNum + "/";
        requestDatas(url, false);
        return recyclerView;
    }

    private void requestDatas(String url, final boolean loadMore) {
        if (loadMore) {
            pageNum++;
        }
        url = url + pageNum;
        OkHttpUtils.get().url(url).build().execute(new DatasCallback() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(getActivity(), "Error! Please retry!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ArrayList<ContentItemInfo> response) {
                if (loadMore) {
                    datas.addAll(response);
                    adapter.refresh(datas);
                } else {
                    if (response != null) {
                        datas = response;
                        adapter.refresh(datas);
                    }
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
