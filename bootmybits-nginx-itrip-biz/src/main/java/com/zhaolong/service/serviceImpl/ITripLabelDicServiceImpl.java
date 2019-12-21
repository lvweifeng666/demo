package com.zhaolong.service.serviceImpl;

import com.zhaolong.mapper.ItripLabelDicMapper;
import com.zhaolong.po.ItripLabelDic;
import com.zhaolong.service.ITripLabelDicService;
import com.zhaolong.util.ItripLabelDicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ITripLabelDicServiceImpl implements ITripLabelDicService {
@Autowired
    private ItripLabelDicMapper itripLabelDicMapper;

    public ItripLabelDicMapper getItripLabelDicMapper() {
        return itripLabelDicMapper;
    }

    public void setItripLabelDicMapper(ItripLabelDicMapper itripLabelDicMapper) {
        this.itripLabelDicMapper = itripLabelDicMapper;
    }

    @Override
    public List<ItripLabelDic> getItripLabelDicListByMap(Map<String, Object> param) throws Exception {
        return itripLabelDicMapper.getItripLabelDicListByMap(param);
    }

    @Override
    public List<ItripLabelDicVO> getItripLabelDicByParentId(Long parentId) throws Exception {
        return itripLabelDicMapper.getItripLabelDicByParentId(parentId);
    }
}
