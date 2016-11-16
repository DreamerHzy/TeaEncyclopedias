package com.ckw.teaencyclopedias.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.ckw.teaencyclopedias.R;

public class AnimationActivity extends AppCompatActivity {

    private ImageView animationImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        getSupportActionBar().hide();

        initView();

        initAnimation();
    }

    //设置动画
    private void initAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f,1.2f,1f,1.2f,
                ScaleAnimation.RELATIVE_TO_SELF,0.5f,
                ScaleAnimation.RELATIVE_TO_SELF,0.5f
        );

        scaleAnimation.setDuration(3000);

        scaleAnimation.setFillAfter(true);

        //对动画进行监听，当动画结束的时候，进行跳转
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(AnimationActivity.this,GuideActivity.class);

                AnimationActivity.this.startActivity(intent);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        //指定控件进行动画
        animationImg.startAnimation(scaleAnimation);
    }

    private void initView() {
        animationImg = (ImageView) findViewById(R.id.animationImg);
        animationImg.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
