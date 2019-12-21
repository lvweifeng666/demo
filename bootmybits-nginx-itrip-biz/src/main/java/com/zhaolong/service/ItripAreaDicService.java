package com.zhaolong.service;

import com.zhaolong.po.ItripAreaDic;

import java.util.List;
import java.util.Map;

public interface ItripAreaDicService {

    public List<ItripAreaDic> getITripAreaDicListByMap(Map<String,Object> param)throws Exception;

    //查询热门城市酒店
    public List<ItripAreaDic>	getItripAreaDicListByMap(Map<String, Object> param)throws Exception;
}
