package com.E8908.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.db.DaoQueryBean;
import com.E8908.util.BItmapUtil;
import com.E8908.util.SeekBarUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

//空调系统
public class KongtiaoView extends RelativeLayout {
    @Bind(R.id.num_tv)
    TextView mNumTv;
    @Bind(R.id.seekbar1)
    public SeekBar mSeekbar1;
    @Bind(R.id.num_tv2)
    public TextView mNumTv2;
    @Bind(R.id.seekbar2)
    public SeekBar mSeekbar2;
    @Bind(R.id.num_tv3)
    TextView mNumTv3;
    @Bind(R.id.seekbar3)
    public SeekBar mSeekbar3;
    @Bind(R.id.num_tv4)
    TextView mNumTv4;
    @Bind(R.id.seekbar4)
    public SeekBar mSeekbar4;
    @Bind(R.id.num_tv5)
    TextView mNumTv5;
    @Bind(R.id.seekbar5)
    public SeekBar mSeekbar5;
    @Bind(R.id.num_tv6)
    TextView mNumTv6;
    @Bind(R.id.seekbar6)
    public SeekBar mSeekbar6;
    @Bind(R.id.num_tv7)
    TextView mNumTv7;
    @Bind(R.id.seekbar7)
    public SeekBar mSeekbar7;
    @Bind(R.id.num_tv8)
    TextView mNumTv8;
    @Bind(R.id.seekbar8)
    public SeekBar mSeekbar8;
    @Bind(R.id.num_tv9)
    TextView mNumTv9;
    @Bind(R.id.seekbar9)
    public SeekBar mSeekbar9;
    @Bind(R.id.num_tv10)
    TextView mNumTv10;
    @Bind(R.id.seekbar10)
    public SeekBar mSeekbar10;
    @Bind(R.id.num_tv11)
    TextView mNumTv11;
    @Bind(R.id.seekbar11)
    public SeekBar mSeekbar11;
    @Bind(R.id.num_tv12)
    TextView mNumTv12;
    @Bind(R.id.seekbar12)
    public SeekBar mSeekbar12;
    @Bind(R.id.num_tv17)
    TextView mNumTv17;
    @Bind(R.id.seekbar17)
    public SeekBar mSeekbar17;
    @Bind(R.id.num_tv18)
    TextView mNumTv18;
    @Bind(R.id.seekbar18)
    public SeekBar mSeekbar18;
    @Bind(R.id.before_ktlxbmwg)
    public ImageView mBeforeKtlx;
    @Bind(R.id.after_ktlxbmwg)
    public ImageView mAfterKtlx;
    @Bind(R.id.before_zfx)
    public ImageView mBeforeZfx;
    @Bind(R.id.after_zfx)
    public ImageView mAfterZfx;
    @Bind(R.id.before_gfj)
    public ImageView mBeforeGfj;
    @Bind(R.id.after_gfj)
    public ImageView mAfterGfj;
    @Bind(R.id.before_tfgd)
    public ImageView mBeforeTfgd;
    @Bind(R.id.after_tfgd)
    public ImageView mAfterTfgd;
    @Bind(R.id.befoew_lnq)
    public ImageView mBefoewLnq;
    @Bind(R.id.after_lnq)
    public ImageView mAfterLnq;
    @Bind(R.id.before_ktzl)
    public ImageView mBeforeKtzl;
    @Bind(R.id.after_ktzl)
    public ImageView mAfterKtzl;
    @Bind(R.id.before_ktyx)
    public ImageView mBeforeKtyx;
    @Bind(R.id.after_ktyx)
    public ImageView mAfterKtyx;
    private int numbers = 100;
    private Context mContext;

    public KongtiaoView(Context context) {
        this(context, null);
    }

    public KongtiaoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_kongtiao, this);
        ButterKnife.bind(this, this);
        SeekBarUtils.initSeekBarProgress(mSeekbar1, mNumTv, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar2, mNumTv2, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar3, mNumTv3, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar4, mNumTv4, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar5, mNumTv5, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar6, mNumTv6, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar7, mNumTv7, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar8, mNumTv8, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar9, mNumTv9, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar10, mNumTv10, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar11, mNumTv11, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar12, mNumTv12, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar17, mNumTv17, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar18, mNumTv18, numbers);
    }

    public void setData(DaoQueryBean daoQueryBean) {
        if (daoQueryBean == null)
            return;
        //空调滤芯表面污垢
        BItmapUtil.loadImageForIv(daoQueryBean.beforeLxbmwgPicUrl, mBeforeKtlx, daoQueryBean.beforeLxbmwgScore, mSeekbar1, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterLxbmwgPicUrl, mAfterKtlx, daoQueryBean.afterLxbmwgScore, mSeekbar2, mContext);
        //蒸发箱表面污垢
        BItmapUtil.loadImageForIv(daoQueryBean.beforeZfxbmwgPicUrl, mBeforeZfx, daoQueryBean.beforeZfxbmwgScore, mSeekbar3, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterZfxbmwgPicUrl, mAfterZfx, daoQueryBean.afterZfxbmwgScore, mSeekbar4, mContext);
        //鼓风机表面污垢
        BItmapUtil.loadImageForIv(daoQueryBean.beforeGfjbmwgPicUrl, mBeforeGfj, daoQueryBean.beforeGfjbmwgScore, mSeekbar5, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterGfjbmwgPicUrl, mAfterGfj, daoQueryBean.afterGfjbmwgScore, mSeekbar6, mContext);
        //通风管道表面污垢
        BItmapUtil.loadImageForIv(daoQueryBean.beforeTfgdbmwgPicUrl, mBeforeTfgd, daoQueryBean.beforeTfgdbmwgScore, mSeekbar7, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterTfgdbmwgPicUrl, mAfterTfgd, daoQueryBean.afterTfgdbmwgScore, mSeekbar8, mContext);
        //冷凝器表面污垢
        BItmapUtil.loadImageForIv(daoQueryBean.beforeLnqbmwgPicUrl, mBefoewLnq, daoQueryBean.beforeLnqbmwgScore, mSeekbar9, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterLnqbmwgPicUrl, mAfterLnq, daoQueryBean.afterLnqbmwgScore, mSeekbar10, mContext);
        //空调系统制冷新能
        BItmapUtil.loadImageForIv(daoQueryBean.beforeKtxtzlxnPicUrl, mBeforeKtzl, daoQueryBean.beforeKtxtzlxnScore, mSeekbar11, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterKtxtzlxnPicUrl, mAfterKtzl, daoQueryBean.afterKtxtzlxnScore, mSeekbar12, mContext);
        //开启空调时异向
        BItmapUtil.loadImageForIv(daoQueryBean.beforeKtkqyxPicUrl, mBeforeKtyx, daoQueryBean.beforeKtkqyxScore, mSeekbar17, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterKtkqyxPicUrl, mAfterKtyx, daoQueryBean.afterKtkqyxScore, mSeekbar18, mContext);
    }
}
