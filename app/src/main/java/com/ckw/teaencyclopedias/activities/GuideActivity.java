package com.ckw.teaencyclopedias.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ckw.teaencyclopedias.MainActivity;
import com.ckw.teaencyclopedias.R;
import com.ckw.teaencyclopedias.adapters.GuidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<ImageView> data = new ArrayList<>();//数据源
    private PagerAdapter mAdapter;//适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //设置actionBar隐藏
        getSupportActionBar().hide();

        initViewPager();

    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.guideViewPager);

        //加载图片数据源
        for (int i = 0; i < 3; i++) {
            int id = getResources().getIdentifier("slide" + (i + 1), "mipmap", getPackageName());

            ImageView imageView = new ImageView(this);

            imageView.setImageResource(id);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            data.add(imageView);
        }

        mAdapter = new GuidePagerAdapter(data);

        mViewPager.setAdapter(mAdapter);

        //调整监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //跳转到主界面
            @Override
            public void onPageSelected(int position) {

                if(position==2){

                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);

                    GuideActivity.this.startActivity(intent);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
