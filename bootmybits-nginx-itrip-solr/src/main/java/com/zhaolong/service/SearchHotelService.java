package com.zhaolong.service;
import com.zhaolong.util.ItripHotelVO;
import com.zhaolong.util.Page;
import com.zhaolong.util.SearchHotelVO;

import java.util.List;
import java.util.Map;

/**
 * solr搜索酒店的Service
 * Created by XX on 17-5-7.
 */
public interface SearchHotelService {
    /***
     * 搜索旅馆
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<ItripHotelVO> searchItripHotelPage(SearchHotelVO vo, Integer pageNo, Integer pageSize)throws Exception;
    /***
     * 根据热门城市查询酒店
     * @param pageSize
     * @return
     */
    public List<ItripHotelVO> searchItripHotelListByHotCity(Integer cityId, Integer pageSize)throws Exception;

}
