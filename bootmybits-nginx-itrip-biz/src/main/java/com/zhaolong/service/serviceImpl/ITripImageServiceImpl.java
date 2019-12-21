package com.zhaolong.service.serviceImpl;

import com.zhaolong.mapper.ItripImageMapper;
import com.zhaolong.service.ITripImageService;
import com.zhaolong.util.ItripImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ITripImageServiceImpl  implements ITripImageService {
@Autowired
private ItripImageMapper itripImageMapper;

    public ItripImageMapper getItripImageMapper() {
        return itripImageMapper;
    }

    public void setItripImageMapper(ItripImageMapper itripImageMapper) {
        this.itripImageMapper = itripImageMapper;
    }

    @Override
    public List<ItripImageVO> getItripImageListByMap(Map<String, Object> param) throws Exception {
        return itripImageMapper.getItripImageListByMap(param);
    }
}
