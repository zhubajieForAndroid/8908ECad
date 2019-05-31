package com.E8908.textFunction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.E8908.R;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TextResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    TextView mTime;
    TextView mEquipmentType;
    TextView mEquipmentId;
    TextView mAdd;
    TextView mBack;
    TextView mWuhua;
    TextView mShajun;
    TextView mJinghua;
    TextView mService;
    TextView mDtu;
    TextView mLoopCount;
    private Context mContext;
    private List<TextTabBean.ResponseBean> mResponseBeans;

    public TextResultAdapter(Context context, List<TextTabBean.ResponseBean> beans) {
        mContext = context;
        mResponseBeans = beans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.view_text_adapter, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TextTabBean.ResponseBean responseBean = mResponseBeans.get(i);
        int fillStatus = responseBean.getFillStatus();
        if (fillStatus == 1){
            mAdd.setTextColor(mContext.getResources().getColor(R.color.red));
            mAdd.setText("异常");
        }else {
            mAdd.setTextColor(mContext.getResources().getColor(R.color.white));
            mAdd.setText("正常");
        }
        int recycleStatus = responseBean.getRecycleStatus();
        if (recycleStatus == 1){
            mBack.setTextColor(mContext.getResources().getColor(R.color.red));
            mBack.setText("异常");
        }else {
            mBack.setTextColor(mContext.getResources().getColor(R.color.white));
            mBack.setText("正常");
        }
        int atomizeStatus = responseBean.getAtomizeStatus();    //雾化
        if (atomizeStatus == 1){
            mWuhua.setTextColor(mContext.getResources().getColor(R.color.red));
            mWuhua.setText("异常");
        }else {
            mWuhua.setTextColor(mContext.getResources().getColor(R.color.white));
            mWuhua.setText("正常");
        }
        int sterilizeStatus = responseBean.getSterilizeStatus();            //杀菌
        if (sterilizeStatus == 1){
            mShajun.setTextColor(mContext.getResources().getColor(R.color.red));
            mShajun.setText("异常");
        }else {
            mShajun.setTextColor(mContext.getResources().getColor(R.color.white));
            mShajun.setText("正常");
        }
        int purifyStatus = responseBean.getPurifyStatus();          //净化
        if (purifyStatus == 1){
            mJinghua.setTextColor(mContext.getResources().getColor(R.color.red));
            mJinghua.setText("异常");
        }else {
            mJinghua.setTextColor(mContext.getResources().getColor(R.color.white));
            mJinghua.setText("正常");
        }
        int connectStatus = responseBean.getConnectStatus();        //服务器
        if (connectStatus == 1){
            mService.setTextColor(mContext.getResources().getColor(R.color.red));
            mService.setText("异常");
        }else {
            mService.setTextColor(mContext.getResources().getColor(R.color.white));
            mService.setText("正常");
        }
        int dtustatus = responseBean.getDtustatus();            //DTU
        if (dtustatus == 1){
            mDtu.setTextColor(mContext.getResources().getColor(R.color.red));
            mDtu.setText("异常");
        }else {
            mDtu.setTextColor(mContext.getResources().getColor(R.color.white));
            mDtu.setText("正常");
        }
        int cycleTestTimes = responseBean.getCycleTestTimes();      //循环次数
        mLoopCount.setText(cycleTestTimes+"次");
        long uploadTime = responseBean.getUploadTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = simpleDateFormat.format(uploadTime);
        mTime.setText(format);

        String equipmentType = responseBean.getEquipmentType();
        if (!TextUtils.isEmpty(equipmentType))
        mEquipmentType.setText(equipmentType);
        String equipmentCode = responseBean.getEquipmentCode();
        if (!TextUtils.isEmpty(equipmentCode))
            mEquipmentId.setText(equipmentCode);
    }

    @Override
    public int getItemCount() {
        return mResponseBeans.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTime = itemView.findViewById(R.id.time);
            mEquipmentType = itemView.findViewById(R.id.equipment_type);
            mEquipmentId = itemView.findViewById(R.id.equipment_id);
            mAdd = itemView.findViewById(R.id.add);
            mBack = itemView.findViewById(R.id.back);
            mWuhua = itemView.findViewById(R.id.wuhua);
            mShajun = itemView.findViewById(R.id.shajun);
            mJinghua = itemView.findViewById(R.id.jinghua);
            mService = itemView.findViewById(R.id.service);
            mDtu = itemView.findViewById(R.id.dtu);
            mLoopCount = itemView.findViewById(R.id.loop_count);
        }
    }
}
