package com.ckw.teaencyclopedias.fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ckw.teaencyclopedias.MainActivity;
import com.ckw.teaencyclopedias.R;
import com.ckw.teaencyclopedias.activities.DetailsItemActivity;
import com.ckw.teaencyclopedias.adapters.FirstFragmentBaseAdapter;
import com.ckw.teaencyclopedias.adapters.HeaderImgAdapter;
import com.ckw.teaencyclopedias.beans.HeaderImg;
import com.ckw.teaencyclopedias.beans.HotData;
import com.ckw.teaencyclopedias.database.MySQLiteOpenHelper;
import com.ckw.teaencyclopedias.interfaces.HeaderImgCallback;
import com.ckw.teaencyclopedias.interfaces.HotDataCallback;
import com.ckw.teaencyclopedias.interfaces.ImageCallback;
import com.ckw.teaencyclopedias.net.FirstFragmentAsyncTask;
import com.ckw.teaencyclopedias.net.HeaderImgAsyncTask;
import com.ckw.teaencyclopedias.net.ImageAsyncTask;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment implements HotDataCallback, HeaderImgCallback {
    private int index = 0;
    private String path = "http://sns.maimaicha.com/" +
            "api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=" +
            "json&method=news.getHeadlines&rows=15&page=" + index;

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private ViewPager mHeaderViewPager;
    private String mHeaderPath = "http://sns.maimaicha.com/api?" +
            "apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getSlideshow";

    //数据源
    private List<HotData.DataBean> mList = new ArrayList<>();
    //适配器
    private BaseAdapter mAdapter;
    private HeaderImgAdapter mImgAdapter;
    private View mHeadView;

    private MySQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mDatabase;


    private int currentPosition = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 911:
                    if (mHandler.hasMessages(911)) {
                        mHandler.removeMessages(911);
                    }
                    //进行ViewPager的切换
                    currentPosition++;

                    //上一次currentPosition==2，的时候
                    if (currentPosition > 2) {
                        currentPosition = 0;
                    }

                    mHeaderViewPager.setCurrentItem(currentPosition, false);

                    //实现无限轮播的效果,自己调用自己
                    this.sendEmptyMessageDelayed(911, 3000);
                    break;
            }


        }
    };

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_first, container, false);

        mSQLiteOpenHelper = new MySQLiteOpenHelper(getContext());
        mDatabase = mSQLiteOpenHelper.getReadableDatabase();

        initData();

        initHeaderImageData();

        initPullToRefreshListView(ret);

        initLoadMore();

        initBackToTop(ret);

        //设置头布局底部圆形的导航
        initIndicator();

        //mHandler两秒之后就收到消息了
        mHandler.sendEmptyMessageDelayed(911, 3000);//设置4秒之后自动进行切换


        return ret;
    }

    //被选中的位置
    private int lastSelected = 0;
    //底部圆形图片，数组
    private View[] indicators = new View[3];

    private void initIndicator() {

        LinearLayout indicatorContainer = (LinearLayout) mHeadView.findViewById(R.id.indicators);

        //2.获取子控件view
        for (int i = 0; i < indicatorContainer.getChildCount(); i++) {
            //容器中的子控件
            indicators[i] = indicatorContainer.getChildAt(i);

            indicators[i].setEnabled(true);//代表可点击，颜色是白色的圆点

            //设置标记
            indicators[i].setTag(i);

            indicators[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();

                    //有了索引，就可以切换viewPager
                    mHeaderViewPager.setCurrentItem(index, true);
                }
            });//方法在最下面
        }

        //0位置的图片变成灰色的圆点，不可点击，表示当前正在展示
        indicators[0].setEnabled(false);
    }

    //加载更多
    private boolean isBottom = false;

    private void initLoadMore() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:

                        if (isBottom) {

                            index++;
                            String newPath = "http://sns.maimaicha.com/" +
                                    "api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=" +
                                    "json&method=news.getHeadlines&rows=15&page=" + index;

                            new FirstFragmentAsyncTask(FirstFragment.this).execute(path);

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //这里如果是true，说明已经到达底部
                isBottom = firstVisibleItem + visibleItemCount == totalItemCount;
            }
        });
    }

    //点击返回 顶部
    private void initBackToTop(View ret) {
        ImageView backToTop = (ImageView) ret.findViewById(R.id.backToTop);

        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setSelection(0);
            }
        });
    }

    private void initHeaderImageData() {
        new HeaderImgAsyncTask(this).execute(mHeaderPath);
    }

    private void initData() {

        new FirstFragmentAsyncTask(this).execute(path);
    }

    //初始化PullToRefreshListView（设置了头布局）
    private void initPullToRefreshListView(View ret) {

        mPullToRefreshListView =
                (PullToRefreshListView) ret.findViewById(R.id.pullToRefreshListView);

        mAdapter = new FirstFragmentBaseAdapter(getContext(), mList);

        mPullToRefreshListView.setAdapter(mAdapter);


        //适配器
        mImgAdapter = new HeaderImgAdapter(mHeaderImgs);

        //添加头布局
        mListView = mPullToRefreshListView.getRefreshableView();

        //设置头布局
        mHeadView = LayoutInflater.from(getContext()).inflate(R.layout.header_item, mListView, false);

        mHeaderViewPager = (ViewPager) mHeadView.findViewById(R.id.headerViewPager);

        mHeaderViewPager.setAdapter(mImgAdapter);

        mHeaderViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //得到主线程的viewPager，调用viewPager的方法，true：使用里面的viewPager时
                // 不会触发外层的viewPager，子的ViewPager不会被打断
                ((MainActivity) getContext()).getViewPager().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        //设置头布局的viewPager的滑动监听
        mHeaderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicators[position].setEnabled(false);

                //需要将之前选中的小圆点，变成白色的
                indicators[lastSelected].setEnabled(true);

                //重新设置上一次选中的位置
                lastSelected = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mListView.addHeaderView(mHeadView);

        //设置尾布局
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer_item, mListView, false);

        mListView.addFooterView(footerView);

        //设置点击事件，点击条目进入新的activity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();

                String itemId = mList.get(position - 2).getId();
                String title = mList.get(position - 2).getTitle();
                String path = mList.get(position - 2).getWap_thumb();
                String source = mList.get(position - 2).getSource();
                String nickName = mList.get(position - 2).getNickname();
                String createTime = mList.get(position - 2).getCreate_time();
                String description = mList.get(position - 2).getDescription();

                intent.putExtra("id", itemId);
                intent.putExtra("title", title);
                intent.putExtra("imgPath", path);
                intent.putExtra("source", source);
                intent.putExtra("nickName", nickName);
                intent.putExtra("createTime", createTime);
                intent.putExtra("description", description);

                intent.setClass(getContext(), DetailsItemActivity.class);
                getContext().startActivity(intent);

                //这边添加到数据库
                /**
                 * title varchar,imgPath varchar,source varchar,nickName varchar" +
                 "createTime varchar,description varchar)
                 */
                //先对数据库进行查询，判断数据库中是否存在这条数据
                List<String> historyTitle = new ArrayList<String>();
                Cursor history = mDatabase.query("history", new String[]{"title"}, null, null, null, null, null);

                while (history.moveToNext()) {
                    String str = history.getString(history.getColumnIndex("title"));
                    historyTitle.add(str);
                }

                boolean isExist = false;
                if (historyTitle.size() > 0) {//如果数据库中有数据
                    for (int i = 0; i < historyTitle.size(); i++) {
                        if (title.equals(historyTitle.get(i))) {//只要数据库中有，就表示为true
                            isExist = true;
                        }
                    }
                }

                if (!isExist) {//表示数据库中不存在该条数据，需要向数据库中添加数据

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("createTime", createTime);
                    contentValues.put("title", title);
                    contentValues.put("source", source);
                    contentValues.put("nickName", nickName);
                    contentValues.put("description", description);
                    contentValues.put("imgPath", path);
                    contentValues.put("keyId", itemId);

                    mDatabase.insert("history", null, contentValues);
                }


            }
        });


        //设置长按删除事件
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setIcon(R.mipmap.icon_dialog);
                builder.setTitle("提示");
                builder.setMessage("亲，确定删除吗？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //设置动画效果
                        final TranslateAnimation translateAnimation = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, -1,
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 0
                        );

                        translateAnimation.setDuration(1000);

                        view.startAnimation(translateAnimation);

                        //设置动画监听，当动画结束的时候，更新数据源
                        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                //数据源，移除数据
                                mList.remove(position - 2);
                                mAdapter.notifyDataSetChanged();

                                AnimationSet animationSet = new AnimationSet(true);//共享动画插值器

                                AlphaAnimation alphaAnimation = new AlphaAnimation(0.8f, 1);
                                alphaAnimation.setDuration(1000);

                                //TODO 这边做一个平移的动作
                                TranslateAnimation translateAnimation = new TranslateAnimation(
                                        Animation.RELATIVE_TO_SELF, 0,
                                        Animation.RELATIVE_TO_SELF, 0,
                                        Animation.RELATIVE_TO_SELF, 1,
                                        Animation.RELATIVE_TO_SELF, 0
                                );

                                translateAnimation.setDuration(1000);
                                /*ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,
                                                    Animation.RELATIVE_TO_SELF,0.5f,
                                                    Animation.RELATIVE_TO_SELF,0.5f);

                                scaleAnimation.setDuration(1000);*/

                                animationSet.addAnimation(alphaAnimation);
                                //animationSet.addAnimation(scaleAnimation);
                                animationSet.addAnimation(translateAnimation);

                                int count = mListView.getChildCount();

                                //距离最最最开始的时候的头部的距离
                                int currentTop = view.getTop();
                                for (int i = 0; i < count; i++) {
                                    View itemView = mListView.getChildAt(i);
                                    if (itemView.getTop() >= currentTop) {
                                        itemView.setAnimation(animationSet);
                                    }

                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                });


                builder.create().show();

                return true;
            }
        });

    }

    //数据回调，拿到hotData
    @Override
    public void callbackHotData(HotData data) {

        mList.addAll(data.getData());

        //通知适配器
        mAdapter.notifyDataSetChanged();

    }

    private List<ImageView> mHeaderImgs = new ArrayList<>();

    //数据回调得到头布局的图片数据
    @Override
    public void callbackHeaderImg(HeaderImg headerImg) {

        List<HeaderImg.DataBean> data = headerImg.getData();


        for (int i = 0; i < data.size(); i++) {
            String imgPath = data.get(i).getImage();
            //这里有网址
            //Log.d("flag", "----------------->callbackHeaderImg: 照片网址" +i+"="+imgPath);
            final ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.ic_launcher);
            mHeaderImgs.add(imageView);
            new ImageAsyncTask(new ImageCallback() {
                @Override
                public void callbackImage(Bitmap bitmap) {
                    //Log.d("flag", "----------------->callbackImage: 测试点1");
                    if (bitmap != null) {

                        imageView.setImageBitmap(bitmap);

                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    }

                }
            }).execute(imgPath);


        }

        mImgAdapter.notifyDataSetChanged();


    }
}
