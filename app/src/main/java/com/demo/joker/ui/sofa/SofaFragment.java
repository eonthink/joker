package com.demo.joker.ui.sofa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.joker.R;
import com.demo.libnavannotation.FragmentDestination;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

@FragmentDestination(pageUrl = "main/tabs/sofa")
public class SofaFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sofa, container, false);

        return root;
    }
}