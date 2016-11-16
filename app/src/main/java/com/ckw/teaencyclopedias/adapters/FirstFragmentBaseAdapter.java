package com.ckw.teaencyclopedias.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ckw.teaencyclopedias.R;
import com.ckw.teaencyclopedias.beans.HotData;
import com.ckw.teaencyclopedias.interfaces.ImageCallback;
import com.ckw.teaencyclopedias.net.ImageAsyncTask;

import java.util.List;

/**
 * Created by ckw on 2016/11/11.
 */

public class FirstFragmentBaseAdapter extends BaseAdapter {

    private Context mContext;
    private List<HotData.DataBean> mList;

    public FirstFragmentBaseAdapter(Context context, List<HotData.DataBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        ViewHolder holder = null;

        if (convertView != null) {
            ret = convertView;
            holder = (ViewHolder) ret.getTag();

        }else {

            ret = LayoutInflater.from(mContext).inflate(R.layout.list_item_first,parent,false);

            holder = new ViewHolder();
            holder.title = (TextView) ret.findViewById(R.id.title_first);
            holder.source = (TextView) ret.findViewById(R.id.source_first);
            holder.nickName = (TextView) ret.findViewById(R.id.nickName_first);
            holder.time = (TextView) ret.findViewById(R.id.time_first);
            holder.img = (ImageView) ret.findViewById(R.id.img_first);
            holder.description = (TextView) ret.findViewById(R.id.description_first);

            ret.setTag(holder);
        }

        //设置数据
        HotData.DataBean dataBean = mList.get(position);

        holder.title.setText(dataBean.getTitle());
        holder.source.setText(dataBean.getSource());
        holder.nickName.setText(dataBean.getNickname());
        holder.time.setText(dataBean.getCreate_time());

        //用于第四个fragment的一个textView
        String description = dataBean.getDescription();

        if(!description.equals("")){
            holder.description.setText(description);
        }else {
            holder.description.setVisibility(View.INVISIBLE);
        }

        final String imgPath = dataBean.getWap_thumb();
        //Log.d("flag", "----------------->getView: 网址=" +(imgPath.equals("")));
        if (!imgPath.equals("")) {
            final ViewHolder finalHolder = holder;
            finalHolder.img.setTag(imgPath);

            new ImageAsyncTask(new ImageCallback() {
                @Override
                public void callbackImage(Bitmap bitmap) {
                    if(bitmap!=null && finalHolder.img.getTag().equals(imgPath)){
                        finalHolder.img.setImageBitmap(bitmap);
                    }
                }
            }).execute(imgPath);
        }else {
            holder.img.setVisibility(View.INVISIBLE);
       }

        return ret;
    }

    private class ViewHolder{
        ImageView img;
        TextView title,source,nickName,time,description;
    }
}
