package com.E8908.manage;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;

public class PulseData implements IPulseSendable {
    private byte[] mDatas;
    public PulseData(byte[] data){
        mDatas = data;
    }

    @Override
    public byte[] parse() {
        return mDatas;
    }
}
