package com.ckw.teaencyclopedias.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ckw.teaencyclopedias.R;

public class CopyRightActivity extends AppCompatActivity {

    private TextView loadMore;
    private TextView copyRight_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_right);

        getSupportActionBar().hide();

        initView();

    }

    private void initView() {
        loadMore = (TextView) findViewById(R.id.loadmore);
        copyRight_content = (TextView) findViewById(R.id.copy_content);

        loadMore.setGravity(Gravity.CENTER);
    }

    public void clickForMore(View view) {
        if(copyRight_content.getLineCount()==2){
            loadMore.setText("收起");
            copyRight_content.setMaxLines(3);
        }else{
            copyRight_content.setMaxLines(2);
            loadMore.setText("更多");
        }
    }
}
