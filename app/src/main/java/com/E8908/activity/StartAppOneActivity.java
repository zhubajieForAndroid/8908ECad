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
    private String mName;
    private String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app_one);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mUserId = intent.getStringExtra("userId");
        initData();
    }



    @Override
    protected void isYesData(boolean isdata) {

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
        intent.putExtra("name",mName);
        intent.putExtra("userId",mUserId);
        startActivity(intent);
        finish();
    }
}
