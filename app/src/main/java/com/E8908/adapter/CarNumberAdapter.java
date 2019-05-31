package com.E8908.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.bean.YunInfoBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarNumberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<YunInfoBean.ObjBean> mCarNumbers;
    private Map<Integer,Boolean> tempState = new HashMap<>();
    private int checkedPosition = -1;
    private boolean onBind;

    private OnCarNumberItemClickListener mOnitemClickListener;
    public CarNumberAdapter(Context context,List<YunInfoBean.ObjBean> carNumbers){
        mCarNumbers = carNumbers;
        mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = View.inflate(mContext, R.layout.view_car_number_item, null);
        return new CarNumberItemViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof CarNumberItemViewHolder){
            YunInfoBean.ObjBean bean = mCarNumbers.get(i);
            final CarNumberItemViewHolder holder = (CarNumberItemViewHolder) viewHolder;
            holder.mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mCheckBox.isChecked()){
                        holder.mCheckBox.setChecked(false);
                        tempState.remove(i);
                        if (tempState.size() == 0) {
                            checkedPosition = -1; //-1 代表一个都未选择
                        }
                    }else {
                        holder.mCheckBox.setChecked(true);
                        tempState.clear();
                        tempState.put(i,true);
                        checkedPosition = i;
                    }
                    if (!onBind) {
                        notifyDataSetChanged();
                    }
                    mOnitemClickListener.carNumberItemClick(checkedPosition);
                }
            });
            onBind = true;
            if (tempState != null && tempState.containsKey(i)) {
                holder.mCheckBox.setChecked(true);
            } else {
                holder.mCheckBox.setChecked(false);
            }
            onBind = false;
            String isUploadCase = bean.getIsUploadCase();               //是否上传了案例
            if ("1".equals(isUploadCase)){
                holder.mCarName.setTextColor(mContext.getResources().getColor(R.color.gray));
                holder.mCarName.setText(bean.getCarNo());
            }else {
                holder.mCarName.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.mCarName.setText(bean.getCarNo());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mCarNumbers != null){
            return mCarNumbers.size();
        }
        return 0;
    }
    private class CarNumberItemViewHolder extends RecyclerView.ViewHolder{
        public CheckBox mCheckBox;
        public TextView mCarName;
        public  LinearLayout mContainer;

        public CarNumberItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mCarName = itemView.findViewById(R.id.car_name);
            mCheckBox = itemView.findViewById(R.id.radio);
            mContainer = itemView.findViewById(R.id.car_number_container);

        }
    }

    public interface OnCarNumberItemClickListener{
        void carNumberItemClick(int selectPosition);
    }

    public void setOnitemClickListener(OnCarNumberItemClickListener onitemClickListener) {
        mOnitemClickListener = onitemClickListener;
    }
}
