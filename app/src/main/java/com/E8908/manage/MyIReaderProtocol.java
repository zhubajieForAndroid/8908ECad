package com.E8908.manage;

import com.xuhao.didi.core.protocol.IReaderProtocol;

import java.nio.ByteOrder;

public class MyIReaderProtocol implements IReaderProtocol {
    @Override
    public int getHeaderLength() {
        return 2;
    }

    @Override
    public int getBodyLength(byte[] header, ByteOrder byteOrder) {
        return header[1];
    }
}
