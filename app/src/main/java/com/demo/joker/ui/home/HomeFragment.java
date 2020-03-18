package com.demo.joker.ui.home;

import android.os.Bundle;
import android.view.View;

import com.demo.joker.model.Feed;
import com.demo.joker.ui.AbsListFragment;
import com.demo.joker.ui.MutablePageKeyedDataSource;
import com.demo.libnavannotation.FragmentDestination;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

@FragmentDestination(pageUrl = "main/tabs/home",asStarter=true)
public class HomeFragment extends AbsListFragment<Feed,HomeViewModel> {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel.getCacheLiveData().observe(this, new Observer<PagedList<Feed>>() {
            @Override
            public void onChanged(PagedList<Feed> feeds) {
                submitList(feeds);
            }
        });

    }

    @Override
    public PagedListAdapter getAdapter() {
        String feedType = getArguments() == null ? "all" : getArguments().getString("feedType");
        return new FeedAdapter(getContext(),feedType);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        Feed feed = adapter.getCurrentList().get(adapter.getItemCount() - 1);
        mViewModel.loadAfter(feed.id, new ItemKeyedDataSource.LoadCallback<Feed>() {
            @Override
            public void onResult(@NonNull List<Feed> data) {
                if(data!=null&&data.size()>0){
                    PagedList.Config config = adapter.getCurrentList().getConfig();
                    if (data != null && data.size() > 0) {
                        //这里 咱们手动接管 分页数据加载的时候 使用MutableItemKeyedDataSource也是可以的。
                        //由于当且仅当 paging不再帮我们分页的时候，我们才会接管。所以 就不需要ViewModel中创建的DataSource继续工作了，所以使用
                        //MutablePageKeyedDataSource也是可以的
                        MutablePageKeyedDataSource dataSource = new MutablePageKeyedDataSource();
                        //这里要把列表上已经显示的先添加到dataSource.data中
                        //而后把本次分页回来的数据再添加到dataSource.data中
                        dataSource.data.addAll(adapter.getCurrentList());
                        dataSource.data.addAll(data);
                        PagedList pagedList = dataSource.buildNewPagedList(config);
                        submitList(pagedList);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.getDataSource().invalidate();
    }
}