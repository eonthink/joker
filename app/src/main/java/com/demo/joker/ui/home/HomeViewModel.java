package com.demo.joker.ui.home;

import com.alibaba.fastjson.TypeReference;
import com.demo.joker.model.Feed;
import com.demo.joker.ui.AbsViewModel;
import com.demo.joker.ui.MutablePageKeyedDataSource;
import com.demo.libnetwork.ApiResponse;
import com.demo.libnetwork.ApiService;
import com.demo.libnetwork.JsonCallback;
import com.demo.libnetwork.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

public class HomeViewModel extends AbsViewModel<Feed> {
    private volatile boolean withCache = true;
    private MutableLiveData<PagedList<Feed>> cacheLiveData = new MutableLiveData<>();
    private AtomicBoolean loadAfter=new AtomicBoolean(false);
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
        if(key>0){
            loadAfter.set(true);
        }

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
                    List<Feed> body = response.body;
                    MutablePageKeyedDataSource<Feed> dataSource = new MutablePageKeyedDataSource<>();
                    dataSource.data.addAll(response.body);
                    PagedList<Feed> pageList = dataSource.buildNewPagedList(config);
                    cacheLiveData.postValue(pageList);
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
                loadAfter.set(false);
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    public LiveData<PagedList<Feed>> getCacheLiveData() {
        return cacheLiveData;
    }

    public void loadAfter(int id, ItemKeyedDataSource.LoadCallback<Feed> callback) {

        if(loadAfter.get()){
            callback.onResult(Collections.emptyList());
            return;
        }
        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loadData(id,callback);
            }
        });
    }
}