package com.zhaolong.service;

import com.zhaolong.po.ItripLabelDic;
import com.zhaolong.util.ItripLabelDicVO;

import java.util.List;
import java.util.Map;

public interface ITripLabelDicService {
    //查询酒店特色文字
    public List<ItripLabelDic> getItripLabelDicListByMap(Map<String, Object> param)throws Exception;

//根据床型父级id查询床型数据
public List<ItripLabelDicVO>  getItripLabelDicByParentId(Long parentId)throws Exception;

}
