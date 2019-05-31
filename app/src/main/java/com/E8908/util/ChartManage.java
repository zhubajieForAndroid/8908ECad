package com.E8908.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.E8908.R;
import com.E8908.widget.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class ChartManage {
    private LineChart mLineChart;
    private Context mContext;
    private int mState;
    public ChartManage(LineChart lineChart, Context context, int state){
        mLineChart = lineChart;
        mContext = context;
        mState = state;
        initLine();

    }
    private void initLine() {
        //设置动画
        mLineChart.animateY(2500);
        mLineChart.animateX(1500);
        mLineChart.setScaleEnabled(false);
        //设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setDoubleTapToZoomEnabled(false);
        mLineChart.setNoDataText("当前没有数据");
        // 没有数据时显示的文字样式
        mLineChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);
        //设置样式
        YAxis rightAxis = mLineChart.getAxisRight();
        //设置图表右边的y轴禁用
        rightAxis.setEnabled(false);

        //y轴
        YAxis axisLeft = mLineChart.getAxisLeft();
        axisLeft.setTextSize(18f);
        axisLeft.setTextColor(Color.WHITE);
        axisLeft.setStartAtZero(true);
        axisLeft.setLabelCount(16, true);//Y轴上标签显示的数量true是进准的
        axisLeft.enableGridDashedLine(10f,5f,20f);      //设置网格虚线
        axisLeft.setGridColor(Color.parseColor("#264c61"));                 //设置网格线颜色
        axisLeft.setAxisLineColor(Color.WHITE);                                         //设置左Y轴颜色
        axisLeft.setAxisLineWidth(2f);                                                  //设置左Y轴宽度
        //设置x轴
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(false);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisLineWidth(2f);
        //透明化图例
        Legend legend = mLineChart.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);



        //隐藏x轴描述
        Description description = new Description();
        description.setEnabled(false);
        mLineChart.setDescription(description);
        //自定义MarkerView
        MyMarkerView markerView = new MyMarkerView(mContext,mState);
        markerView.setChartView(mLineChart);
        mLineChart.setMarker(markerView);
    }
    public void initData(List<Entry> data2) {
        LineDataSet data = new LineDataSet(data2, ""); // add entries to dataset
        data.setColor(Color.parseColor("#0965f9"));//线条颜色
        data.setCircleColor(Color.parseColor("#ffffff"));//圆点颜色
        data.setCircleRadius(5f);
        data.setDrawValues(false);
        data.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        data.setLineWidth(3f);//曲线宽度;
        data.setHighLightColor(mContext.getResources().getColor(R.color.red));
        data.setDrawHorizontalHighlightIndicator(false);  // 画水平高亮指示器，默认true
        data.setDrawVerticalHighlightIndicator(true);    // 垂直方向高亮指示器,默认true
        //在加一条
        mLineChart.getLineData().addDataSet(data);

    }
    public void initData1(List<Entry> data1){
        LineDataSet dataSet = new LineDataSet(data1, "");
        dataSet.setColor(Color.parseColor("#e6e602"));//线条颜色
        dataSet.setCircleColor(Color.parseColor("#ffffff"));//圆点颜色
        dataSet.setCircleRadius(5f);
        dataSet.setDrawValues(false);//不显示数值
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setLineWidth(3f);//曲线宽度
        dataSet.setHighLightColor(mContext.getResources().getColor(R.color.red));
        dataSet.setDrawHorizontalHighlightIndicator(false);  // 画水平高亮指示器，默认true
        dataSet.setDrawVerticalHighlightIndicator(true);    // 垂直方向高亮指示器,默认true
        //设置一条线
        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
    }
}
