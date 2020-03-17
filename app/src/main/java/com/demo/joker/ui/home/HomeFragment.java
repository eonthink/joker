package com.demo.joker.ui.home;

import com.demo.joker.model.Feed;
import com.demo.joker.ui.AbsListFragment;
import com.demo.libnavannotation.FragmentDestination;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;

@FragmentDestination(pageUrl = "main/tabs/home",asStarter=true)
public class HomeFragment extends AbsListFragment<Feed,HomeViewModel> {


    @Override
    public PagedListAdapter getAdapter() {
        String feedType = getArguments() == null ? "all" : getArguments().getString("feedType");
        return new FeedAdapter(getContext(),feedType);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}