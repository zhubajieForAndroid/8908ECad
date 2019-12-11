package com.E8908.manage;

import android.os.SystemClock;
import android.util.Log;

import com.E8908.factory.ThreadPoolProxyFactory;
import com.E8908.util.DataUtil;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.impl.client.PulseManager;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.ISocketActionListener;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import static android.support.constraint.Constraints.TAG;


public class SocketManage {
    private IConnectionManager mManager;
    private OnSocketLinkListener mOnSocketLinkListener;
    private LinkServiceRunnable mLinkServiceRunnable;

    private SocketManage() {
    }

    private static SocketManage manage;

    public static SocketManage getSocketManage() {
        if (manage == null) {
            synchronized (SocketManage.class) {
                if (manage == null) {
                    manage = new SocketManage();
                }
            }
        }
        return manage;
    }

    public void init(String ip, int port) {
        //连接参数设置(IP,端口号)
        ConnectionInfo info = new ConnectionInfo(ip, port);
        mManager = OkSocket.open(info);

        //连接通道参数配置
        OkSocketOptions options = mManager.getOption();
        OkSocketOptions.setIsDebug(false);
        //基于当前参配对象构建一个参配建造者类
        OkSocketOptions.Builder optionsBuilder = new OkSocketOptions.Builder(options);
        //设置心跳间隔时间
        optionsBuilder.setPulseFrequency(30000);
        //设置最大数据丢失次数
        optionsBuilder.setPulseFeedLoseTimes(10);
        //设置连接超时时间,单位秒
        optionsBuilder.setConnectTimeoutSecond(5);
        //设置连接是管理保存,设置为true断开连接之后会自动连接一次
        optionsBuilder.setConnectionHolden(false);
        //建造一个新的参配对象并且付给通道
        mManager.option(optionsBuilder.build());

        mManager.registerReceiver(new ISocketActionListener() {
            @Override
            public void onSocketIOThreadStart(String s) {

            }

            @Override
            public void onSocketIOThreadShutdown(String s, Exception e) {

            }

            @Override
            public void onSocketReadResponse(ConnectionInfo connectionInfo, String s, OriginalData originalData) {
                mOnSocketLinkListener.socketReadResponse(connectionInfo, s, originalData);
            }

            @Override
            public void onSocketWriteResponse(ConnectionInfo connectionInfo, String s, ISendable iSendable) {

            }

            @Override
            public void onPulseSend(ConnectionInfo connectionInfo, IPulseSendable iPulseSendable) {

            }

            @Override
            public void onSocketDisconnection(ConnectionInfo connectionInfo, String s, Exception e) {
                mOnSocketLinkListener.onSocketDisconnection(connectionInfo, s, e);
            }

            @Override
            public void onSocketConnectionSuccess(ConnectionInfo connectionInfo, String s) {
                mOnSocketLinkListener.onSocketConnectionSuccess(connectionInfo, s);
            }

            @Override
            public void onSocketConnectionFailed(ConnectionInfo connectionInfo, String s, Exception e) {
                mOnSocketLinkListener.onSocketConnectionFailed(connectionInfo, s, e);
            }
        });
        //设置接受服务器数据监听
        OkSocketOptions.Builder builder = new OkSocketOptions.Builder();
        builder.setReaderProtocol(new MyIReaderProtocol());
        mManager.option(builder.build());

    }

    /**
     * 连接
     */
    public void connect() {
        if (mManager != null && !mManager.isConnect()) {
            if (mLinkServiceRunnable == null)
                mLinkServiceRunnable = new LinkServiceRunnable();
            ThreadPoolProxyFactory.getmThreadPoolProxy().submit(mLinkServiceRunnable);
        }
    }

    /**
     * 断开连接
     */
    public void disconnect(String id) {
        if (mManager != null && mManager.isConnect()) {
            try {
                mManager.send(new TestSendData(DataUtil.getLogOutData(id)));
                SystemClock.sleep(500);
                mManager.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 发送数据
     *
     * @param iSendable
     */
    public void sendData(ISendable iSendable) {
        if (mManager != null && mManager.isConnect()) {
            mManager.send(iSendable);
        }
    }

    /**
     * 发送心跳
     *
     * @param pulseSendable
     */
    public void sendHeartbeat(IPulseSendable pulseSendable) {
        if (mManager != null && mManager.isConnect()) {
            PulseManager pulseManager = mManager.getPulseManager();
            pulseManager.setPulseSendable(pulseSendable);
            pulseManager.pulse();
        }
    }


    /**
     * 获取连接管理器
     *
     * @return
     */
    public IConnectionManager getConnectionManager() {
        return mManager;
    }

    public interface OnSocketLinkListener {
        void socketReadResponse(ConnectionInfo connectionInfo, String s, OriginalData originalData);

        //Socket连接状态由连接->断开回调
        void onSocketDisconnection(ConnectionInfo connectionInfo, String s, Exception e);

        //连接成功
        void onSocketConnectionSuccess(ConnectionInfo connectionInfo, String s);

        //连接失败
        void onSocketConnectionFailed(ConnectionInfo connectionInfo, String s, Exception e);
    }

    public void setOnSocketLinkListener(OnSocketLinkListener onSocketLinkListener) {
        mOnSocketLinkListener = onSocketLinkListener;
    }

    /**
     * 是否连接服务器
     *
     * @return
     */
    public boolean isConnect() {
        if (mManager != null) {
            return mManager.isConnect();
        } else {
            return false;
        }
    }

    /**
     * socket连接服务器的线程
     */
    private class LinkServiceRunnable implements Runnable {
        @Override
        public void run() {
            mManager.connect();
        }
    }

}
