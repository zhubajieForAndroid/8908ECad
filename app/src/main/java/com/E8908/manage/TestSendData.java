package com.E8908.manage;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

public class TestSendData implements ISendable {
    private byte[] datas;
    public TestSendData(byte[] login) {
        datas = login;
    }

    @Override
    public byte[] parse() {
        return datas;
    }
}
