package com.zhaolong.service.serviceImpl;

import com.zhaolong.service.ITripHotelService;
import com.zhaolong.mapper.ItripHotelMapper;
import com.zhaolong.po.ItripAreaDic;
import com.zhaolong.po.ItripHotel;
import com.zhaolong.po.ItripLabelDic;
import com.zhaolong.service.ITripHotelService;
import com.zhaolong.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripHotelServiceImpl implements ITripHotelService {

    @Autowired
    private ItripHotelMapper itripHotelMapper;

    public ItripHotelMapper getItripHotelMapper() {
        return itripHotelMapper;
    }

    public void setItripHotelMapper(ItripHotelMapper itripHotelMapper) {
        this.itripHotelMapper = itripHotelMapper;
    }

    public ItripHotel getItripHotelById(Long id)throws Exception{
        return itripHotelMapper.getItripHotelById(id);
    }
//根据酒店id查询视频文字

      public HotelVideoDescVO getVideoDescByHotelId(Long id)throws Exception{
        //区域字典表商圈区域
        HotelVideoDescVO hotelVideoDescVO = new HotelVideoDescVO();
       //特色
        List<ItripAreaDic> itripAreaDicList = new ArrayList<>();
        //处理商圈
        itripAreaDicList = itripHotelMapper.getHotelAreaByHotelId(id);
        List<String> tempList1 = new ArrayList<>();
        for (ItripAreaDic itripAreaDic:itripAreaDicList) {
            tempList1.add(itripAreaDic.getName());
        }
        hotelVideoDescVO.setTradingAreaNameList(tempList1);

        //处理特色
        List<ItripLabelDic> itripLabelDicList = new ArrayList<>();

        itripLabelDicList = itripHotelMapper.getHotelFeatureByHotelId(id);
        List<String> tempList2 = new ArrayList<>();
        for (ItripLabelDic itripLabelDic:itripLabelDicList) {
            tempList2.add(itripLabelDic.getName());
        }
        hotelVideoDescVO.setHotelFeatureList(tempList2);
     //处理酒店名称
        hotelVideoDescVO.setHotelName(itripHotelMapper.getItripHotelById(id).getHotelName());
        return hotelVideoDescVO;
    }

//根据酒店设施查询
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelFacilitiesById(id);
    }



    //根据酒店政策查询
   public ItripSearchPolicyHotelVO queryHotelPolicy(Long id)throws Exception{
        return itripHotelMapper.queryHotelPolicy(id);
    }

//根据酒店特色查询
    public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id) throws Exception {
        List<ItripLabelDic> itripLabelDicList = new ArrayList<>();
        itripLabelDicList = itripHotelMapper.getHotelFeatureByHotelId(id);
        ItripSearchDetailsHotelVO vo = new ItripSearchDetailsHotelVO();
        List<ItripSearchDetailsHotelVO> list = new ArrayList<ItripSearchDetailsHotelVO>();
        vo.setName("酒店介绍");
        vo.setDescription(itripHotelMapper.getItripHotelById(id).getDetails());
        list.add(vo);
        for (ItripLabelDic itripLabelDic:itripLabelDicList) {
            ItripSearchDetailsHotelVO vo2 = new ItripSearchDetailsHotelVO();
            vo2.setName(itripLabelDic.getName());
            vo2.setDescription(itripLabelDic.getDescription());
            list.add(vo2);
        }
        return list;
    }
}
