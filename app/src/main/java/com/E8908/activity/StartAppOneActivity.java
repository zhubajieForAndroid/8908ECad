package com.E8908.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.base.BaseToolBarActivity;
import com.E8908.util.DataUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StartAppOneActivity extends BaseToolBarActivity implements View.OnClickListener {


    @Bind(R.id.in_home)
    ImageView mInHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app_one);
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {
        mInHome.setOnClickListener(this);

    }

    @Override
    public int getToolbarImage() {
        return R.mipmap.top_bar_6_1;
    }

    @Override
    protected void equipmentData(byte[] buffer) {}

    @Override
    protected void setResultData(byte[] buffer) {}

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
