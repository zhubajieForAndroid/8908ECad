package com.E8908.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.E8908.R;
import com.E8908.base.CurverBean;
import com.E8908.util.ChartManage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class TVOC extends RelativeLayout {

    LineChart mLineChart;
    private ChartManage mManage;
    List<Entry> data1 = new ArrayList<>();
    List<Entry> data2 = new ArrayList<>();
    public TVOC(Context context) {
        this(context,null);
    }

    public TVOC(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View inflate = View.inflate(context, R.layout.view_fragment_history, this);
        mLineChart = inflate.findViewById(R.id.chart);
        mManage = new ChartManage(mLineChart, context, 2);
    }

    public void setData(CurverBean.ResponseBean responseBean) {
        if (responseBean == null)
            return;
        List<CurverBean.ResponseBean.AfterBean> after = responseBean.getAfter();
        List<CurverBean.ResponseBean.BeforeBean> before = responseBean.getBefore();
        for (int i = 0; i < before.size(); i++) {
            CurverBean.ResponseBean.BeforeBean beforeBean = before.get(i);
            Entry entry = new Entry(i, Float.parseFloat(beforeBean.getTvoc()));
            data1.add(entry);
        }
        for (int i = 0; i < after.size(); i++) {
            CurverBean.ResponseBean.AfterBean afterBean = after.get(i);
            Entry entry = new Entry(i, Float.parseFloat(afterBean.getTvoc()));
            data2.add(entry);
        }
        if (data1.size() > 0) {
            mManage.initData1(data1);
        }
        if (data2.size() > 0) {
            mManage.initData(data2);
        }
    }
}
