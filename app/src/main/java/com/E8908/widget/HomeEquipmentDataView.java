package com.E8908.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.OkhttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class HomeEquipmentDataView extends RelativeLayout {
    @Bind(R.id.eqeuipment_id)
    TextView mEqeuipmentId;
    @Bind(R.id.surplus_oil)
    TextView mSurplusOil;
    @Bind(R.id.work_ranking)
    TextView mWorkRanking;
    @Bind(R.id.work_ranking_month)
    TextView mWorkRankingMonth;
    @Bind(R.id.work_in_ranking)
    TextView mWorkInRanking;
    @Bind(R.id.work_in_ranking_month)
    TextView mWorkInRankingMonth;
    private Context mContext;
    private String mEquipmentNumber;
    private int mResultRatioNumbwe;

    public HomeEquipmentDataView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        View.inflate(mContext, R.layout.fragment_home_ble_data, this);
        ButterKnife.bind(this, this);

    }

    public void setData(byte[] buffer, Map<String, Object> rankInfo) {
        if (rankInfo != null) {
            for (Map.Entry<String,Object> keys : rankInfo.entrySet()) {
                String key = keys.getKey();
                switch (key){
                    case "nationWideRanking":       //全国排名
                        mWorkRanking.setText("第" + (keys.getValue()==null?0:keys.getValue()) + "名");
                        break;
                    case "interiorRanking":         //内部排名
                        mWorkInRanking.setText("第" + (keys.getValue()==null?0:keys.getValue()) + "名");
                        break;
                    case "nationWideMonthRanking":  //当月全国排名
                        mWorkRankingMonth.setText("第" + (keys.getValue()==null?0:keys.getValue()) + "名");
                        break;
                    case "interiorMonthRanking":    //当月内部排名
                        mWorkInRankingMonth.setText("第" + (keys.getValue()==null?0:keys.getValue()) + "名");
                        break;
                }
            }
        }

        if (buffer == null)
            return;
        //获取序列号
        mEquipmentNumber = DataUtil.getEquipmentNumber(buffer);
        if (!TextUtils.isEmpty(mEquipmentNumber))
            mEqeuipmentId.setText(mEquipmentNumber);
        //获取液体升数
        String riseNumbwe = DataUtil.getRiseNumbwe(buffer);
        //药液量
        mResultRatioNumbwe = Integer.parseInt(riseNumbwe, 16);
        if (mResultRatioNumbwe < Constants.MAX_NUMBER) {
            mSurplusOil.setText(mResultRatioNumbwe + "ML (" + (mResultRatioNumbwe / 250) + ")次");
        } else {
            mSurplusOil.setText(Constants.MAX_NUMBER - 1 + "ML(80次)");
        }
    }

}
