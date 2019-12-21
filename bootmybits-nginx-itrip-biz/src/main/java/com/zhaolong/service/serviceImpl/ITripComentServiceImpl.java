package com.zhaolong.service.serviceImpl;

import com.zhaolong.mapper.ItripCommentMapper;
import com.zhaolong.service.ITripComentService;
import com.zhaolong.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ITripComentServiceImpl implements ITripComentService {

@Autowired
private ItripCommentMapper itripCommentMapper;

    public ItripCommentMapper getItripCommentMapper() {
        return itripCommentMapper;
    }

    public void setItripCommentMapper(ItripCommentMapper itripCommentMapper) {
        this.itripCommentMapper = itripCommentMapper;
    }



    @Override
    public ItripScoreCommentVO getCommentAvgScore(Long hotelId) throws Exception {
        return itripCommentMapper.getCommentAvgScore(hotelId);
    }

    @Override
    public Integer getItripCommentCountByMap(Map<String, Object> param) throws Exception {
        return itripCommentMapper.getItripCommentCountByMap(param);
    }

    @Override
    public Page<ItripListCommentVO> queryItripCommentPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripCommentMapper.getItripCommentCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripListCommentVO> itripCommentList = itripCommentMapper.getItripCommentListByMap(param);
        page.setRows(itripCommentList);
        return page;
    }

}

