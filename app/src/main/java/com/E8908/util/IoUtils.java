package com.E8908.util;


import java.io.Closeable;
import java.io.IOException;

/**
 * Created by dell on 2018/6/3.
 */

public class IoUtils {
    public static void closeFileStream(Closeable... closeables){
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null){
                    closeable.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
