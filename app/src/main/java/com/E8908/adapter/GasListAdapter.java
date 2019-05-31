package com.E8908.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.bean.CreateTabListBean;

import java.util.List;

public class GasListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CreateTabListBean.ResponseBean.RowsBean> mRowsBeans;
    private Context mContext;
    private OnLookCurveBtnListener mOnLookCurveBtnListener;
    private static final int VIEWTYPE_ITEM = 0;         //普通条目
    private static final int VIEWTYPE_FOOT = 1;         //加载更多条目
    public static final int LOAD_MORE_STATE_LOADING = 0;         //加载中
    public static final int LOAD_MORE_STATE_ERROR = 1;         //加载更多失败
    public static final int LOAD_MORE_STATE_NOMORE= 2;         //没有加载更多数据了
    public static final int LOAD_MORE_STATE_SUCCESS= 3;         //加载更多成功
    private int mCurrentLoadingState = LOAD_MORE_STATE_SUCCESS;      //当前加载状态


    public GasListAdapter(Context context, List<CreateTabListBean.ResponseBean.RowsBean> rowsBeans) {
        mRowsBeans = rowsBeans;
        mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case VIEWTYPE_ITEM:
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.view_gas_list_item, viewGroup, false);
                return new ItemViewHolder(itemView);
            case VIEWTYPE_FOOT:
                View footView = LayoutInflater.from(mContext).inflate(R.layout.view_gas_list_foot, viewGroup, false);
                return new FootViewHolder(footView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        if (holder instanceof ItemViewHolder){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            CreateTabListBean.ResponseBean.RowsBean rowsBean = mRowsBeans.get(i);
            String checkDate = rowsBean.getCheckDate();
            if (!TextUtils.isEmpty(checkDate)){
                itemViewHolder.mCheckTime.setText(checkDate);
            }
            String carNum = rowsBean.getCarNum();
            if (!TextUtils.isEmpty(carNum))
                itemViewHolder.mCarNumber.setText(carNum);
            String pm25 = rowsBean.getPm25();
            if (!TextUtils.isEmpty(pm25)) {
                float v = Float.parseFloat(pm25);
                itemViewHolder.mPmValue.setText("PM2.5 : " + v);
                setColorAndText(v, 35, itemViewHolder.mPmLevel, 75, 150);
            }
            String tvoc = rowsBean.getTvoc();
            if (!TextUtils.isEmpty(tvoc)) {
                float v = Float.parseFloat(tvoc);
                itemViewHolder.mTvocValue.setText("TVOC : " + v);
                float result = v * 100;
                setColorAndText(result, 60, itemViewHolder.mTvocLevel, 100, 160);
            }
            String formaldehyde = rowsBean.getFormaldehyde();
            if (!TextUtils.isEmpty(formaldehyde)) {
                float v = Float.parseFloat(formaldehyde);
                itemViewHolder.mFormaldehydeValue.setText("甲醛浓度 : " + v);
                float result = v * 10000;
                setColorAndText(result, 300, itemViewHolder.mFormaldehydeLevel, 1000, 3000);
            }
            //查看趋势图
            itemViewHolder.mLookCurve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnLookCurveBtnListener.onLookCurve(i);
                }
            });

        }
        if (holder instanceof  FootViewHolder){
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (mCurrentLoadingState){
                case LOAD_MORE_STATE_LOADING:
                    footViewHolder.mProgress.setVisibility(View.VISIBLE);
                    footViewHolder.mLoadState.setText("加载中...");
                    break;
                case LOAD_MORE_STATE_ERROR:
                    footViewHolder.mProgress.setVisibility(View.GONE);
                    footViewHolder.mLoadState.setText("-- 网络异常 --");
                    break;
                case LOAD_MORE_STATE_NOMORE:
                    footViewHolder.mProgress.setVisibility(View.GONE);
                    footViewHolder.mLoadState.setText("-- 没有更多数据了 --");
                    break;
                case LOAD_MORE_STATE_SUCCESS:
                    footViewHolder.mProgress.setVisibility(View.GONE);
                    footViewHolder.mLoadState.setText("");
                    break;
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1) {
            return VIEWTYPE_FOOT;
        } else {
            return VIEWTYPE_ITEM;
        }
    }

    private void setColorAndText(float v, int i2, TextView pmValue, int i3, int i4) {
        if (v < i2) {
            pmValue.setTextColor(mContext.getResources().getColor(R.color.green));
            pmValue.setText("优秀");
        }
        if (v >= i2 && v < i3) {
            pmValue.setTextColor(mContext.getResources().getColor(R.color.blue));
            pmValue.setText("正常");
        }
        if (v >= i3 && v < i4) {
            pmValue.setTextColor(mContext.getResources().getColor(R.color.yellow));
            pmValue.setText("一般危害");
        }
        if (v >= i4) {
            pmValue.setTextColor(mContext.getResources().getColor(R.color.red));
            pmValue.setText("重度危害");
        }
    }

    @Override
    public int getItemCount() {
        if (mRowsBeans.size()>0){
            return mRowsBeans.size()+1;
        }else {
            return 0;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView mPmLevel;
        public TextView mTvocLevel;
        public TextView mFormaldehydeLevel;
        public TextView mPmValue;
        public TextView mTvocValue;
        public TextView mFormaldehydeValue;
        public TextView mCarNumber;
        public TextView mCheckTime;
        public ImageView mLookCurve;
        public RelativeLayout mEditTab;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mPmLevel = itemView.findViewById(R.id.pm_level);
            mTvocLevel = itemView.findViewById(R.id.tvoc_level);
            mFormaldehydeLevel = itemView.findViewById(R.id.formaldehyde_level);
            mPmValue = itemView.findViewById(R.id.pm_value);
            mTvocValue = itemView.findViewById(R.id.tvoc_value);
            mFormaldehydeValue = itemView.findViewById(R.id.formaldehyde_value);
            mCarNumber = itemView.findViewById(R.id.car_number);
            mCheckTime = itemView.findViewById(R.id.check_time);
            mLookCurve = itemView.findViewById(R.id.look_curve);

            mEditTab = itemView.findViewById(R.id.edit_tab_container);

        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar mProgress;
        private final TextView mLoadState;

        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
            mProgress = itemView.findViewById(R.id.progress);
            mLoadState = itemView.findViewById(R.id.state_text);
        }
    }

    public void setCurrentLoadingState(int currentLoadingState) {
        mCurrentLoadingState = currentLoadingState;
        notifyItemChanged(getItemCount()-1);
    }

    public interface OnLookCurveBtnListener {
        void onLookCurve(int position);
    }

    public void setOnLookCurveBtnListener(OnLookCurveBtnListener onLookCurveBtnListener) {
        mOnLookCurveBtnListener = onLookCurveBtnListener;
    }


}
