package com.zhaolong.service.serviceImpl;

import com.zhaolong.mapper.ItripHotelTempStoreMapper;
import com.zhaolong.po.ItripHotelTempStore;
import com.zhaolong.service.ITripHotelTempService;
import com.zhaolong.util.EmptyUtils;
import com.zhaolong.util.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ITripHotelTempServiceImpl implements ITripHotelTempService {
    @Autowired
    private ItripHotelTempStoreMapper itripHotelTempStoreMapper;

    public ItripHotelTempStoreMapper getItripHotelTempStoreMapper() {
        return itripHotelTempStoreMapper;
    }

    public void setItripHotelTempStoreMapper(ItripHotelTempStoreMapper itripHotelTempStoreMapper) {
        this.itripHotelTempStoreMapper = itripHotelTempStoreMapper;
    }



    @Override
    public List<StoreVO> queryRoomStore(Map<String, Object> param) throws Exception {
        return itripHotelTempStoreMapper.queryRoomStore(param);
    }

    @Override
    public Integer queryRoomStoreById(Map<String, Object> param) throws Exception {
        return itripHotelTempStoreMapper.queryRoomStoreById(param);
    }

    @Override
    public boolean vaildateRoomStore(Map<String, Object> param) throws Exception {
        Integer count = (Integer) param.get("count");
        itripHotelTempStoreMapper.flushStore(param);
        List<StoreVO> storeVOList = itripHotelTempStoreMapper.queryRoomStore(param);
        if(EmptyUtils.isEmpty(storeVOList)){
            return false;
        }
        for (StoreVO vo : storeVOList) {
            if (vo.getStore() < count) {
                return false;
            }
        }
        return true;
    }

}
