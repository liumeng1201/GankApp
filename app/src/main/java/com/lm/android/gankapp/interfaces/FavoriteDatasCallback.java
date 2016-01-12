package com.lm.android.gankapp.interfaces;

import com.google.gson.Gson;
import com.lm.android.gankapp.models.FavoriteModel;
import com.lm.android.gankapp.models.FavoriteResponseModel;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by liumeng on 2015/12/18.
 */
public abstract class FavoriteDatasCallback extends Callback<ArrayList<FavoriteModel>> {
    @Override
    public ArrayList<FavoriteModel> parseNetworkResponse(Response response) throws IOException {
        String payload = response.body().string();
        FavoriteResponseModel result = new Gson().fromJson(payload, FavoriteResponseModel.class);
        return result.results;
    }
}