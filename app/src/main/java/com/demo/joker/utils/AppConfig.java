package com.demo.joker.utils;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo.joker.model.Destination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppConfig {
    private static HashMap<String, Destination> sDestConfig;
    public static HashMap<String,Destination> getsDestConfig(){
        if (sDestConfig == null) {
            String content = parseFile("destination.json");
            sDestConfig = JSON.parseObject(content, new TypeReference<HashMap<String, Destination>>() {
            }.getType());
        }
        return sDestConfig;
    }

    private static String parseFile(String fileName){
        AssetManager assets = AppGlobals.getsApplication().getResources().getAssets();
        InputStream stream = null;
        BufferedReader reader = null;
        StringBuilder builder=new StringBuilder();
        try {
             stream = assets.open(fileName);
             reader = new BufferedReader(new InputStreamReader(stream));
             String line = null;
             while ((line=reader.readLine())!=null){
                 builder.append(line);
             }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(stream!=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
