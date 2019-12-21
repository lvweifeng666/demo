package com.zhaolong.service;

import com.zhaolong.util.ItripListCommentVO;
import com.zhaolong.util.ItripScoreCommentVO;
import com.zhaolong.util.Page;

import java.util.Map;

public interface ITripComentService {
    //根据酒店id查看评分
    public ItripScoreCommentVO getCommentAvgScore(Long hotelId)throws Exception;
    //根据酒店ID查询各种评论数
    public Integer getItripCommentCountByMap(Map<String,Object> param) throws Exception;

//根据酒店id（评论条件）查询各种评论内容
   public Page<ItripListCommentVO> queryItripCommentPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize)throws Exception;

}
