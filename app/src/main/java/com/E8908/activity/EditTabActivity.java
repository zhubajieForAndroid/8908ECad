package com.E8908.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.adapter.CreateTabAdapter;
import com.E8908.base.BaseToolBarActivity;
import com.E8908.base.QuerySuccess;
import com.E8908.bean.CarNumberBeforeAndAfterBean;
import com.E8908.conf.Constants;
import com.E8908.db.DaoQueryBean;
import com.E8908.db.DaoUtil;
import com.E8908.db.GasDataBaseDao;
import com.E8908.fragment.CheneiYiweiView;
import com.E8908.fragment.KongtiaoView;
import com.E8908.fragment.ZhengjieView;
import com.E8908.impl.CreateTabPersenterImpl;
import com.E8908.util.CheckCameraUtils;
import com.E8908.util.DataUtil;
import com.E8908.util.FileUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.view.BaseView;
import com.E8908.widget.InputCarNumberHinDialog;
import com.E8908.widget.SelectImageDialog;
import com.E8908.widget.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class EditTabActivity extends BaseToolBarActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, QuerySuccess, CreateTabAdapter.OnCreateWpbfView, CreateTabAdapter.OnCreateKtxtView, CreateTabAdapter.OnCheneiYiweiCreatedListener, BaseView<String>, SelectImageDialog.OnSelectAlbumBtnListener, SelectImageDialog.OnSelectCameraBtnListener {

    private static final String TAG = "EditTabActivity";
    @Bind(R.id.car_number)
    TextView mCarNumber;
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.save_tab)
    Button mSaveTab;
    @Bind(R.id.back_btn)
    Button mBackBtn;
    @Bind(R.id.progress_bar)
    LinearLayout mProgressBar;
    @Bind(R.id.updata_tab)
    Button mUpdataTab;
    @Bind(R.id.push_box_btn)
    CheckBox mPushBoxBtn;
    @Bind(R.id.delece_btn)
    Button mDeleceBtn;
    private String[] mTitles = {"车内整洁卫生", "空调系统检测", "车内异味检测", "有害气体检测"};
    private DaoQueryBean mDaoQueryBean;
    private CreateTabAdapter mAdapter;
    private ZhengjieView mZhengjieView;        //车内整洁卫生
    private KongtiaoView mKongtiaoView;     //空调系统检测
    private CheneiYiweiView mCheneiYiweiView;  //车内异味检测
    private DaoUtil mDaoUtil;
    private String mBeforeCnwpbfFile;
    private String mAfterCnwpbfFile;
    private String mBeforeKtlxFile;
    private String mAfterKtlxFile;
    private String mBeforeSwczFile;
    private String mAfterSwczFile;
    private String mBeforeNsjjdFile;
    private String mAfterNsjjdFile;
    private String mBeforeCnfcFile;
    private String mAfterCnfcFile;
    private String mBeforeSjxdFile;
    private String mAfterSjxdFile;
    private String mBeforeKqjhFile;
    private String mAfterKqjhFile;
    private String mBeforeCnqxFile;
    private String mAfterCnqxFile;
    private String mBeforeKtlxbmwgFile;
    private String mAfterKtlxbmwgFile;
    private String mBeforeZfxFile;
    private String mAfterZfxFile;
    private String mBeforeGfjFile;
    private String mAfterGfjFile;
    private String mBeforeTfgdFile;
    private String mAfterTfgdFile;
    private String mBeforeLnqFile;
    private String mAfterLnqFile;
    private String mBeforeKtzlFile;
    private String mAfterKtzlFile;
    private String mBeforeKtyxFile;
    private String mAfterKtyxFile;
    private Handler mHandler;
    private String mCarNumberStr;
    private String mEquipmentId;
    private Map<String, String> stringPames;
    private boolean isLinked = false;               //检测器是否连接并且预热完成
    private boolean mIsCreate;
    private SelectImageDialog mSelectImgDialog;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;           //申请相机权限的申请码
    private int mSelectCurrentImgState;
    private Uri mTempUri;
    private boolean isCanmera;                  //是否是拍照回调
    private static final int REQUEST_CAMERA = 101;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private File mFile;
    private InputCarNumberHinDialog mInputCarNumberDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tab);
        ButterKnife.bind(this);
        mInputCarNumberDialog = new InputCarNumberHinDialog(this, R.style.dialog);
        stringPames = new HashMap<>();
        mHandler = new Handler();
        initData();
        initListener();
    }

    private void initData() {
        mSelectImgDialog = new SelectImageDialog(this, R.style.dialog);
        mSelectImgDialog.setImg(R.mipmap.popup_photo);

        Intent intent = getIntent();
        mCarNumberStr = intent.getStringExtra("carNumber");
        mIsCreate = intent.getBooleanExtra("isCreate", false);
        mEquipmentId = intent.getStringExtra("equipmentId");
        mCarNumber.setText(mCarNumberStr);
        mAdapter = new CreateTabAdapter(this, mTitles, mDaoQueryBean);
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOffscreenPageLimit(3);
        mTab.setupWithViewPager(mViewpager);
        if (mIsCreate) {//新建报告(在保存草稿的时候如果有草稿就会替换草稿,如果没有就会新建)
            mDeleceBtn.setVisibility(View.GONE);
        } else {
            mDeleceBtn.setVisibility(View.VISIBLE);
            //读取数据库中的数据
            readDataBase();
        }

    }
    private void initListener() {
        mSelectImgDialog.setOnSelectAlbumBtnListener(this);
        mSelectImgDialog.setOnSelectCameraBtnListener(this);
        mViewpager.addOnPageChangeListener(this);
        mUpdataTab.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mDeleceBtn.setOnClickListener(this);
        mSaveTab.setOnClickListener(this);
        mAdapter.setOnCreateWpbfView(this);
        mAdapter.setOnCreateKtxtView(this);
        mAdapter.setOnCheneiYiweiCreatedListener(this);
    }

    private void readDataBase() {
        mDaoUtil = new DaoUtil(new GasDataBaseDao(this), this);
        mDaoUtil.query();
    }

    @Override
    public int getToolbarImage() {
        return R.mipmap.create_report_nav;
    }

    @Override
    protected void equipmentData(byte[] buffer) {
        String checkGasState = DataUtil.getCheckGasState(buffer);       //气体检测仪的状态
        String connectState = checkGasState.substring(0, 1);            //检测仪连接状态
        String preheatState = checkGasState.substring(7);  //预热状态
        if ("1".equals(connectState) && "0".equals(preheatState)) {              //连接成功并且预热完成
            isLinked = true;
        } else {             //没有连接或者没有预热好
            isLinked = false;
        }
    }

    @Override
    protected void setResultData(byte[] buffer) {
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
        if (i == 3) {
            Intent intent = new Intent(this, CheckGasActivity.class);
            startActivity(intent);
            mViewpager.setCurrentItem(0);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delece_btn:           //删除草稿
                if (!mInputCarNumberDialog.isShowing()) {
                    mInputCarNumberDialog.setBitmap(R.mipmap.delete_popup);
                    mInputCarNumberDialog.show();
                    mInputCarNumberDialog.setOnMakeSureBtnClickListener(new InputCarNumberHinDialog.OnMakeSureBtnClickListener() {
                        @Override
                        public void onBtnClick() {
                            deleteTab();
                        }
                    });
                }
                break;
            case R.id.updata_tab:           //上传报告/草稿
                if (isLinked) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    //根据车牌获取施工前后气体数据
                    getBeforeAndAfterByCarnumber();
                } else {
                    ToastUtil.showMessage("您还没有开启检测");
                }
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.save_tab:             //保存草稿,如果是编辑草稿一进来就会先查询数据库得到mDaoQueryBean对象
                if (mIsCreate) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mDaoUtil = new DaoUtil(new GasDataBaseDao(this), this);
                    mDaoUtil.query();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    getAllData();
                }

                break;
            case R.id.before_cnwupf:        //车内物品摆放施工前
                mSelectCurrentImgState = 10;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_cnwupf:         //施工后
                mSelectCurrentImgState = 11;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_ktlx:          //空调滤芯施工前
                mSelectCurrentImgState = 12;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_ktlx:           //施工后
                mSelectCurrentImgState = 13;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_swcz:           //食物残渣施工前
                mSelectCurrentImgState = 14;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_swcz:           //施工后
                mSelectCurrentImgState = 15;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.befoew_nsjjd:           //内饰洁净度施工前
                mSelectCurrentImgState = 16;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_nsjjd:           //施工后
                mSelectCurrentImgState = 17;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_cnfc:           //车内粉尘施工前
                mSelectCurrentImgState = 18;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_cnfc:           //施工后
                mSelectCurrentImgState = 19;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_sjxd:           //车内定期杀菌消毒施工前
                mSelectCurrentImgState = 20;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_sjxd:           //施工后
                mSelectCurrentImgState = 21;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_kqjh:           //车内定期空气净化施工前
                mSelectCurrentImgState = 22;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_kqjh:           //施工后
                mSelectCurrentImgState = 23;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_cnqx:           //车内定期清洗施工前
                mSelectCurrentImgState = 24;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_cnqx:           //施工后
                mSelectCurrentImgState = 25;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_ktlxbmwg:      //空调滤芯表面污垢
                mSelectCurrentImgState = 26;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_ktlxbmwg:
                mSelectCurrentImgState = 27;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_zfx:      //蒸发箱表面污垢
                mSelectCurrentImgState = 28;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_zfx:
                mSelectCurrentImgState = 29;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_gfj:      //鼓风机表面污垢
                mSelectCurrentImgState = 30;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_gfj:
                mSelectCurrentImgState = 31;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_tfgd:      //通风管道表面污垢
                mSelectCurrentImgState = 32;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_tfgd:
                mSelectCurrentImgState = 33;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.befoew_lnq:      //冷凝器表面污垢
                mSelectCurrentImgState = 34;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_lnq:
                mSelectCurrentImgState = 35;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_ktzl:      //空调制冷性能
                mSelectCurrentImgState = 36;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_ktzl:
                mSelectCurrentImgState = 37;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.before_ktyx:      //开启空调异响
                mSelectCurrentImgState = 38;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
            case R.id.after_ktyx:
                mSelectCurrentImgState = 39;
                if (!mSelectImgDialog.isShowing()) {
                    mSelectImgDialog.show();
                }
                break;
        }
    }


    private void getBeforeAndAfterByCarnumber() {
        Map<String, String> pames = new HashMap<>();
        pames.put("carNumber", mCarNumberStr);
        OkhttpManager manager = OkhttpManager.getOkhttpManager();
        manager.doPost(Constants.URLS.GET_BEFORE_AND_AFTER_BY_CARNUMBER, pames, mCallback);
    }

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String string = response.body().string();
            if (!TextUtils.isEmpty(string) && string.length() > 9) {
                String substring = string.substring(8, 9);
                if ("0".equals(substring)) {
                    Gson gson = new Gson();
                    CarNumberBeforeAndAfterBean bean = gson.fromJson(string, CarNumberBeforeAndAfterBean.class);
                    CarNumberBeforeAndAfterBean.ResponseBean beanResponse = bean.getResponse();
                    CarNumberBeforeAndAfterBean.ResponseBean.AfterBean after = beanResponse.getAfter();
                    CarNumberBeforeAndAfterBean.ResponseBean.BeforeBean before = beanResponse.getBefore();
                    checkEmpty(after.getFormaldehyde(), "afterJq");
                    checkEmpty(after.getTvoc(), "afterTvoc");
                    checkEmpty(after.getPm25(), "afterPm25");
                    checkEmpty(after.getTemperature(), "afterWd");
                    checkEmpty(after.getHumidity(), "afterSd");

                    checkEmpty(before.getFormaldehyde(), "beforeJq");
                    checkEmpty(before.getTvoc(), "beforeTvoc");
                    checkEmpty(before.getPm25(), "beforePm25");
                    checkEmpty(before.getTemperature(), "beforeWd");
                    checkEmpty(before.getHumidity(), "beforeSd");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            upDataTab();
                        }
                    });

                }
            }
        }
    };

    private void checkEmpty(String afterFormaldehyde, String afterJq) {
        if (!TextUtils.isEmpty(afterFormaldehyde)) {
            stringPames.put(afterJq, afterFormaldehyde);
        } else {
            stringPames.put(afterJq, "0");
        }
    }

    private void deleteTab() {
        mProgressBar.setVisibility(View.VISIBLE);
        mDaoUtil.delete();
    }

    private void upDataTab() {
        //创建报告
        //添加气体数据,创建人,设备ID,车牌号
        stringPames.put("createUser", Constants.CREATE_USER_NAME);
        stringPames.put("carNumber", mCarNumberStr);
        stringPames.put("deviceno", mEquipmentId);
        stringPames.put("beforeWpbfScore", mZhengjieView.mSeekbar1.getProgress() + "");
        stringPames.put("afterWpbfScore", mZhengjieView.mSeekbar2.getProgress() + "");
        stringPames.put("beforeKqlxScore", mZhengjieView.mSeekbar3.getProgress() + "");
        stringPames.put("afterKqlxScore", mZhengjieView.mSeekbar4.getProgress() + "");
        stringPames.put("beforeCnfcwrScore", mZhengjieView.mSeekbar9.getProgress() + "");
        stringPames.put("afterCnfcwrScore", mZhengjieView.mSeekbar10.getProgress() + "");
        stringPames.put("beforeXdsjScore", mZhengjieView.mSeekbar11.getProgress() + "");
        stringPames.put("afterXdsjScore", mZhengjieView.mSeekbar12.getProgress() + "");
        stringPames.put("beforeSwczScore", mZhengjieView.mSeekbar5.getProgress() + "");
        stringPames.put("afterSwczScore", mZhengjieView.mSeekbar6.getProgress() + "");
        stringPames.put("beforeKqjhScore", mZhengjieView.mSeekbar13.getProgress() + "");
        stringPames.put("afterKqjhScore", mZhengjieView.mSeekbar14.getProgress() + "");
        stringPames.put("beforeNsjjdScore", mZhengjieView.mSeekbar7.getProgress() + "");
        stringPames.put("afterNsjjdScore", mZhengjieView.mSeekbar8.getProgress() + "");
        stringPames.put("beforeCnjxScore", mZhengjieView.mSeekbar15.getProgress() + "");
        stringPames.put("afterCnjxScore", mZhengjieView.mSeekbar16.getProgress() + "");
        stringPames.put("beforeLxbmwgScore", mKongtiaoView.mSeekbar1.getProgress() + "");
        stringPames.put("afterLxbmwgScore", mKongtiaoView.mSeekbar2.getProgress() + "");
        stringPames.put("beforeZfxbmwgScore", mKongtiaoView.mSeekbar3.getProgress() + "");
        stringPames.put("afterZfxbmwgScore", mKongtiaoView.mSeekbar4.getProgress() + "");
        stringPames.put("beforeGfjbmwgScore", mKongtiaoView.mSeekbar5.getProgress() + "");
        stringPames.put("afterGfjbmwgScore", mKongtiaoView.mSeekbar6.getProgress() + "");
        stringPames.put("beforeTfgdbmwgScore", mKongtiaoView.mSeekbar7.getProgress() + "");
        stringPames.put("afterTfgdbmwgScore", mKongtiaoView.mSeekbar8.getProgress() + "");
        stringPames.put("beforeLnqbmwgScore", mKongtiaoView.mSeekbar9.getProgress() + "");
        stringPames.put("afterLnqbmwgScore", mKongtiaoView.mSeekbar10.getProgress() + "");
        stringPames.put("beforeKtxtzlxnScore", mKongtiaoView.mSeekbar11.getProgress() + "");
        stringPames.put("afterKtxtzlxnScore", mKongtiaoView.mSeekbar12.getProgress() + "");
        stringPames.put("beforeKtkqyxScore", mKongtiaoView.mSeekbar17.getProgress() + "");
        stringPames.put("afterKtkqyxScore", mKongtiaoView.mSeekbar18.getProgress() + "");
        boolean checked = mPushBoxBtn.isChecked();          //是否直接推送报告给车主
        if (checked) {
            stringPames.put("pushmessage", "1");
        } else {
            stringPames.put("pushmessage", "0");
        }
        //异味
        getYiweiPames();

        CreateTabPersenterImpl createTabPersenter = new CreateTabPersenterImpl(this);
        createTabPersenter.loadData(Constants.URLS.CREATE_TAB, stringPames);
    }

    private void getYiweiPames() {
        if (mCheneiYiweiView != null) {
            //出风口异味
            stringPames.put("beforeCfkyw", mCheneiYiweiView.mSeekbar1.getProgress() + "");
            stringPames.put("afterCfkyw", mCheneiYiweiView.mSeekbar2.getProgress() + "");
            //新车异味
            stringPames.put("beforeXcyw", mCheneiYiweiView.mSeekbar3.getProgress() + "");
            stringPames.put("afterXcyw", mCheneiYiweiView.mSeekbar4.getProgress() + "");
            //内饰皮革味
            stringPames.put("beforeNspgyw", mCheneiYiweiView.mSeekbar5.getProgress() + "");
            stringPames.put("afterNspgyw", mCheneiYiweiView.mSeekbar6.getProgress() + "");
            //内饰吸附异味
            stringPames.put("beforeNsxfyw", mCheneiYiweiView.mSeekbar7.getProgress() + "");
            stringPames.put("afterNsxfyw", mCheneiYiweiView.mSeekbar8.getProgress() + "");
            //随车物品异味
            stringPames.put("beforeScwpyw", mCheneiYiweiView.mSeekbar9.getProgress() + "");
            stringPames.put("afterScwpyw", mCheneiYiweiView.mSeekbar10.getProgress() + "");
            //二手烟异味
            stringPames.put("beforeEsyyw", mCheneiYiweiView.mSeekbar11.getProgress() + "");
            stringPames.put("afterEsyyw", mCheneiYiweiView.mSeekbar12.getProgress() + "");
            //细菌霉变异味
            stringPames.put("beforeXjmbyw", mCheneiYiweiView.mSeekbar13.getProgress() + "");
            stringPames.put("afterXjmbyw", mCheneiYiweiView.mSeekbar14.getProgress() + "");
            //其他异味
            stringPames.put("beforeQtyw", mCheneiYiweiView.mSeekbar15.getProgress() + "");
            stringPames.put("afterQtyw", mCheneiYiweiView.mSeekbar16.getProgress() + "");
        } else {
            //出风口异味
            stringPames.put("beforeCfkyw", "100");
            stringPames.put("afterCfkyw", "100");
            //新车异味
            stringPames.put("beforeXcyw", "100");
            stringPames.put("afterXcyw", "100");
            //内饰皮革味
            stringPames.put("beforeNspgyw", "100");
            stringPames.put("afterNspgyw", "100");
            //内饰吸附异味
            stringPames.put("beforeNsxfyw", "100");
            stringPames.put("afterNsxfyw", "100");
            //随车物品异味
            stringPames.put("beforeScwpyw", "100");
            stringPames.put("afterScwpyw", "100");
            //二手烟异味
            stringPames.put("beforeEsyyw", "100");
            stringPames.put("afterEsyyw", "100");
            //细菌霉变异味
            stringPames.put("beforeXjmbyw", "100");
            stringPames.put("afterXjmbyw", "100");
            //其他异味
            stringPames.put("beforeQtyw", "100");
            stringPames.put("afterQtyw", "100");
        }
    }

    private void getAllData() {
        Map<String, String> datas = getStringMap();
        if (!TextUtils.isEmpty(mDaoQueryBean.carNumber)) {          //说明已经存在草稿了
            mDaoUtil.upData(datas);
        } else {
            datas.put("_id", "100");
            mDaoUtil.insert(datas);
        }
    }

    @NonNull
    private Map<String, String> getStringMap() {
        Map<String, String> datas = new HashMap<>();
        if (mZhengjieView != null) {
            datas.put("carnumber", mCarNumberStr);
            //物品摆放
            datas.put("beforewpbfpicurl", mBeforeCnwpbfFile);
            datas.put("beforewpbfscore", mZhengjieView.mSeekbar1.getProgress() + "");
            datas.put("afterwpbfpicurl", mAfterCnwpbfFile);
            datas.put("afterwpbfscore", mZhengjieView.mSeekbar2.getProgress() + "");
            //空调滤芯
            datas.put("beforekqlxpicurl", mBeforeKtlxFile);
            datas.put("beforekqlxscore", mZhengjieView.mSeekbar3.getProgress() + "");
            datas.put("afterkqlxpicurl", mAfterKtlxFile);
            datas.put("afterkqlxscore", mZhengjieView.mSeekbar4.getProgress() + "");
            //食物残渣
            datas.put("beforeswczpicurl", mBeforeSwczFile);
            datas.put("beforeswczscore", mZhengjieView.mSeekbar5.getProgress() + "");
            datas.put("afterswczpicurl", mAfterSwczFile);
            datas.put("afterswczscore", mZhengjieView.mSeekbar6.getProgress() + "");
            //内饰洁净度
            datas.put("beforensjjdpicurl", mBeforeNsjjdFile);
            datas.put("beforensjjdscore", mZhengjieView.mSeekbar7.getProgress() + "");
            datas.put("afternsjjdpicurl", mAfterNsjjdFile);
            datas.put("afternsjjdscore", mZhengjieView.mSeekbar8.getProgress() + "");
            //粉尘污染
            datas.put("beforecnfcwrpicurl", mBeforeCnfcFile);
            datas.put("beforecnfcwrscore", mZhengjieView.mSeekbar9.getProgress() + "");
            datas.put("aftercnfcwrpicurl", mAfterCnfcFile);
            datas.put("aftercnfcwrscore", mZhengjieView.mSeekbar10.getProgress() + "");
            //杀菌消毒
            datas.put("beforexdsjpicurl", mBeforeSjxdFile);
            datas.put("beforexdsjscore", mZhengjieView.mSeekbar11.getProgress() + "");
            datas.put("afterxdsjpicurl", mAfterSjxdFile);
            datas.put("afterxdsjscore", mZhengjieView.mSeekbar12.getProgress() + "");
            //空气净化
            datas.put("beforekqjhpicurl", mBeforeKqjhFile);
            datas.put("beforekqjhscore", mZhengjieView.mSeekbar13.getProgress() + "");
            datas.put("afterkqjhpicurl", mAfterKqjhFile);
            datas.put("afterkqjhscore", mZhengjieView.mSeekbar14.getProgress() + "");
            //车内清洗
            datas.put("beforecnjxpicurl", mBeforeCnqxFile);
            datas.put("beforecnjxscore", mZhengjieView.mSeekbar15.getProgress() + "");
            datas.put("aftercnjxpicurl", mAfterCnqxFile);
            datas.put("aftercnjxscore", mZhengjieView.mSeekbar16.getProgress() + "");
            //空调滤芯表面污垢
            datas.put("beforelxbmwgpicurl", mBeforeKtlxbmwgFile);
            datas.put("beforelxbmwgscore", mKongtiaoView.mSeekbar1.getProgress() + "");
            datas.put("afterlxbmwgpicurl", mAfterKtlxbmwgFile);
            datas.put("afterlxbmwgscore", mKongtiaoView.mSeekbar2.getProgress() + "");
            //蒸发箱表面污垢
            datas.put("beforezfxbmwgpicurl", mBeforeZfxFile);
            datas.put("beforezfxbmwgscore", mKongtiaoView.mSeekbar3.getProgress() + "");
            datas.put("afterzfxbmwgpicurl", mAfterZfxFile);
            datas.put("afterzfxbmwgscore", mKongtiaoView.mSeekbar4.getProgress() + "");
            //鼓风机表面污垢
            datas.put("beforegfjbmwgpicurl", mBeforeGfjFile);
            datas.put("beforegfjbmwgscore", mKongtiaoView.mSeekbar5.getProgress() + "");
            datas.put("aftergfjbmwgpicurl", mAfterGfjFile);
            datas.put("aftergfjbmwgscore", mKongtiaoView.mSeekbar6.getProgress() + "");
            //通风管道表面污垢
            datas.put("beforetfgdbmwgpicurl", mBeforeTfgdFile);
            datas.put("beforetfgdbmwgscore", mKongtiaoView.mSeekbar7.getProgress() + "");
            datas.put("aftertfgdbmwgpicurl", mAfterTfgdFile);
            datas.put("aftertfgdbmwgscore", mKongtiaoView.mSeekbar8.getProgress() + "");
            //冷凝器表面污垢
            datas.put("beforelnqbmwgpicurl", mBeforeLnqFile);
            datas.put("beforelnqbmwgscore", mKongtiaoView.mSeekbar9.getProgress() + "");
            datas.put("afterlnqbmwgpicurl", mAfterLnqFile);
            datas.put("afterlnqbmwgscore", mKongtiaoView.mSeekbar10.getProgress() + "");
            //空调制冷新能
            datas.put("beforektxtzlxnpicurl", mBeforeKtzlFile);
            datas.put("beforektxtzlxnscore", mKongtiaoView.mSeekbar11.getProgress() + "");
            datas.put("afterktxtzlxnpicurl", mAfterKtzlFile);
            datas.put("afterktxtzlxnscore", mKongtiaoView.mSeekbar12.getProgress() + "");
            //开启空调时异向
            datas.put("beforektkqyxpicurl", mBeforeKtyxFile);
            datas.put("beforektkqyxscore", mKongtiaoView.mSeekbar17.getProgress() + "");
            datas.put("afterktkqyxpicurl", mAfterKtyxFile);
            datas.put("afterktkqyxscore", mKongtiaoView.mSeekbar18.getProgress() + "");
            if (mCheneiYiweiView != null) {
                //出风口异味
                datas.put("beforecfkyw", mCheneiYiweiView.mSeekbar1.getProgress() + "");
                datas.put("aftercfkyw", mCheneiYiweiView.mSeekbar2.getProgress() + "");
                //新车异味
                datas.put("beforexcyw", mCheneiYiweiView.mSeekbar3.getProgress() + "");
                datas.put("afterxcyw", mCheneiYiweiView.mSeekbar4.getProgress() + "");
                //内饰皮革味
                datas.put("beforenspgyw", mCheneiYiweiView.mSeekbar5.getProgress() + "");
                datas.put("afternspgyw", mCheneiYiweiView.mSeekbar6.getProgress() + "");
                //内饰吸附异味
                datas.put("beforensxfyw", mCheneiYiweiView.mSeekbar7.getProgress() + "");
                datas.put("afternsxfyw", mCheneiYiweiView.mSeekbar8.getProgress() + "");
                //随车物品异味
                datas.put("beforescwpyw", mCheneiYiweiView.mSeekbar9.getProgress() + "");
                datas.put("afterscwpyw", mCheneiYiweiView.mSeekbar10.getProgress() + "");
                //二手烟异味
                datas.put("beforeesyyw", mCheneiYiweiView.mSeekbar11.getProgress() + "");
                datas.put("afteresyyw", mCheneiYiweiView.mSeekbar12.getProgress() + "");
                //细菌霉变异味
                datas.put("beforexjmbyw", mCheneiYiweiView.mSeekbar13.getProgress() + "");
                datas.put("afterxjmbyw", mCheneiYiweiView.mSeekbar14.getProgress() + "");
                //其他异味
                datas.put("beforeqtyw", mCheneiYiweiView.mSeekbar15.getProgress() + "");
                datas.put("afterqtyw", mCheneiYiweiView.mSeekbar16.getProgress() + "");
            }

        }
        return datas;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mProgressBar.setVisibility(View.VISIBLE);
            //通知系统扫描文件
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mTempUri));
            switch (requestCode) {
                case 10:            //车内物品摆放施工前
                    Uri beforeCnwpbfUri;
                    if (isCanmera) {
                        beforeCnwpbfUri = mTempUri;
                    } else {
                        beforeCnwpbfUri = data.getData();
                    }
                    File file = setImageAndgetBitmapFile(beforeCnwpbfUri);
                    //压缩图片并上传
                    compressImg(file, "beforeWpbfPicUrl", mZhengjieView.mBeforeCnwupf, 1);
                    break;
                case 11:
                    Uri afterCnwpbfUri;
                    if (isCanmera) {
                        afterCnwpbfUri = mTempUri;
                    } else {
                        afterCnwpbfUri = data.getData();
                    }
                    File file1 = setImageAndgetBitmapFile(afterCnwpbfUri);
                    //压缩图片并上传
                    compressImg(file1, "afterWpbfPicUrl", mZhengjieView.mAfterCnwupf, 2);
                    break;
                case 12:                    //空调滤芯前
                    Uri beforeKtlxUri;
                    if (isCanmera) {
                        beforeKtlxUri = mTempUri;
                    } else {
                        beforeKtlxUri = data.getData();
                    }
                    File file2 = setImageAndgetBitmapFile(beforeKtlxUri);
                    compressImg(file2, "beforeKqlxPicUrl", mZhengjieView.mBeforeKtlx, 3);
                    break;
                case 13:
                    Uri afterKtlxUri;
                    if (isCanmera) {
                        afterKtlxUri = mTempUri;
                    } else {
                        afterKtlxUri = data.getData();
                    }
                    File file3 = setImageAndgetBitmapFile(afterKtlxUri);
                    mAfterKtlxFile = file3.getAbsolutePath();
                    compressImg(file3, "afterKqlxPicUrl", mZhengjieView.mAfterKtlx, 4);
                    break;
                case 14:                        //食物残渣前
                    Uri beforeSwczUri;
                    if (isCanmera) {
                        beforeSwczUri = mTempUri;
                    } else {
                        beforeSwczUri = data.getData();
                    }
                    File file4 = setImageAndgetBitmapFile(beforeSwczUri);
                    compressImg(file4, "beforeSwczPicUrl", mZhengjieView.mBeforeSwcz, 5);
                    break;
                case 15:
                    Uri afterSwczUri;
                    if (isCanmera) {
                        afterSwczUri = mTempUri;
                    } else {
                        afterSwczUri = data.getData();
                    }
                    File file5 = setImageAndgetBitmapFile(afterSwczUri);
                    compressImg(file5, "afterSwczPicUrl", mZhengjieView.mAfterSwcz, 6);
                    break;
                case 16:                            //内饰洁净度
                    Uri beforeNsjjdUri;
                    if (isCanmera) {
                        beforeNsjjdUri = mTempUri;
                    } else {
                        beforeNsjjdUri = data.getData();
                    }
                    File file6 = setImageAndgetBitmapFile(beforeNsjjdUri);
                    compressImg(file6, "beforeNsjjdPicUrl", mZhengjieView.mBefoewNsjjd, 7);
                    break;
                case 17:
                    Uri afterNsjjdUri;
                    if (isCanmera) {
                        afterNsjjdUri = mTempUri;
                    } else {
                        afterNsjjdUri = data.getData();
                    }
                    File file7 = setImageAndgetBitmapFile(afterNsjjdUri);
                    compressImg(file7, "afterNsjjdPicUrl", mZhengjieView.mAfterNsjjd, 8);
                    break;
                case 18:                                //车内粉尘污染施工前
                    Uri beforeCnfcUri;
                    if (isCanmera) {
                        beforeCnfcUri = mTempUri;
                    } else {
                        beforeCnfcUri = data.getData();
                    }
                    File file8 = setImageAndgetBitmapFile(beforeCnfcUri);
                    compressImg(file8, "beforeCnfcwrPicUrl", mZhengjieView.mBeforeCnfc, 9);
                    break;
                case 19:
                    Uri afterCnfcUri;
                    if (isCanmera) {
                        afterCnfcUri = mTempUri;
                    } else {
                        afterCnfcUri = data.getData();
                    }
                    File file9 = setImageAndgetBitmapFile(afterCnfcUri);
                    compressImg(file9, "afterCnfcwrPicUrl", mZhengjieView.mAfterCnfc, 10);
                    break;
                case 20:                            //定期车内杀菌消毒
                    Uri beforerSjxdUri;
                    if (isCanmera) {
                        beforerSjxdUri = mTempUri;
                    } else {
                        beforerSjxdUri = data.getData();
                    }
                    File file10 = setImageAndgetBitmapFile(beforerSjxdUri);
                    compressImg(file10, "beforeXdsjPicUrl", mZhengjieView.mBeforeSjxd, 11);
                    break;
                case 21:
                    Uri afterSjxdUri;
                    if (isCanmera) {
                        afterSjxdUri = mTempUri;
                    } else {
                        afterSjxdUri = data.getData();
                    }
                    File file11 = setImageAndgetBitmapFile(afterSjxdUri);
                    compressImg(file11, "afterXdsjPicUrl", mZhengjieView.mAfterSjxd, 12);
                    break;
                case 22:                            //定期车内空气净化
                    Uri beforerKqjhUri;
                    if (isCanmera) {
                        beforerKqjhUri = mTempUri;
                    } else {
                        beforerKqjhUri = data.getData();
                    }
                    File file12 = setImageAndgetBitmapFile(beforerKqjhUri);
                    compressImg(file12, "beforeKqjhPicUrl", mZhengjieView.mBeforeKqjh, 13);
                    break;
                case 23:
                    Uri afterKqjhUri;
                    if (isCanmera) {
                        afterKqjhUri = mTempUri;
                    } else {
                        afterKqjhUri = data.getData();
                    }
                    File file13 = setImageAndgetBitmapFile(afterKqjhUri);
                    compressImg(file13, "afterKqjhPicUrl", mZhengjieView.mAfterKqjh, 14);
                    break;
                case 24:                            //定期车内空气清洗
                    Uri beforerCnqxUri;
                    if (isCanmera) {
                        beforerCnqxUri = mTempUri;
                    } else {
                        beforerCnqxUri = data.getData();
                    }
                    File file14 = setImageAndgetBitmapFile(beforerCnqxUri);
                    compressImg(file14, "beforeCnjxPicUrl", mZhengjieView.mBeforeCnqx, 15);
                    break;
                case 25:
                    Uri afterCnqxUri;
                    if (isCanmera) {
                        afterCnqxUri = mTempUri;
                    } else {
                        afterCnqxUri = data.getData();
                    }
                    File file15 = setImageAndgetBitmapFile(afterCnqxUri);
                    compressImg(file15, "afterCnjxPicUrl", mZhengjieView.mAfterCnqx, 16);
                    break;
                case 26:                    //空调滤芯
                    Uri beforeKtlxbmwgUri;
                    if (isCanmera) {
                        beforeKtlxbmwgUri = mTempUri;
                    } else {
                        beforeKtlxbmwgUri = data.getData();
                    }
                    File file16 = setImageAndgetBitmapFile(beforeKtlxbmwgUri);
                    compressImg(file16, "beforeLxbmwgPicUrl", mKongtiaoView.mBeforeKtlx, 17);
                    break;
                case 27:
                    Uri afterKtlxbmwgUri;
                    if (isCanmera) {
                        afterKtlxbmwgUri = mTempUri;
                    } else {
                        afterKtlxbmwgUri = data.getData();
                    }
                    File file17 = setImageAndgetBitmapFile(afterKtlxbmwgUri);
                    compressImg(file17, "afterLxbmwgPicUrl", mKongtiaoView.mAfterKtlx, 18);
                    break;
                case 28:                 //蒸发箱
                    Uri beforeZfxUri;
                    if (isCanmera) {
                        beforeZfxUri = mTempUri;
                    } else {
                        beforeZfxUri = data.getData();
                    }
                    File file18 = setImageAndgetBitmapFile(beforeZfxUri);
                    compressImg(file18, "beforeZfxbmwgPicUrl", mKongtiaoView.mBeforeZfx, 19);
                    break;
                case 29:
                    Uri afterZfxUri;
                    if (isCanmera) {
                        afterZfxUri = mTempUri;
                    } else {
                        afterZfxUri = data.getData();
                    }
                    File file19 = setImageAndgetBitmapFile(afterZfxUri);
                    compressImg(file19, "afterZfxbmwgPicUrl", mKongtiaoView.mAfterZfx, 20);
                    break;
                case 30:                            //鼓风机
                    Uri beforeGfjUri;
                    if (isCanmera) {
                        beforeGfjUri = mTempUri;
                    } else {
                        beforeGfjUri = data.getData();
                    }
                    File file20 = setImageAndgetBitmapFile(beforeGfjUri);
                    compressImg(file20, "beforeGfjbmwgPicUrl", mKongtiaoView.mBeforeGfj, 21);
                    break;
                case 31:
                    Uri afterGfjUri;
                    if (isCanmera) {
                        afterGfjUri = mTempUri;
                    } else {
                        afterGfjUri = data.getData();
                    }
                    File file21 = setImageAndgetBitmapFile(afterGfjUri);
                    compressImg(file21, "afterGfjbmwgPicUrl", mKongtiaoView.mAfterGfj, 22);
                    break;
                case 32:                        //通风管道表面污垢
                    Uri beforeTfgdUri;
                    if (isCanmera) {
                        beforeTfgdUri = mTempUri;
                    } else {
                        beforeTfgdUri = data.getData();
                    }
                    File file22 = setImageAndgetBitmapFile(beforeTfgdUri);
                    compressImg(file22, "beforeTfgdbmwgPicUrl", mKongtiaoView.mBeforeTfgd, 23);
                    break;
                case 33:
                    Uri afterTfgdUri;
                    if (isCanmera) {
                        afterTfgdUri = mTempUri;
                    } else {
                        afterTfgdUri = data.getData();
                    }
                    File file23 = setImageAndgetBitmapFile(afterTfgdUri);
                    compressImg(file23, "afterTfgdbmwgPicUrl", mKongtiaoView.mAfterTfgd, 24);
                    break;
                case 34:                        //冷凝器表面污垢
                    Uri beforeLnqUri;
                    if (isCanmera) {
                        beforeLnqUri = mTempUri;
                    } else {
                        beforeLnqUri = data.getData();
                    }
                    File file24 = setImageAndgetBitmapFile(beforeLnqUri);
                    compressImg(file24, "beforeLnqbmwgPicUrl", mKongtiaoView.mBefoewLnq, 25);
                    break;
                case 35:
                    Uri afterLnqUri;
                    if (isCanmera) {
                        afterLnqUri = mTempUri;
                    } else {
                        afterLnqUri = data.getData();
                    }
                    File file25 = setImageAndgetBitmapFile(afterLnqUri);
                    compressImg(file25, "afterLnqbmwgPicUrl", mKongtiaoView.mAfterLnq, 26);
                    break;
                case 36:                        //空调制冷性能
                    Uri beforeKtzlUri;
                    if (isCanmera) {
                        beforeKtzlUri = mTempUri;
                    } else {
                        beforeKtzlUri = data.getData();
                    }
                    File file26 = setImageAndgetBitmapFile(beforeKtzlUri);
                    compressImg(file26, "beforeKtxtzlxnPicUrl", mKongtiaoView.mBeforeKtzl, 27);
                    break;
                case 37:
                    Uri afterKtzlUri;
                    if (isCanmera) {
                        afterKtzlUri = mTempUri;
                    } else {
                        afterKtzlUri = data.getData();
                    }
                    File file27 = setImageAndgetBitmapFile(afterKtzlUri);
                    compressImg(file27, "afterKtxtzlxnPicUrl", mKongtiaoView.mAfterKtzl, 28);
                    break;
                case 38:                        //空调异响
                    Uri beforeKtyxUri;
                    if (isCanmera) {
                        beforeKtyxUri = mTempUri;
                    } else {
                        beforeKtyxUri = data.getData();
                    }
                    File file28 = setImageAndgetBitmapFile(beforeKtyxUri);
                    compressImg(file28, "beforeKtkqyxPicUrl", mKongtiaoView.mBeforeKtyx, 29);
                    break;
                case 39:
                    Uri afterKtyxUri;
                    if (isCanmera) {
                        afterKtyxUri = mTempUri;
                    } else {
                        afterKtyxUri = data.getData();
                    }
                    File file29 = setImageAndgetBitmapFile(afterKtyxUri);
                    compressImg(file29, "afterKtkqyxPicUrl", mKongtiaoView.mAfterKtyx, 30);
                    break;

            }
            //通知系统扫描文件
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mTempUri));
        }
    }

    private void compressImg(File file, final String beforeWpbfPicUrl, final ImageView beforeCnwupf, final int i) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_CAMERA);
        } else {
            Luban.with(this)
                    .load(file)
                    .ignoreBy(100)//不进行压缩的阈值单位k
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        }

                        @Override
                        public void onSuccess(File file) {
                            uploadImage(file, beforeWpbfPicUrl, beforeCnwupf, i);
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过程出现问题时调用
                            ToastUtil.showMessage("无权限");
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }).launch();
        }

    }

    private void uploadImage(final File file, final String pames, final ImageView beforeCnwupf, final int i) {
        OkhttpManager manager = OkhttpManager.getOkhttpManager();
        manager.doFile(Constants.URLS.UPLOAD_IMAGE, file, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showMessage("网络异常");
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int errno = jsonObject.getInt("errno");
                    if (errno == 0) {
                        String data = jsonObject.getString("data");
                        switch (i) {
                            case 1:
                                mBeforeCnwpbfFile = data;
                                break;
                            case 2:
                                mAfterCnwpbfFile = data;
                                break;
                            case 3:
                                mBeforeKtlxFile = data;
                                break;
                            case 4:
                                mAfterKtlxFile = data;
                                break;
                            case 5:
                                mBeforeSwczFile = data;
                                break;
                            case 6:
                                mAfterSwczFile = data;
                                break;
                            case 7:
                                mBeforeNsjjdFile = data;
                                break;
                            case 8:
                                mAfterNsjjdFile = data;
                                break;
                            case 9:
                                mBeforeCnfcFile = data;
                                break;
                            case 10:
                                mAfterCnfcFile = data;
                                break;
                            case 11:
                                mBeforeSjxdFile = data;
                                break;
                            case 12:
                                mAfterSjxdFile = data;
                                break;
                            case 13:
                                mBeforeKqjhFile = data;
                                break;
                            case 14:
                                mAfterKqjhFile = data;
                                break;
                            case 15:
                                mBeforeCnqxFile = data;
                                break;
                            case 16:
                                mAfterCnqxFile = data;
                                break;
                            case 17:
                                mBeforeKtlxbmwgFile = data;
                                break;
                            case 18:
                                mAfterKtlxbmwgFile = data;
                                break;
                            case 19:
                                mBeforeZfxFile = data;
                                break;
                            case 20:
                                mAfterZfxFile = data;
                                break;
                            case 21:
                                mBeforeGfjFile = data;
                                break;
                            case 22:
                                mAfterGfjFile = data;
                                break;
                            case 23:
                                mBeforeTfgdFile = data;
                                break;
                            case 24:
                                mAfterTfgdFile = data;
                                break;
                            case 25:
                                mBeforeLnqFile = data;
                                break;
                            case 26:
                                mAfterLnqFile = data;
                                break;
                            case 27:
                                mBeforeKtzlFile = data;
                                break;
                            case 28:
                                mAfterKtzlFile = data;
                                break;
                            case 29:
                                mBeforeKtyxFile = data;
                                break;
                            case 30:
                                mAfterKtyxFile = data;
                                break;
                        }
                        stringPames.put(pames, data);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.GONE);
                                setImage(file, beforeCnwupf);
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private File setImageAndgetBitmapFile(Uri uri) {
        String filePathByUri = FileUtil.getFilePathByUri(this, uri);
        if (TextUtils.isEmpty(filePathByUri)){
            return mFile;
        }else {
            return new File(filePathByUri);
        }
    }

    private void setImage(File file, ImageView beforeCnwupf) {
        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        beforeCnwupf.setImageBitmap(bmp);
    }

    @Override
    public void onSuccess(DaoQueryBean bean) {
        mDaoQueryBean = bean;
        if (mIsCreate) {
            getAllData();
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setData(mDaoQueryBean);
                }
            });
            //车内整洁卫生
            mBeforeCnwpbfFile = mDaoQueryBean.beforeWpbfPicUrl;
            if (!TextUtils.isEmpty(mBeforeCnwpbfFile))
                stringPames.put("beforeWpbfPicUrl", mBeforeCnwpbfFile);
            mAfterCnwpbfFile = mDaoQueryBean.afterWpbfPicUrl;
            if (!TextUtils.isEmpty(mAfterCnwpbfFile))
                stringPames.put("afterWpbfPicUrl", mAfterCnwpbfFile);
            mBeforeKtlxFile = mDaoQueryBean.beforeKqlxPicUrl;
            if (!TextUtils.isEmpty(mBeforeKtlxFile))
                stringPames.put("beforeKqlxPicUrl", mBeforeKtlxFile);
            mAfterKtlxFile = mDaoQueryBean.afterKqlxPicUrl;
            if (!TextUtils.isEmpty(mAfterKtlxFile))
                stringPames.put("afterKqlxPicUrl", mAfterKtlxFile);
            mBeforeSwczFile = mDaoQueryBean.beforeSwczPicUrl;
            if (!TextUtils.isEmpty(mBeforeSwczFile))
                stringPames.put("beforeSwczPicUrl", mBeforeSwczFile);
            mAfterSwczFile = mDaoQueryBean.afterSwczPicUrl;
            if (!TextUtils.isEmpty(mAfterSwczFile))
                stringPames.put("afterSwczPicUrl", mAfterSwczFile);
            mBeforeNsjjdFile = mDaoQueryBean.beforeNsjjdPicUrl;
            if (!TextUtils.isEmpty(mBeforeNsjjdFile))
                stringPames.put("beforeNsjjdPicUrl", mBeforeNsjjdFile);
            mAfterNsjjdFile = mDaoQueryBean.afterNsjjdPicUrl;
            if (!TextUtils.isEmpty(mAfterNsjjdFile))
                stringPames.put("afterNsjjdPicUrl", mAfterNsjjdFile);
            mBeforeCnfcFile = mDaoQueryBean.beforeCnfcwrPicUrl;
            if (!TextUtils.isEmpty(mBeforeCnfcFile))
                stringPames.put("beforeCnfcwrPicUrl", mBeforeCnfcFile);
            mAfterCnfcFile = mDaoQueryBean.afterCnfcwrPicUrl;
            if (!TextUtils.isEmpty(mAfterCnfcFile))
                stringPames.put("afterCnfcwrPicUrl", mAfterCnfcFile);
            mBeforeSjxdFile = mDaoQueryBean.beforeXdsjPicUrl;
            if (!TextUtils.isEmpty(mBeforeSjxdFile))
                stringPames.put("beforeXdsjPicUrl", mBeforeSjxdFile);
            mAfterSjxdFile = mDaoQueryBean.afterXdsjPicUrl;
            if (!TextUtils.isEmpty(mAfterSjxdFile))
                stringPames.put("afterXdsjPicUrl", mAfterSjxdFile);
            mBeforeKqjhFile = mDaoQueryBean.beforeKqjhPicUrl;
            if (!TextUtils.isEmpty(mBeforeKqjhFile))
                stringPames.put("beforeKqjhPicUrl", mBeforeKqjhFile);
            mAfterKqjhFile = mDaoQueryBean.afterKqjhPicUrl;
            if (!TextUtils.isEmpty(mAfterKqjhFile))
                stringPames.put("afterKqjhPicUrl", mAfterKqjhFile);
            mBeforeCnqxFile = mDaoQueryBean.beforeCnjxPicUrl;
            if (!TextUtils.isEmpty(mBeforeCnqxFile))
                stringPames.put("beforeCnjxPicUrl", mBeforeCnqxFile);
            mAfterCnqxFile = mDaoQueryBean.afterCnjxPicUrl;
            if (!TextUtils.isEmpty(mAfterCnqxFile))
                stringPames.put("afterCnjxPicUrl", mAfterCnqxFile);
            //空调系统检测
            mBeforeKtlxbmwgFile = mDaoQueryBean.beforeLxbmwgPicUrl;
            if (!TextUtils.isEmpty(mBeforeKtlxbmwgFile))
                stringPames.put("beforeLxbmwgPicUrl", mBeforeKtlxbmwgFile);
            mAfterKtlxbmwgFile = mDaoQueryBean.afterLxbmwgPicUrl;
            if (!TextUtils.isEmpty(mAfterKtlxbmwgFile))
                stringPames.put("afterLxbmwgPicUrl", mAfterKtlxbmwgFile);
            mBeforeZfxFile = mDaoQueryBean.beforeZfxbmwgPicUrl;
            if (!TextUtils.isEmpty(mBeforeZfxFile))
                stringPames.put("beforeZfxbmwgPicUrl", mBeforeZfxFile);
            mAfterZfxFile = mDaoQueryBean.afterZfxbmwgPicUrl;
            if (!TextUtils.isEmpty(mAfterZfxFile))
                stringPames.put("afterZfxbmwgPicUrl", mAfterZfxFile);
            mBeforeGfjFile = mDaoQueryBean.beforeGfjbmwgPicUrl;
            if (!TextUtils.isEmpty(mBeforeGfjFile))
                stringPames.put("beforeGfjbmwgPicUrl", mBeforeGfjFile);
            mAfterGfjFile = mDaoQueryBean.afterGfjbmwgPicUrl;
            if (!TextUtils.isEmpty(mBeforeGfjFile))
                stringPames.put("afterGfjbmwgPicUrl", mBeforeGfjFile);
            mBeforeTfgdFile = mDaoQueryBean.beforeTfgdbmwgPicUrl;
            if (!TextUtils.isEmpty(mBeforeTfgdFile))
                stringPames.put("beforeTfgdbmwgPicUrl", mBeforeTfgdFile);
            mAfterTfgdFile = mDaoQueryBean.afterTfgdbmwgPicUrl;
            if (!TextUtils.isEmpty(mAfterTfgdFile))
                stringPames.put("afterTfgdbmwgPicUrl", mAfterTfgdFile);
            mBeforeLnqFile = mDaoQueryBean.beforeLnqbmwgPicUrl;
            if (!TextUtils.isEmpty(mBeforeLnqFile))
                stringPames.put("beforeLnqbmwgPicUrl", mBeforeLnqFile);
            mAfterLnqFile = mDaoQueryBean.afterLnqbmwgPicUrl;
            if (!TextUtils.isEmpty(mAfterLnqFile))
                stringPames.put("afterLnqbmwgPicUrl", mAfterLnqFile);
            mBeforeKtzlFile = mDaoQueryBean.beforeKtxtzlxnPicUrl;
            if (!TextUtils.isEmpty(mBeforeKtzlFile))
                stringPames.put("beforeKtxtzlxnPicUrl", mBeforeKtzlFile);
            mAfterKtzlFile = mDaoQueryBean.afterKtxtzlxnPicUrl;
            if (!TextUtils.isEmpty(mAfterKtzlFile))
                stringPames.put("afterKtxtzlxnPicUrl", mAfterKtzlFile);
            mBeforeKtyxFile = mDaoQueryBean.beforeKtkqyxPicUrl;
            if (!TextUtils.isEmpty(mBeforeKtyxFile))
                stringPames.put("beforeKtkqyxPicUrl", mBeforeKtyxFile);
            mAfterKtyxFile = mDaoQueryBean.afterKtkqyxPicUrl;
            if (!TextUtils.isEmpty(mAfterKtyxFile))
                stringPames.put("afterKtkqyxPicUrl", mAfterKtyxFile);
            String carNumber = mDaoQueryBean.carNumber;
            if (!TextUtils.isEmpty(carNumber)) {
                mCarNumberStr = carNumber;
                mCarNumber.setText(mCarNumberStr);
            }
        }
    }

    @Override
    public void onUpdataSuccess(final int result) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //更新成功
                if (result != 0) {
                    ToastUtil.showMessage("保存成功");
                    mProgressBar.setVisibility(View.GONE);
                    finish();
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.showMessage("保存失败");
                }
            }
        });
    }

    @Override
    public void onDeleteSuccess(final int result) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //删除成功
                if (result > 0) {
                    ToastUtil.showMessage("删除成功");
                    mProgressBar.setVisibility(View.GONE);
                    finish();
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.showMessage("删除失败");
                }
            }
        });
    }

    @Override
    public void onInsertSuccess(final int result) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //加入成功
                if (result > 0) {
                    mProgressBar.setVisibility(View.GONE);
                    finish();
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.showMessage("保存失败");
                }

            }
        });
    }

    @Override
    public void onViewCreated(ZhengjieView view) {
        mZhengjieView = view;
        mZhengjieView.mBeforeCnwupf.setOnClickListener(this);
        mZhengjieView.mAfterCnwupf.setOnClickListener(this);
        //空调滤芯
        mZhengjieView.mBeforeKtlx.setOnClickListener(this);
        mZhengjieView.mAfterKtlx.setOnClickListener(this);
        //车内食物残渣
        mZhengjieView.mBeforeSwcz.setOnClickListener(this);
        mZhengjieView.mAfterSwcz.setOnClickListener(this);
        //内饰洁净度
        mZhengjieView.mBefoewNsjjd.setOnClickListener(this);
        mZhengjieView.mAfterNsjjd.setOnClickListener(this);
        //车内粉尘
        mZhengjieView.mBeforeCnfc.setOnClickListener(this);
        mZhengjieView.mAfterCnfc.setOnClickListener(this);
        //定期杀菌消毒
        mZhengjieView.mBeforeSjxd.setOnClickListener(this);
        mZhengjieView.mAfterSjxd.setOnClickListener(this);
        //车内空气净化
        mZhengjieView.mBeforeKqjh.setOnClickListener(this);
        mZhengjieView.mAfterKqjh.setOnClickListener(this);
        //车内清洗
        mZhengjieView.mBeforeCnqx.setOnClickListener(this);
        mZhengjieView.mAfterCnqx.setOnClickListener(this);
    }

    @Override
    public void onWpbfViewCreated(KongtiaoView view) {
        //空调系统检测显示的view创建成功
        mKongtiaoView = view;
        //空调滤芯表面污垢
        mKongtiaoView.mBeforeKtlx.setOnClickListener(this);
        mKongtiaoView.mAfterKtlx.setOnClickListener(this);
        //蒸发箱表面污垢
        mKongtiaoView.mBeforeZfx.setOnClickListener(this);
        mKongtiaoView.mAfterZfx.setOnClickListener(this);
        //鼓风机
        mKongtiaoView.mBeforeGfj.setOnClickListener(this);
        mKongtiaoView.mAfterGfj.setOnClickListener(this);
        //通风管道表面污垢
        mKongtiaoView.mBeforeTfgd.setOnClickListener(this);
        mKongtiaoView.mAfterTfgd.setOnClickListener(this);
        //冷凝器表面污垢
        mKongtiaoView.mBefoewLnq.setOnClickListener(this);
        mKongtiaoView.mAfterLnq.setOnClickListener(this);
        //空调制冷
        mKongtiaoView.mBeforeKtzl.setOnClickListener(this);
        mKongtiaoView.mAfterKtzl.setOnClickListener(this);
        //空调异响
        mKongtiaoView.mBeforeKtyx.setOnClickListener(this);
        mKongtiaoView.mAfterKtyx.setOnClickListener(this);

    }

    @Override
    public void onCheneiYiweiCreated(CheneiYiweiView view) {
        mCheneiYiweiView = view;
    }

    @Override
    public void onFaild(String msg) {
        ToastUtil.showMessage(msg);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String result) {
        ToastUtil.showMessage(result);
        mProgressBar.setVisibility(View.GONE);
        //上传报告成功之后删除所有的草稿记录
        if (mDaoUtil != null)
            mDaoUtil.delete();
        finish();
    }

    @Override
    public void onNoData(String msg) {
    }

    @Override
    public void onSelectAlbum() {
        isCanmera = false;
        //选择了相册
        startAc(mSelectCurrentImgState);
    }

    private void startAc(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);
    }

    private void startCamera(int requestCode) {
        isCanmera = true;
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        //创建临时Uri用于存储临时图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTempUri = FileProvider.getUriForFile(this, "com.cad.fileprovider", mFile);
        } else {
            mTempUri = Uri.fromFile(mFile);
        }
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTempUri);
        startActivityForResult(openCameraIntent, requestCode);
    }

    @Override
    public void onSelectCamera() {
        boolean b = CheckCameraUtils.hasCamera();
        if (b) {
            //选择了相机,去检测相机权限
            requestCameraPermission();
        }else {
            ToastUtil.showMessage("没有检测到摄像头");
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, TAKE_PHOTO_REQUEST_CODE);
        } else {
            startCamera(mSelectCurrentImgState);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 获取到相机权限
                startCamera(mSelectCurrentImgState);
            } else {
                ToastUtil.showMessage("上传照片需要相机权限");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDaoQueryBean = null;
    }
}
