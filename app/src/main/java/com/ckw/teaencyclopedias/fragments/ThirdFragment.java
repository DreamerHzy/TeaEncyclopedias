package com.ckw.teaencyclopedias.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ckw.teaencyclopedias.R;
import com.ckw.teaencyclopedias.adapters.FirstFragmentBaseAdapter;
import com.ckw.teaencyclopedias.beans.HotData;
import com.ckw.teaencyclopedias.interfaces.HotDataCallback;
import com.ckw.teaencyclopedias.net.FirstFragmentAsyncTask;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment implements HotDataCallback {

    private int index = 1;
    private String path = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb" +
            "754c33e6&format=json&method=news.getListByType&type=52&rows=15&page="+index;

    private PullToRefreshListView mPullToRefreshListView;

    //数据源
    private List<HotData.DataBean> mList = new ArrayList<>();
    //适配器
    private BaseAdapter mAdapter;

    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_third, container, false);

        initData();

        initPullToRefreshListView(ret);

        return ret;
    }

    private void initPullToRefreshListView(View ret) {
        mPullToRefreshListView = (PullToRefreshListView)
                ret.findViewById(R.id.pullToRefreshListView_3);

        mAdapter = new FirstFragmentBaseAdapter(getContext(),mList);

        mPullToRefreshListView.setAdapter(mAdapter);
    }

    private void initData() {
        new FirstFragmentAsyncTask(this).execute(path);
    }

    @Override
    public void callbackHotData(HotData data) {
        mList.addAll(data.getData());

        //通知适配器
        mAdapter.notifyDataSetChanged();
    }
}
