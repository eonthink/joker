package com.demo.joker.ui.publish;

import android.os.Bundle;

import com.demo.joker.R;
import com.demo.libnavannotation.ActivityDestination;

import androidx.appcompat.app.AppCompatActivity;
@ActivityDestination(pageUrl = "main/tabs/publish", needLogin = true)
public class publishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_publish);
    }
}
