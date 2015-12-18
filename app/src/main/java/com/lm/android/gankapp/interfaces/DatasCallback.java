package com.lm.android.gankapp.interfaces;

import com.google.gson.Gson;
import com.lm.android.gankapp.models.ContentItemInfo;
import com.lm.android.gankapp.models.ResponseModel;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by liumeng on 2015/12/18.
 */
public abstract class DatasCallback extends Callback<ArrayList<ContentItemInfo>> {
    @Override
    public ArrayList<ContentItemInfo> parseNetworkResponse(Response response) throws IOException {
        String payload = response.body().string();
        ResponseModel result = new Gson().fromJson(payload, ResponseModel.class);
        return result.results;
    }
}