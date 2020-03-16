package com.demo.joker.utils;

import android.content.ComponentName;

import com.demo.joker.model.Destination;
import com.demo.joker.navigator.FixFragmentNavigator;
import com.demo.libcommon.utils.AppGlobals;

import java.util.HashMap;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

public class NavGraphBuilder {
    public static void build(NavController controller, FragmentActivity activity, int containerId){

        NavigatorProvider provider = controller.getNavigatorProvider();

        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(activity,activity.getSupportFragmentManager(),containerId);
        provider.addNavigator(fragmentNavigator);

        ActivityNavigator activityNavigator = provider.getNavigator(ActivityNavigator.class);




        HashMap<String, Destination> destConfig = AppConfig.getsDestConfig();
        for(Destination value :destConfig.values()){
            if(value.isIsFragment()){
                FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setClassName(value.getClassName());
                destination.setId(value.getId());
                destination.addDeepLink(value.getPageUrl());
                navGraph.addDestination(destination);
            }else {
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setId(value.getId());
                destination.addDeepLink(value.getPageUrl());
                destination.setComponentName(new ComponentName(AppGlobals.getApplication().getPackageName(),value.getClassName()));
                navGraph.addDestination(destination);
            }

            if(value.isAsStarter()){
                navGraph.setStartDestination(value.getId());
            }

        }
        controller.setGraph(navGraph);

    }
}
