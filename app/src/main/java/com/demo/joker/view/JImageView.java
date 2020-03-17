package com.demo.joker.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.demo.joker.utils.PixUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class JImageView extends AppCompatImageView {

    public JImageView(Context context) {
        super(context);
    }

    public JImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @BindingAdapter(value = {"image_url","isCircle"},requireAll = true)
    public static void setImageUrl(JImageView view,String imageUrl,boolean isCircle){
        RequestBuilder<Drawable> builder = Glide.with(view).load(imageUrl);
        if(isCircle){
            builder.transform(new CircleCrop());
        }
        ViewGroup.LayoutParams laoutParams = view.getLayoutParams();
        if (laoutParams != null && laoutParams.width > 0 && laoutParams.height > 0) {
            builder.override(laoutParams.width, laoutParams.height);
        }
        builder.into(view);
    }

    public void bindData(int widthPx, int heightPx, int marginLeft, String url) {
        bindData(widthPx, heightPx, marginLeft, PixUtils.getScreenHeight(), PixUtils.getScreenWidth(), url);
    }

    public void bindData(int widthPx,int heightPx,int marginLeft,int maxHeight,int maxWidth,String url){

        if(widthPx<=0 || heightPx<=0){
            Glide.with(this).load(url).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();
                    setSize(width,height,marginLeft,maxWidth,maxHeight);

                    setImageDrawable(resource);
                }
            });
            return;
        }

        setSize(widthPx, heightPx, marginLeft, maxWidth, maxHeight);
        setImageUrl(this, url, false);
    }

    private void setSize(int width, int height, int marginLeft, int maxWidth, int maxHeight) {
        int finalWidth, finalHeight;
        if (width > height) {
            finalWidth = maxWidth;
            finalHeight = (int) (height / (width * 1.0f / finalWidth));
        } else {
            finalHeight=maxHeight;
            finalWidth = (int) (width/(height*1.0f/finalHeight));
        }

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(finalWidth, finalHeight);
        params.leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        setLayoutParams(params);
    }

    public void setBlurImageUrl(String url, int radius) {
        Glide.with(this).load(url)
                .override(50)
                .transform(new BlurTransformation())
                .dontAnimate()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        setBackground(resource);
                    }
                });

    }
}
