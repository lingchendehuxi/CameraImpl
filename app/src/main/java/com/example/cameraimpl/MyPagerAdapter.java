package com.example.cameraimpl;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class MyPagerAdapter extends PagerAdapter {
    private final Context context;
    private View.OnClickListener onBannerClickListener;
    private static final String TAG = "MyPagerAdapter";
    private final List<String> imgList;

    public MyPagerAdapter(Context context, ArrayList<String> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public int getCount() {
        //返回int的最大值 可以一直滑动
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.e(TAG, "instantiateItem1: " + position);
        position %= imgList.size();
        Log.e(TAG, "instantiateItem2: " + position);
        ImageView imageView = new ImageView(context);
        //设置图片缩放类型
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //把当前的下标通过setTag方法设置进去
        imageView.setTag(position);
        Glide.with(context).load(imgList.get(position)).override(240,240).into(imageView);
        imageView.setOnClickListener(onClickListener);
        container.addView(imageView);
        return imageView;
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBannerClickListener.onClick(v);
        }
    };

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setOnBannerClickListener(View.OnClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }
}
