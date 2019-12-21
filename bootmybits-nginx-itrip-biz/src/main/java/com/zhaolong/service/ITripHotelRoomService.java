package com.zhaolong.service;

import com.zhaolong.po.ItripHotelRoom;
import com.zhaolong.util.ItripHotelRoomVO;

import java.util.List;
import java.util.Map;

public interface ITripHotelRoomService {
    //光传酒店ID是不够的  在Mapper.xml文档中找方法
    //查询酒店房间列表
public List<ItripHotelRoomVO> getItripHotelRoomListByMap(Map<String,Object> param)throws Exception;


    //生成订单前酒店信息展示
    public ItripHotelRoom getItripHotelRoomById(Long id)throws Exception;
}
