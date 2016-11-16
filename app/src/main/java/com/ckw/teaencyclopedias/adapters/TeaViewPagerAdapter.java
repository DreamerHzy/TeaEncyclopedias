package com.ckw.teaencyclopedias.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ckw on 2016/11/11.
 */

public class TeaViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> data;
    private String[] titles;

    public TeaViewPagerAdapter(FragmentManager fm, List<Fragment> data, String[] titles) {
        super(fm);
        this.data = data;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }
}
