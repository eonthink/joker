package com.demo.joker.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.demo.joker.R;
import com.demo.joker.view.JImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListPlayerView  extends FrameLayout {
    private final View bufferView;
    private final JImageView cover,blur;
    private final ImageView palyBtn;
    private String mCategory;
    private String mVideoUrl;


    public ListPlayerView(@NonNull Context context) {
        this(context,null);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_player_view, this, true);
        bufferView = findViewById(R.id.buffer_view);
        cover = findViewById(R.id.cover);
        blur = findViewById(R.id.blur_background);
        palyBtn = findViewById(R.id.play_btn);

    }

    public void bindData(String category,int widthPx,int heightPx,String coverUrl,String videoUrl){
        mCategory = category;
        mVideoUrl = videoUrl;
        cover.setImageUrl(cover, coverUrl, false);
        if (widthPx < heightPx) {
            blur.setBlurImageUrl(coverUrl, 10);
            blur.setVisibility(VISIBLE);
        }else {
            blur.setVisibility(INVISIBLE);
        }
        setSize(widthPx,heightPx);
    }

    private void setSize(int widthPx, int heightPx) {
        int maxWidth = PixUtils.getScreenWidth();
        int maxHeight = PixUtils.getScreenHeight();
        int layoutWidth = maxWidth;
        int layoutHeight = 0;
        int coverWidth;
        int coverHeight;

        if (widthPx >= heightPx) {
            coverWidth = maxWidth;
            layoutHeight = coverHeight = (int) (heightPx / (widthPx * 1.0f / maxWidth));
        } else {
            layoutHeight = coverHeight = maxHeight;
            coverWidth = (int) (widthPx / (heightPx*1.0f/maxHeight));
        }

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height=layoutWidth;
        params.height=layoutHeight;
        setLayoutParams(params);

        ViewGroup.LayoutParams blurLayoutParams = blur.getLayoutParams();
        blurLayoutParams.width=layoutWidth;
        blurLayoutParams.height=layoutHeight;
        blur.setLayoutParams(blurLayoutParams);

        FrameLayout.LayoutParams coverParams = (LayoutParams) cover.getLayoutParams();
        coverParams.width=coverWidth;
        coverParams.height=coverHeight;
        coverParams.gravity= Gravity.CENTER;
        cover.setLayoutParams(coverParams);

        FrameLayout.LayoutParams palyBtnParams = (LayoutParams) palyBtn.getLayoutParams();
        palyBtnParams.gravity= Gravity.CENTER;
        palyBtn.setLayoutParams(palyBtnParams);
    }


}
