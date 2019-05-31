package com.E8908.widget;

import android.content.Context;
import android.widget.TextView;

import com.E8908.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;


public class MyMarkerView extends MarkerView {
    private TextView beforeValue;
    private TextView afterValue;
    private TextView mTypeBefore;
    private TextView mTypeAdter;
    private Context mContext;
    private int mState;

    public MyMarkerView(Context context, int state) {
        super(context, R.layout.layout_markerview);
        mContext = context;
        mState = state;
        mTypeBefore =  findViewById(R.id.type_before);
        mTypeAdter =  findViewById(R.id.type_after);
        beforeValue =  findViewById(R.id.value_before);
        afterValue =  findViewById(R.id.value_after);
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Chart chart = getChartView();
        if (chart instanceof LineChart){
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            for (int i = 0; i < dataSetList.size(); i++) {
                LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                float y = 0;
                List<Entry> values = dataSet.getValues();
                float x = e.getX();
                for (int j = 0; j < values.size(); j++) {
                    if (x > j){
                        y = 0;
                    }else {
                        y = dataSet.getValues().get((int) x).getY();
                    }
                }
                if (i == 0) {
                    beforeValue.setText(y+"");
                    float result;
                    switch (mState){
                        case 1:                 //甲醛
                            result = y*10000;
                            setTextAndColor(result,mTypeBefore,300,1000,3000);
                            break;
                        case 2:                 //tvoc
                            result = y*100;
                            setTextAndColor(result,mTypeBefore,60,100,160);
                            break;
                        case 3:                 //pm
                            result = y;
                            setTextAndColor(result,mTypeBefore,35,75,150);
                            break;
                        case 4:                 //温度
                            result = y*10;
                            setWenduTextAndColor(result,mTypeBefore,180,250,320);
                            break;
                        case 5:                 //湿度
                            result = y*10;
                            setShiduTextAndColor(result,mTypeBefore,50,60);
                            break;
                    }

                }
                if (i == 1) {

                    afterValue.setText(y+"");
                    float result;
                    switch (mState){
                        case 1:                 //甲醛
                            result = y*10000;
                            setTextAndColor(result,mTypeAdter,300,1000,3000);
                            break;
                        case 2:                 //tvoc
                            result = y*100;
                            setTextAndColor(result,mTypeAdter,60,100,160);
                            break;
                        case 3:                 //pm
                            result = y;
                            setTextAndColor(result,mTypeAdter,35,75,150);
                            break;
                        case 4:                 //温度
                            result = y*10;
                            setWenduTextAndColor(result,mTypeAdter,180,250,320);
                            break;
                        case 5:                 //湿度
                            result = y*10;
                            setShiduTextAndColor(result,mTypeAdter,50,60);
                            break;
                    }
                }

            }
        }
        super.refreshContent(e, highlight);
    }

    private void setShiduTextAndColor(float result, TextView typeBefore, int i, int i1) {
        if (result >= i && result <= i1) {
            typeBefore.setText("优秀");
        } else {
            typeBefore.setText("重度污染");
        }
    }

    private void setWenduTextAndColor(float result, TextView typeBefore, int i, int i1, int i2) {
        if (result < i) {
            typeBefore.setText("重度污染");
        }
        if (result > i1 && result <= i2) {
            typeBefore.setText("优秀");
        }
        if (result > i2) {
            typeBefore.setText("重度污染");
        }
    }

    private void setTextAndColor(float result,TextView typeBefore,int i1,int i2,int i3) {
        if (result < i1) {
            typeBefore.setText("优秀");
        }
        if (result >= i1 && result < i2) {
            typeBefore.setText("一般");
        }
        if (result >= i2 && result < i3) {             //轻度污染
            typeBefore.setText("轻度污染");
        }
        if (result >= i3) {                                    //重度污染
            typeBefore.setText("重度污染");
        }
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
