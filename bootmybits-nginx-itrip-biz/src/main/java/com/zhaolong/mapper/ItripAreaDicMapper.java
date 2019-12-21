package com.zhaolong.mapper;

import com.zhaolong.po.ItripAreaDic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
@Mapper
//区域查询
public interface ItripAreaDicMapper {
//通过ID查询
	public ItripAreaDic getItripAreaDicById(@Param(value = "id") Long id)throws Exception;

	public List<ItripAreaDic>	getItripAreaDicListByMap(Map<String, Object> param)throws Exception;



}
