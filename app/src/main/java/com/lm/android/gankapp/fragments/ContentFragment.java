package com.lm.android.gankapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lm.android.gankapp.R;

public class ContentFragment extends BaseFragment {
    private static final String ARG_PARAM_TYPE = "type";
    private static final String ARG_PARAM_CATEGORY = "category";

    private String mType;
    private String mCategory;

    public ContentFragment() {
        // Required empty public constructor
    }

    /**
     * @param type     页面类型，分为网络数据和收藏数据
     * @param category 数据类型，分为Android、iOS、前端等
     * @return
     */
    public static ContentFragment newInstance(String type, String category) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TYPE, type);
        args.putString(ARG_PARAM_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_PARAM_TYPE);
            mCategory = getArguments().getString(ARG_PARAM_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

}
