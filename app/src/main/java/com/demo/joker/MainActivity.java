package com.demo.joker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.demo.joker.model.Destination;
import com.demo.joker.model.User;
import com.demo.joker.ui.login.UserManager;
import com.demo.joker.utils.AppConfig;
import com.demo.joker.utils.NavGraphBuilder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NavController navController;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavGraphBuilder.build(navController,this,fragment.getId());
        navView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        HashMap<String, Destination> destinationHashMap = AppConfig.getsDestConfig();
        Iterator<Map.Entry<String, Destination>> entryIterator = destinationHashMap.entrySet().iterator();
        while (entryIterator.hasNext()){
            Map.Entry<String, Destination> entry = entryIterator.next();
            Destination value = entry.getValue();
            if(value!=null
                    &&!UserManager.get().isLogin()
                    &&value.isNeedLogin()
                    &&value.getId()==item.getItemId()){
                UserManager.get().login(this).observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                            navView.setSelectedItemId(item.getItemId());
                    }
                });
                return false;
            }
        }

        navController.navigate(item.getItemId());
        return !TextUtils.isEmpty(item.getTitle());
    }
}
