package com.cad.widget;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cad.R;
import com.cad.activity.SystemSettingsActivity;
import com.cad.bean.EquipmentOnlineBean;
import com.cad.conf.Constants;
import com.cad.conf.Protocol;
import com.cad.util.SendUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dell on 2018/3/19.
 */

public class OnlineDialog extends Dialog implements View.OnTouchListener {

    TextView mName;

    TextView mType;

    EditText mSim;
    private List<String> companyName = new ArrayList<>();
    private List<String> companyIdList = new ArrayList<>();
    private List<String> equipmentTypeName = new ArrayList<>();
    private List<String> equipmentTypeIdList = new ArrayList<>();
    private int selectComanpyIndex;
    private int selectEquipmentTypeIndex;
    private static final String TAG = "SetVersionDialog";
    private SystemSettingsActivity mActivity;
    private ProgressDialog mDialog;
    private String mEquipmentID;
    private OnEquipmentIDlistener mOnEquipmentIDlistener;
    private String mSimText;

    public OnlineDialog(Context context, int themeResId) {
        super(context, themeResId);
        mActivity = (SystemSettingsActivity) context;
        mDialog = new ProgressDialog(context);
        mDialog.setMessage("正在上线,请稍后");
        getComapnyLIst();
        getEquipmentType();
    }

    public void setBitmap(int imageRes) {
        setContentView(R.layout.dialog_set_number);
        mName = (TextView) findViewById(R.id.name);
        mType = (TextView) findViewById(R.id.type);
        mSim = (EditText) findViewById(R.id.sim);
        ImageView iv = (ImageView) findViewById(R.id.dialog_bg_image);
        iv.setImageResource(imageRes);
        iv.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 161 && x <= 540) && (y >= 66 && y <= 129)) {                //公司名称
            SendUtil.controlVoice();
            showMyDialog(companyName, true);
        } else if ((x >= 161 && x <= 540) && (y >= 153 && y <= 210)) {         //设备类型
            SendUtil.controlVoice();
            showMyDialog(equipmentTypeName, false);
        } else if ((x >= 130 && x <= 450) && (y >= 326 && y <= 374)) {         //上线
            String name = mName.getText().toString();
            String type = mType.getText().toString();
            mSimText = mSim.getText().toString().trim();
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(type) && !TextUtils.isEmpty(mSimText)){
                SendUtil.controlVoice();
                equipmentOnLIne();
            }else {
                ToastUtil.showMessage("数据不能为空");
            }

        }
        return false;
    }

    /**
     * 获取设备类型列表
     */
    public void getEquipmentType() {
        Map<String, Object> pames = new HashMap<>();
        pames.put("url", Constants.URLS.GET_EQUIPMENT_TYPE);
        pames.put("faccount", Constants.USER_NAME);
        Protocol protocol = new Protocol() {
            @Override
            public void errorManage(IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showMessage("网络异常,获取设备列表失败");
                    }
                });
            }

            @Override
            public void parseData(Gson gson, String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectResult = jsonArray.getJSONObject(i);
                        String equipmentType = jsonObjectResult.getString("type");
                        String equipmentTypeID = jsonObjectResult.getString("equipmentTypeID");
                        equipmentTypeName.add(equipmentType);
                        equipmentTypeIdList.add(equipmentTypeID);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        protocol.setParams(pames);
        protocol.loadDataFromNet();
    }

    /**
     * 获取公司列表
     */
    public void getComapnyLIst() {
        Map<String, Object> pames = new HashMap<>();
        pames.put("url", Constants.URLS.GET_COMAPNY_TYPE);
        Protocol protocol = new Protocol() {
            @Override
            public void errorManage(IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "网络异常,获取公司列表失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void parseData(Gson gson, String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String companyFullName = jsonObject.getString("companyFullName");
                        String companyId = jsonObject.getString("companyId");
                        companyIdList.add(companyId);
                        companyName.add(companyFullName);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        protocol.setParams(pames);
        protocol.loadDataFromNet();
    }

    private void showMyDialog(List<String> data, final boolean isD) {
        if (data.size() > 0) {
            final String[] arr = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                arr[i] = data.get(i);
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
            if (isD){
                dialog.setTitle("公司列表");
            }else {
                dialog.setTitle("设备列表");
            }

            dialog.setItems(arr, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //保存一份选中的索引
                    if (isD) {
                        selectComanpyIndex = which;
                        mName.setText(arr[which]);
                    } else {
                        selectEquipmentTypeIndex = which;
                        mType.setText(arr[which]);
                    }
                }
            });
            dialog.show();
        }
    }

    private void equipmentOnLIne() {
        mDialog.show();
        Map<String, Object> pames = new HashMap<>();
        pames.put("url", Constants.URLS.START_ONLINE);
        pames.put("id", companyIdList.get(selectComanpyIndex) + equipmentTypeIdList.get(selectEquipmentTypeIndex));
        pames.put("simNum", mSimText);
        pames.put("simPassword", "000000");
        pames.put("createUser", Constants.USER_NAME);
        Protocol protocol = new Protocol() {
            @Override
            public void errorManage(IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        dismiss();
                        Toast.makeText(mActivity, "网络异常,上线失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void parseData(Gson gson, String s) {
                EquipmentOnlineBean equipmentOnlineBean = gson.fromJson(s, EquipmentOnlineBean.class);
                int code = equipmentOnlineBean.getCode();
                if (code == 0) {
                    mEquipmentID = equipmentOnlineBean.getResponse().get(0).getEquipmentID();
                    final String messagestr = equipmentOnlineBean.getResponse().get(0).getMessagestr();
                    if (TextUtils.isEmpty(messagestr)) {
                        if (!TextUtils.isEmpty(mEquipmentID)) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mOnEquipmentIDlistener.idListener(mEquipmentID);
                                    Toast.makeText(mActivity, "上线成功", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    mDialog.dismiss();
                                }
                            });
                        }
                    } else {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(mActivity, messagestr, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        };
        protocol.setParams(pames);
        protocol.loadDataFromNet();
    }
    public void setOnEquipmentIDlistener(OnEquipmentIDlistener listener){
        mOnEquipmentIDlistener = listener;
    }
    public interface OnEquipmentIDlistener{
        void idListener(String id);
    }
}
