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



//整洁卫生
public class ZhengjieView extends RelativeLayout {
    @Bind(R.id.num_tv)
    TextView mNumTv;
    @Bind(R.id.seekbar1)
    public SeekBar mSeekbar1;
    @Bind(R.id.num_tv2)
    TextView mNumTv2;
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
    public  SeekBar mSeekbar8;
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
    @Bind(R.id.num_tv13)
    TextView mNumTv13;
    @Bind(R.id.seekbar13)
    public SeekBar mSeekbar13;
    @Bind(R.id.num_tv14)
    TextView mNumTv14;
    @Bind(R.id.seekbar14)
    public SeekBar mSeekbar14;
    @Bind(R.id.num_tv15)
    TextView mNumTv15;
    @Bind(R.id.seekbar15)
    public SeekBar mSeekbar15;
    @Bind(R.id.num_tv16)
    TextView mNumTv16;
    @Bind(R.id.seekbar16)
    public SeekBar mSeekbar16;
    @Bind(R.id.before_cnwupf)
    public ImageView mBeforeCnwupf;
    @Bind(R.id.after_cnwupf)
    public ImageView mAfterCnwupf;
    @Bind(R.id.before_ktlx)
    public ImageView mBeforeKtlx;
    @Bind(R.id.after_ktlx)
    public ImageView mAfterKtlx;
    @Bind(R.id.before_swcz)
    public ImageView mBeforeSwcz;
    @Bind(R.id.after_swcz)
    public ImageView mAfterSwcz;
    @Bind(R.id.befoew_nsjjd)
    public ImageView mBefoewNsjjd;
    @Bind(R.id.after_nsjjd)
    public ImageView mAfterNsjjd;
    @Bind(R.id.before_cnfc)
    public ImageView mBeforeCnfc;
    @Bind(R.id.after_cnfc)
    public ImageView mAfterCnfc;
    @Bind(R.id.before_sjxd)
    public ImageView mBeforeSjxd;
    @Bind(R.id.after_sjxd)
    public ImageView mAfterSjxd;
    @Bind(R.id.before_kqjh)
    public ImageView mBeforeKqjh;
    @Bind(R.id.after_kqjh)
    public ImageView mAfterKqjh;
    @Bind(R.id.before_cnqx)
    public ImageView mBeforeCnqx;
    @Bind(R.id.after_cnqx)
    public ImageView mAfterCnqx;
    private int numbers = 100;
    private Context mContext;

    public ZhengjieView(Context context) {
        this(context, null);
    }

    public ZhengjieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_zhengjie, this);
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
        SeekBarUtils.initSeekBarProgress(mSeekbar13, mNumTv13, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar14, mNumTv14, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar15, mNumTv15, numbers);
        SeekBarUtils.initSeekBarProgress(mSeekbar16, mNumTv16, numbers);

    }

    public void setData(DaoQueryBean daoQueryBean) {
        if (daoQueryBean == null)
            return;
        //物品摆放
        BItmapUtil.loadImageForIv(daoQueryBean.beforeWpbfPicUrl, mBeforeCnwupf, daoQueryBean.beforeWpbfScore, mSeekbar1, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterWpbfPicUrl, mAfterCnwupf, daoQueryBean.afterWpbfScore, mSeekbar2, mContext);
        //滤芯洁净度
        BItmapUtil.loadImageForIv(daoQueryBean.beforeKqlxPicUrl, mBeforeKtlx, daoQueryBean.beforeKqlxScore, mSeekbar3, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterKqlxPicUrl, mAfterKtlx, daoQueryBean.afterKqlxScore, mSeekbar4, mContext);
        //食物残渣
        BItmapUtil.loadImageForIv(daoQueryBean.beforeSwczPicUrl, mBeforeSwcz, daoQueryBean.beforeSwczScore, mSeekbar5, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterSwczPicUrl, mAfterSwcz, daoQueryBean.afterSwczScore, mSeekbar6, mContext);
        //内饰洁净度
        BItmapUtil.loadImageForIv(daoQueryBean.beforeNsjjdPicUrl, mBefoewNsjjd, daoQueryBean.beforeNsjjdScore, mSeekbar7, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterNsjjdPicUrl, mAfterNsjjd, daoQueryBean.afterNsjjdScore, mSeekbar8, mContext);
        //车内粉尘污染
        BItmapUtil.loadImageForIv(daoQueryBean.beforeCnfcwrPicUrl, mBeforeCnfc, daoQueryBean.beforeCnfcwrScore, mSeekbar9, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterCnfcwrPicUrl, mAfterCnfc, daoQueryBean.afterCnfcwrScore, mSeekbar10, mContext);
        //定期车内杀菌消毒
        BItmapUtil.loadImageForIv(daoQueryBean.beforeXdsjPicUrl, mBeforeSjxd, daoQueryBean.beforeXdsjScore, mSeekbar11, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterXdsjPicUrl, mAfterSjxd, daoQueryBean.afterXdsjScore, mSeekbar12, mContext);
        //空气净化
        BItmapUtil.loadImageForIv(daoQueryBean.beforeKqjhPicUrl, mBeforeKqjh, daoQueryBean.beforeKqjhScore, mSeekbar13, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterKqjhPicUrl, mAfterKqjh, daoQueryBean.afterKqjhScore, mSeekbar14, mContext);
        //定期车内清洗
        BItmapUtil.loadImageForIv(daoQueryBean.beforeCnjxPicUrl, mBeforeCnqx, daoQueryBean.beforeCnjxScore, mSeekbar15, mContext);
        BItmapUtil.loadImageForIv(daoQueryBean.afterCnjxPicUrl, mAfterCnqx, daoQueryBean.afterCnjxScore, mSeekbar16, mContext);

    }


}
