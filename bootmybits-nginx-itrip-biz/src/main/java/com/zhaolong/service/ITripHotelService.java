package com.zhaolong.service;

import com.zhaolong.po.ItripHotel;
import com.zhaolong.util.HotelVideoDescVO;
import com.zhaolong.util.ItripSearchDetailsHotelVO;
import com.zhaolong.util.ItripSearchFacilitiesHotelVO;
import com.zhaolong.util.ItripSearchPolicyHotelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ITripHotelService {
//根据酒店ID查询酒店特色和介绍
public List<ItripSearchDetailsHotelVO> queryHotelDetails( Long id) throws Exception;
//根据酒店ID查询酒店设施
public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id)throws Exception;
   //根据酒店政策查询

public ItripSearchPolicyHotelVO queryHotelPolicy(Long id)throws Exception;
//根据酒店id查询酒店视频文字
public HotelVideoDescVO getVideoDescByHotelId(Long id)throws Exception;

    public ItripHotel getItripHotelById(Long id)throws Exception;
}
