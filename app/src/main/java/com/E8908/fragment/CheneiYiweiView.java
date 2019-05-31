package com.E8908.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.db.DaoQueryBean;
import com.E8908.util.SeekBarUtils;
import com.E8908.widget.MySeekbar;

import butterknife.Bind;
import butterknife.ButterKnife;

//车内异味
public class CheneiYiweiView extends RelativeLayout {

    @Bind(R.id.before_cfk_score)
    TextView mBeforeCfkScore;
    @Bind(R.id.seekbar1)
    public MySeekbar mSeekbar1;
    @Bind(R.id.seekbar2)
    public MySeekbar mSeekbar2;
    @Bind(R.id.after_cfk_score)
    TextView mAfterCfkScore;
    @Bind(R.id.before_xcyw_score)
    TextView mBeforeXcywScore;
    @Bind(R.id.seekbar3)
    public MySeekbar mSeekbar3;
    @Bind(R.id.seekbar4)
    public MySeekbar mSeekbar4;
    @Bind(R.id.after_xcyw_score)
    TextView mAfterXcywScore;
    @Bind(R.id.before_nspgyw_score)
    TextView mBeforeNspgywScore;
    @Bind(R.id.seekbar5)
    public MySeekbar mSeekbar5;
    @Bind(R.id.seekbar6)
    public MySeekbar mSeekbar6;
    @Bind(R.id.after_nspgyw_score)
    TextView mAfterNspgywScore;
    @Bind(R.id.before_nsxfyw_score)
    TextView mBeforeNsxfywScore;
    @Bind(R.id.seekbar7)
    public MySeekbar mSeekbar7;
    @Bind(R.id.seekbar8)
    public MySeekbar mSeekbar8;
    @Bind(R.id.after_nsxfyw_score)
    TextView mAfterNsxfywScore;
    @Bind(R.id.before_scwpyw_score)
    TextView mBeforeScwpywScore;
    @Bind(R.id.seekbar9)
    public MySeekbar mSeekbar9;
    @Bind(R.id.seekbar10)
    public MySeekbar mSeekbar10;
    @Bind(R.id.after_scwpyw_score)
    TextView mAfterScwpywScore;
    @Bind(R.id.before_esy_score)
    TextView mBeforeEsyScore;
    @Bind(R.id.seekbar11)
    public MySeekbar mSeekbar11;
    @Bind(R.id.seekbar12)
    public MySeekbar mSeekbar12;
    @Bind(R.id.after_esy_score)
    TextView mAfterEsyScore;
    @Bind(R.id.before_xjmbyw_score)
    TextView mBeforeXjmbywScore;
    @Bind(R.id.seekbar13)
    public MySeekbar mSeekbar13;
    @Bind(R.id.seekbar14)
    public MySeekbar mSeekbar14;
    @Bind(R.id.after_xjmbyw_score)
    TextView mAfterXjmbywScore;
    @Bind(R.id.before_qtyw_score)
    TextView mBeforeQtywScore;
    @Bind(R.id.seekbar15)
    public MySeekbar mSeekbar15;
    @Bind(R.id.seekbar16)
    public MySeekbar mSeekbar16;
    @Bind(R.id.after_qtyw_score)
    TextView mAfterQtywScore;
    private int number = 100;

    public CheneiYiweiView(Context context) {
        this(context, null);
    }

    public CheneiYiweiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_cheneiyiwei, this);
        ButterKnife.bind(this, this);
        SeekBarUtils.setTextByProgress(mSeekbar1, mBeforeCfkScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar2, mAfterCfkScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar3, mBeforeXcywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar4, mAfterXcywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar5, mBeforeNspgywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar6, mAfterNspgywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar7, mBeforeNsxfywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar8, mAfterNsxfywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar9, mBeforeScwpywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar10, mAfterScwpywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar11, mBeforeEsyScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar12, mAfterEsyScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar13, mBeforeXjmbywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar14, mAfterXjmbywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar15, mBeforeQtywScore, number);
        SeekBarUtils.setTextByProgress(mSeekbar16, mAfterQtywScore, number);
    }

    public void setData(DaoQueryBean daoQueryBean) {
        if (daoQueryBean == null)
            return;
        //出风口异味
        checkEmpty(daoQueryBean.beforeCfkyw,mSeekbar1,mBeforeCfkScore);
        checkEmpty(daoQueryBean.afterCfkyw,mSeekbar2,mAfterCfkScore);
        //新车异味
        checkEmpty(daoQueryBean.beforeXcyw,mSeekbar3,mBeforeXcywScore);
        checkEmpty(daoQueryBean.afterXcyw,mSeekbar4,mAfterXcywScore);
        //内饰皮革味
        checkEmpty(daoQueryBean.beforeNspgyw,mSeekbar5,mBeforeNspgywScore);
        checkEmpty(daoQueryBean.afterNspgyw,mSeekbar6,mAfterNspgywScore);
        //内饰吸附异味
        checkEmpty(daoQueryBean.beforeNsxfyw,mSeekbar7,mBeforeNsxfywScore);
        checkEmpty(daoQueryBean.afterNsxfyw,mSeekbar8,mAfterNsxfywScore);
        //随车物品异味
        checkEmpty(daoQueryBean.beforeScwpyw,mSeekbar9,mBeforeScwpywScore);
        checkEmpty(daoQueryBean.afterScwpyw,mSeekbar10,mAfterScwpywScore);
        //二手烟异味
        checkEmpty(daoQueryBean.beforeEsyyw,mSeekbar11,mBeforeEsyScore);
        checkEmpty(daoQueryBean.afterEsyyw,mSeekbar12,mAfterEsyScore);
        //细菌霉变异味
        checkEmpty(daoQueryBean.beforeXjmbyw,mSeekbar13,mBeforeXjmbywScore);
        checkEmpty(daoQueryBean.afterXjmbyw,mSeekbar14,mAfterXjmbywScore);
        //其他异味
        checkEmpty(daoQueryBean.beforeQtyw,mSeekbar15,mBeforeQtywScore);
        checkEmpty(daoQueryBean.afterQtyw,mSeekbar16,mAfterQtywScore);

    }
    private void checkEmpty(String beforeNsxfyw, MySeekbar seekBar, TextView beforeNsxfywScore) {
        if (!TextUtils.isEmpty(beforeNsxfyw)){
            float resultInt = Float.parseFloat(beforeNsxfyw);
            beforeNsxfywScore.setText((int)resultInt+"分");
            seekBar.setProgress((int) resultInt);
        }else {
            beforeNsxfywScore.setText("100分");
            seekBar.setProgress(100);
        }
    }

}
