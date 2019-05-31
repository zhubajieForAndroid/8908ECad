package com.E8908.persenter;

import java.util.Map;

public interface BasePersenter {
    void loadData(String url,int pageNum,int pageCounts);
    void loadData(String url,String equipmentID,String curnimber);
    void loadData(String url,Map<String,String> pames);
}
