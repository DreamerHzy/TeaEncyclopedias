package com.ckw.teaencyclopedias;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ckw.teaencyclopedias.activities.CollectionActivity;
import com.ckw.teaencyclopedias.activities.CopyRightActivity;
import com.ckw.teaencyclopedias.activities.HistoryActivity;
import com.ckw.teaencyclopedias.activities.SearchActivity;
import com.ckw.teaencyclopedias.adapters.TeaViewPagerAdapter;
import com.ckw.teaencyclopedias.fragments.FifthFragment;
import com.ckw.teaencyclopedias.fragments.FirstFragment;
import com.ckw.teaencyclopedias.fragments.FourthFragment;
import com.ckw.teaencyclopedias.fragments.SecondFragment;
import com.ckw.teaencyclopedias.fragments.ThirdFragment;
import com.softpo.viewpagertransformer.CubeInTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Fragment> mList = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    private TabLayout mTabLayout;
    //tabLayout的数据源
    private String[] mTabsData = new String[]{"头条","百科","咨询","经营","数据"};

    //侧拉选项
    private DrawerLayout mDrawerLayout;

    private EditText search_edit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        //加载viewPager的数据源
        Fragment fragment1 = new FirstFragment();
        Fragment fragment2 = new SecondFragment();
        Fragment fragment3 = new ThirdFragment();
        Fragment fragment4 = new FourthFragment();
        Fragment fragment5 = new FifthFragment();

        initView();
        initViewPager(fragment1,fragment2,fragment3,fragment4,fragment5);

        initTabLayout();

    }


    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        search_edit = (EditText) findViewById(R.id.search_edit);
    }


    //点击抽屉的事件
    public void click(View view) {
        if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }else {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    //该方法对应ActionBar上面的控件的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mDrawerLayout.openDrawer(Gravity.RIGHT);

        return super.onOptionsItemSelected(item);
    }

    //用于两层viewPager的使用，具体在FirstFragment中，listView的监听事件
    public ViewPager getViewPager(){
        return mViewPager;
    }

    //初始化ViewPager
    private void initViewPager(Fragment fragment1,Fragment fragment2,Fragment fragment3,
                               Fragment fragment4,Fragment fragment5) {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mList.add(fragment1);
        mList.add(fragment2);
        mList.add(fragment3);
        mList.add(fragment4);
        mList.add(fragment5);

        mAdapter = new TeaViewPagerAdapter(getSupportFragmentManager(),mList,mTabsData);

        mViewPager.setAdapter(mAdapter);

        //对viewPager设置动画效果
        /**
         * DefaultTransformer()：默认的效果
         * FilmPagerTransformer():效果跟网易电影票那个类似，滑动切换
         *AccordionTransformer():翻页效果
         * CubeInTransformer():正方体旋转效果
         * BackgroundToForegroundTransformer():从小到大从右下角出现
         *DepthPageTransformer()：效果一般，层叠出现
         * FlipHorizontalTransformer()：沿着y轴翻转
         *
         */
        mViewPager.setPageTransformer(false,new CubeInTransformer());
    }

    //初始化TabLayout
    private void initTabLayout() {

        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mTabLayout.setupWithViewPager(mViewPager);

    }

    //点开抽屉之后的点击事件
    public void rightClick(View view) {
        switch (view.getId()){
            case R.id.backToMainImg://关闭抽屉
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                break;

            case R.id.historyRecord://跳转到历史记录页面
                Intent historyIntent = new Intent(this,HistoryActivity.class);
                this.startActivity(historyIntent);

                break;
            case R.id.myCollection://跳转到我的收藏页面
                Intent collectionIntent = new Intent(this,CollectionActivity.class);

                this.startActivity(collectionIntent);
                break;

            case R.id.banquanInfo://跳转到版权信息界面
                Intent copyRightIntent = new Intent(this, CopyRightActivity.class);

                this.startActivity(copyRightIntent);
                break;

            case R.id.gosearch:
                 String content = search_edit.getText().toString().trim();

                Intent goSearch = new Intent();
                goSearch.setClass(this, SearchActivity.class);
                goSearch.putExtra("content",content);

                this.startActivity(goSearch);

                break;

            case R.id.backToHome:
                Intent home = new Intent(Intent.ACTION_MAIN,null);
                home.addCategory(Intent.CATEGORY_HOME);
                this.startActivity(home);

                break;
            //TODO 还有其他的case


        }
    }


}
