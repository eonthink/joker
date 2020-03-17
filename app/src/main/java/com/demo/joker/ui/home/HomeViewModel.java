package com.demo.joker.ui.home;

import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.demo.joker.model.Feed;
import com.demo.joker.ui.AbsViewModel;
import com.demo.libnetwork.ApiResponse;
import com.demo.libnetwork.ApiService;
import com.demo.libnetwork.JsonCallback;
import com.demo.libnetwork.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

public class HomeViewModel extends AbsViewModel<Feed> {
    private volatile boolean withCache = true;

    @Override
    public DataSource createDataSource() {
        return mDataSource;
    }

    ItemKeyedDataSource<Integer, Feed> mDataSource = new ItemKeyedDataSource<Integer, Feed>() {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            loadData(0,callback);
            withCache=false;
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            loadData(params.key,callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            return item.id;
        }
    };

    private void loadData(int key, ItemKeyedDataSource.LoadCallback<Feed> callback) {

        Log.e("TAG111111",Thread.currentThread().getName());
        Request request = ApiService.get("/feeds/queryHotFeedsList")
                .addParam("feedType","all")
                .addParam("userId", 0)
                .addParam("feedId", key)
                .addParam("pageCount", 10)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType());
        if (withCache) {
            request.cacheStrategy(Request.CACHE_ONLY);
            request.execute(new JsonCallback<List<Feed>>() {
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {

                }
            });
        }
        try {
            Request netRequest = withCache ? request.clone():request;
            netRequest.cacheStrategy(key==0?Request.NET_ONLY:Request.NET_ONLY);
            ApiResponse<List<Feed>> response = netRequest.execute();
            List<Feed> data = response.body == null ? Collections.emptyList() : response.body;
            callback.onResult(data);
            if (key > 0) {
                getBoundaryPageData().postValue(data.size() > 0);
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }
}