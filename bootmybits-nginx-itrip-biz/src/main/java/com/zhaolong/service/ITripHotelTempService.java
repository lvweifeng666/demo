package com.zhaolong.service;

import com.zhaolong.po.ItripHotelTempStore;
import com.zhaolong.util.StoreVO;

import java.util.List;
import java.util.Map;

public interface ITripHotelTempService {
    public  List<StoreVO> queryRoomStore(Map<String, Object> param) throws Exception ;
    public Integer queryRoomStoreById(Map<String, Object> param)throws Exception;
    public boolean vaildateRoomStore(Map<String, Object> param)throws Exception;
}
