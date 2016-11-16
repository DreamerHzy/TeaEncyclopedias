package com.ckw.teaencyclopedias.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by ckw on 2016/11/14.
 */

public class HeaderImgAdapter extends PagerAdapter {

    private List<ImageView> data;

    public HeaderImgAdapter(List<ImageView> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        /**
         * ViewGroup parent = (ViewGroup) v.getParent();
         if (parent != null) {
         parent.removeAllViews();
         }
        container.addView(v);
        */

        /*if(data.size()==0){
            return null;
        }*/
        View v = data.get(position);
        ViewGroup parent = (ViewGroup) v.getParent();
        if(parent != null){
            parent.removeAllViews();
        }
        container.addView(v);

        return v;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);

        container.removeView(data.get(position));
    }


}
