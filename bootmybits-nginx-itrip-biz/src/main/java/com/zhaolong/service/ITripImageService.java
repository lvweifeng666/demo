package com.zhaolong.service;

import java.util.List;
import java.util.Map;

import com.zhaolong.po.ItripImage;
import com.zhaolong.util.ItripImageVO;

/**
* Created by shang-pc on 2015/11/7.
*/
public interface ITripImageService {


    //根据酒店id和评论ID查询图片展示路径和顺序
    public List<ItripImageVO> getItripImageListByMap(Map<String, Object> param) throws Exception;
//

}