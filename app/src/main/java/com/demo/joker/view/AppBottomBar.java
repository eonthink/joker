package com.demo.joker.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;

import com.demo.joker.R;
import com.demo.joker.model.BottomBar;
import com.demo.joker.model.Destination;
import com.demo.joker.utils.AppConfig;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.List;

public class AppBottomBar extends BottomNavigationView {
    private static int[] sIcons = new int[]{R.drawable.icon_tab_home, R.drawable.icon_tab_sofa, R.drawable.icon_tab_publish, R.drawable.icon_tab_find, R.drawable.icon_tab_mine};
    public AppBottomBar(Context context) {
        this(context,null);
    }

    public AppBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    @SuppressLint("RestrictedApi")
    public AppBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        BottomBar bottomBar = AppConfig.getBootomBar();
        List<BottomBar.Tab> tabs = bottomBar.getTabs();
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        int[] colors = new int[]{Color.parseColor(bottomBar.getActiveColor()),Color.parseColor(bottomBar.getInActiveColor())};
        ColorStateList colorStateList = new ColorStateList(states,colors);
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);



        for(int i=0;i<tabs.size();i++){
            BottomBar.Tab tab = tabs.get(i);
            if(!tab.isEnable()){
                continue;
            }
            int id = getId(tab.getPageUrl());
            if(id < 0){
                continue;
            }
            MenuItem item = getMenu().add(0, id, tab.getIndex(), tab.getTitle());
            item.setIcon(sIcons[tab.getIndex()]);

        }

        for( int i=0;i<tabs.size();i++){
            if (!tabs.get(i).isEnable()) {
                continue;
            }
            int itemId = getId(tabs.get(i).getPageUrl());
            if (itemId < 0) {
                continue;
            }
            BottomBar.Tab tab = tabs.get(i);
            int iconSize = dp2px(tab.getSize());
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(tab.getIndex());
            itemView.setIconSize(iconSize);

            if(TextUtils.isEmpty(tab.getTitle())){
                int tintColor = TextUtils.isEmpty(tab.getTintColor()) ? Color.parseColor("#ff678f") : Color.parseColor(tab.getTintColor());
                itemView.setIconTintList(ColorStateList.valueOf(tintColor));
                itemView.setShifting(false);
            }
        }

        //底部导航栏默认选中项
        if (bottomBar.getSelectTab() != 0) {
            BottomBar.Tab selectTab = bottomBar.getTabs().get(bottomBar.getSelectTab());
            if (selectTab.isEnable()) {
                int itemId = getId(selectTab.getPageUrl());
                //这里需要延迟一下 再定位到默认选中的tab
                //因为 咱们需要等待内容区域,也就NavGraphBuilder解析数据并初始化完成，
                //否则会出现 底部按钮切换过去了，但内容区域还没切换过去
                post(() -> setSelectedItemId(itemId));
            }
        }
    }

    private int dp2px(int size) {
       return (int) (getContext().getResources().getDisplayMetrics().density*size+0.5f);
    }

    private int getId(String pageUrl) {
        Destination destination = AppConfig.getsDestConfig().get(pageUrl);
        if(destination == null){
            return -1;
        }

        return destination.getId();
    }
}
