package com.example.cameraimpl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class BannerActivity extends AppCompatActivity {
    private static final String TAG = "BannerActivity";

    private TextView currentNum;
    private ArrayList<String> imgList;
    private ViewPager viewPager;
    private TextView totalNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        viewPager = findViewById(R.id.viewPager);
        currentNum = findViewById(R.id.currentNum);
        totalNum = findViewById(R.id.totalNum);
        imgList = PicUtils.imagePath(new File(PicUtils.rootFolderPath + File.separator + CameraManager.fileName));
        if (imgList.size() == 0) {
            Log.d(TAG, "onCreate: 没有可以预览的图片");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        totalNum.setText("/" + imgList.size());
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this, imgList);
        myPagerAdapter.setOnBannerClickListener(onBannerClickListener);
        //设置缓存页数
        viewPager.setAdapter(myPagerAdapter);
        //添加页面更改监听器
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }


    private View.OnClickListener onBannerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Toast.makeText(BannerActivity.this, "当前点击位置：" + position, Toast.LENGTH_SHORT).show();
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        /**
         * function: 当前页面滚动的时候回调这个方法
         * @param position 当前页面的位置
         * @param positionOffset 滑动页面的百分比
         * @param positionOffsetPixels 滑动的像素数
         * @return
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * function: 当页面被选中时，会回调这个方法
         * @param position 被选中的页面的位置
         * @return
         */
        @Override
        public void onPageSelected(int position) {
            Log.e(TAG, "onPageSelected: MainActivity" + position);
            //当新页面选中时调用此方法，position 为新选中页面的位置索引
            //在所选页面的时候,点点图片也要发生变化
            currentNum.setText(String.valueOf(position + 1));
            Log.e(TAG, "onPageSelected: " + imgList.size());
        }

        /**
         * function: 当页面滚动状态变化时，会回调这个方法
         * 有三种状态：静止、滑动、拖拽（这里区别滑动和拖拽，以手指是否接触页面为准）
         * @param state 当前状态
         * @return
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
