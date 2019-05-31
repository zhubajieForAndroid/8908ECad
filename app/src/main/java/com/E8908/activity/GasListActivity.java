package com.E8908.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.E8908.R;
import com.E8908.adapter.GasListAdapter;
import com.E8908.base.BaseToolBarActivity;
import com.E8908.base.QuerySuccess;
import com.E8908.bean.CreateTabListBean;
import com.E8908.conf.Constants;
import com.E8908.db.DaoQueryBean;
import com.E8908.db.DaoUtil;
import com.E8908.db.GasDataBaseDao;
import com.E8908.impl.GasListPersenterImpl;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.view.BaseView;
import com.E8908.widget.InputCarNumberDialog;
import com.E8908.widget.SpaceItemDecoration;
import com.E8908.widget.ToastUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * aidl方式调用扫描车牌
 */

public class GasListActivity extends BaseToolBarActivity implements View.OnClickListener, BaseView<CreateTabListBean>, GasListAdapter.OnLookCurveBtnListener, QuerySuccess {

    private static final String TAG = "GasListActivity";
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.back)
    Button mBack;
    @Bind(R.id.progress_bar)
    LinearLayout mProgressBar;
    @Bind(R.id.create_tab)
    ImageView mCreateTab;
    @Bind(R.id.edit_btn)
    Button mEditBtn;
    private String mEquipmentId;
    private List<CreateTabListBean.ResponseBean.RowsBean> mRowsBeans;
    private GasListAdapter mAdapter;
    private Intent mIntent;
    private boolean mIsloadmore = false;        //是否有加载更多
    private int pageNumber = 1;         //页数
    private GasListPersenterImpl mCreateTabPersenter;
    private int mCurrentState = 0;
    private String mCarNumber;
    private InputCarNumberDialog mNumberDialog;
    private Handler mHandler;
    //private OcrplateidResult mOcrplateidResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_list);
        ButterKnife.bind(this);
        mEquipmentId = getIntent().getStringExtra("equipmentId");
        mHandler = new Handler();
        Intent intent = new Intent();
        intent.setAction("com.cad.service.QueryResultService");
        Intent explicitFromImplicitIntent = createExplicitFromImplicitIntent(this, intent);
        if (explicitFromImplicitIntent != null) {
            final Intent eintent = new Intent(explicitFromImplicitIntent);
            bindService(eintent, mServiceC, Service.BIND_AUTO_CREATE);
        }
        initListener();
    }

    @Override
    public int getToolbarImage() {
        return R.mipmap.yhqt_nav;
    }

    @Override
    protected void equipmentData(byte[] buffer) {
    }

    @Override
    protected void setResultData(byte[] buffer) {
        Boolean isSuccess = DataUtil.analysisSetResult(buffer);
        if (mCurrentState == 1) {
            if (isSuccess) {
                mCurrentState = 2;
                SendUtil.setConstructionAfterAndBefore(0);
            } else {
                SendUtil.setCarNumber(mCarNumber);
            }
        }
        if (mCurrentState == 2){
            if (isSuccess){
                mCurrentState = 0;
                mIntent = new Intent(this, EditTabActivity.class);
                mIntent.putExtra("isCreate", true);
                mIntent.putExtra("carNumber", mCarNumber);
                mIntent.putExtra("equipmentId", mEquipmentId);
                startActivity(mIntent);
            }else {
                mCurrentState = 2;
                SendUtil.setConstructionAfterAndBefore(0);
            }
        }

    }

    private void initListener() {
        mRowsBeans = new ArrayList<>();
        mBack.setOnClickListener(this);
        mCreateTab.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);
        mCreateTabPersenter = new GasListPersenterImpl(this);
        mCreateTabPersenter.loadData(Constants.URLS.GET_ALL_CHICK_INFO, mEquipmentId, pageNumber + "");

        mAdapter = new GasListAdapter(this, mRowsBeans);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        //添加Android自带的分割线
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0, 20));

        mAdapter.setOnLookCurveBtnListener(this);


        mRecyclerView.addOnScrollListener(mOnScrollListener);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.create_tab:
                mNumberDialog = new InputCarNumberDialog(GasListActivity.this, R.style.dialog);
                if (!mNumberDialog.isShowing()) {
                    mNumberDialog.setBitmap();
                    mNumberDialog.show();
                }
                mNumberDialog.setOnMakeSureBtnClickListener(new InputCarNumberDialog.OnMakeSureBtnClickListener() {
                    @Override
                    public void onBtnClick(String carNumber) {
                        mCarNumber = carNumber;
                        mCurrentState = 1;
                        SendUtil.setCarNumber(carNumber);
                    }
                });
                mNumberDialog.setOnStartActivityListener(new InputCarNumberDialog.OnStartActivityListener() {
                    @Override
                    public void onStartActiity() {
                       /* try {
                            if (mOcrplateidResult != null)
                                mOcrplateidResult.startCamera();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }*/
                    }
                });
                break;
            case R.id.edit_btn:     //编辑草稿
                mIntent = new Intent(GasListActivity.this, EditTabActivity.class);
                mIntent.putExtra("isCreate", false);
                mIntent.putExtra("carNumber", mCarNumber);
                mIntent.putExtra("equipmentId", mEquipmentId);
                startActivity(mIntent);
                break;
        }
    }




    @Override
    public void onFaild(String msg) {
        ToastUtil.showMessage("网络异常");
        mProgressBar.setVisibility(View.GONE);
        mAdapter.setCurrentLoadingState(mAdapter.LOAD_MORE_STATE_NOMORE);
    }

    @Override
    public void onSuccess(CreateTabListBean result) {
        int code = result.getCode();
        if (code == 0) {
            CreateTabListBean.ResponseBean response = result.getResponse();
            int total = response.getTotal();
            mRowsBeans.addAll(response.getRows());
            mAdapter.notifyDataSetChanged();
            if (total == mRowsBeans.size()) {
                mIsloadmore = false;
                mAdapter.setCurrentLoadingState(mAdapter.LOAD_MORE_STATE_NOMORE);
            } else {
                mIsloadmore = true;
                mAdapter.setCurrentLoadingState(mAdapter.LOAD_MORE_STATE_SUCCESS);
            }
        }
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onNoData(String msg) {
        ToastUtil.showMessage(msg);
        mProgressBar.setVisibility(View.GONE);
        mAdapter.setCurrentLoadingState(mAdapter.LOAD_MORE_STATE_NOMORE);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
                    int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItemPosition == mAdapter.getItemCount() - 1 && mIsloadmore) {
                        mAdapter.setCurrentLoadingState(mAdapter.LOAD_MORE_STATE_LOADING);
                        //触发加载更多数据
                        pageNumber++;
                        mCreateTabPersenter.loadData(Constants.URLS.GET_ALL_CHICK_INFO, mEquipmentId, pageNumber + "");
                    }
                }
            }
        }
    };

    @Override
    public void onLookCurve(int position) {
        CreateTabListBean.ResponseBean.RowsBean rowsBean = mRowsBeans.get(position);
        mIntent = new Intent(this, HarmfulGasCurveActivity.class);
        mIntent.putExtra("carNumber", rowsBean.getCarNum());
        mIntent.putExtra("equipmentId", rowsBean.getDeviceno());
        startActivity(mIntent);
    }

    @Override
    public void onSuccess(DaoQueryBean bean) {
        if (bean != null){
            mCarNumber = bean.carNumber;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(mCarNumber)){
                        mEditBtn.setVisibility(View.VISIBLE);
                    }else {
                        mEditBtn.setVisibility(View.GONE);
                    }
                }
            });
        }else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mEditBtn.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onUpdataSuccess(int result) {

    }

    @Override
    public void onDeleteSuccess(int result) {

    }

    @Override
    public void onInsertSuccess(int result) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        //查询数据库有没有草稿
        DaoUtil daoUtil = new DaoUtil(new GasDataBaseDao(this),this);
        daoUtil.query();
    }


    private ServiceConnection mServiceC = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //mOcrplateidResult = OcrplateidResult.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**
     * 兼容Android5.0中service的intent一定要显性声明
     *
     * @param context
     * @param implicitIntent
     * @return
     */
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        //通过queryIntentActivities()方法，查询Android系统的所有具备ACTION_MAIN和CATEGORY_LAUNCHER
        //的Intent的应用程序，点击后，能启动该应用，说白了就是做一个类似Home程序的简易Launcher 。
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
    @Override
    protected void onResume() {
        super.onResume();
       /* try {
            if (mOcrplateidResult != null) {
                String s = mOcrplateidResult.qureyResult();
                mNumberDialog.setResultCarNumber(s);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/
    }



}
