package com.demo.joker.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

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
        if(laoutParams!=null &&laoutParams.width>0 && laoutParams.height>0){
            builder.override(laoutParams.width,laoutParams.height);
        }
        builder.into(view);
    }
}
