package com.zhaolong.controller;
import com.zhaolong.service.SearchHotelService;
import com.zhaolong.po.Dto;
import com.zhaolong.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询酒店的controller
 * Created by XX on 17-5-10.
 */
@RestController
/*@Api(value = "API", basePath = "/http://api.itrap.com/api")*/
@RequestMapping(value = "/api/hotellist")
public class HotelListController {
    @Autowired
    private SearchHotelService searchHotelService;

    public SearchHotelService getSearchHotelService() {
        return searchHotelService;
    }

    public void setSearchHotelService(SearchHotelService searchHotelService) {
        this.searchHotelService = searchHotelService;
    }

    /***
     * 搜索酒店分页
     *
     * @param vo
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询酒店分页", httpMethod = "POST",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询酒店分页(用于查询酒店列表)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码: </p>" +
            "<p>20001: 系统异常,获取失败</p>" +
            "<p>20002: 目的地不能为空</p>")*/
    @RequestMapping(value = "/searchItripHotelPage")

    public Dto searchItripHotelPage(@RequestBody SearchHotelVO vo) {
        System.out.println("查询酒店搜索模块");
        Page page = null;//分页结果集合
        //传入数据判空判null
        if (EmptyUtils.isEmpty(vo) || EmptyUtils.isEmpty(vo.getDestination())) {
            return DtoUtil.returnFail("目的地不能为空", "20002");
        }
        try {
            //调用solr搜索业务层传入页面填入数据，分页getPageNo()开始，每页记录数PageSize()
            page = searchHotelService.searchItripHotelPage(vo, vo.getPageNo(), vo.getPageSize());
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "20001");
        }
        if (page.getRows().size()==0){
            System.out.println("城市酒店为空");
            return DtoUtil.returnFail("城市为空", "20004");
        }
        return DtoUtil.returnDataSuccess(page);
    }

    /* @ApiOperation(value = "根据热门城市查询酒店", httpMethod = "POST",
             protocols = "HTTP", produces = "application/json",
             response = Dto.class, notes = "根据热门城市查询酒店" +
             "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
             "<p>错误码: </p>" +
             "<p>20003: 系统异常,获取失败</p>" +
             "<p>20004: 城市id不能为空</p>")*/
    @RequestMapping(value = "/searchItripHotelListByHotCity")
    public Dto<Page<ItripHotelVO>> searchItripHotelListByHotCity(@RequestBody SearchHotCityVO vo) throws Exception {
        System.out.println("根据热门城市搜索");
        if (EmptyUtils.isEmpty(vo) || EmptyUtils.isEmpty(vo.getCityId())) {
            return DtoUtil.returnFail("城市id不能为空", "20004");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("cityId", vo.getCityId());
        List list = searchHotelService.searchItripHotelListByHotCity(vo.getCityId(), vo.getCount());
        if (list.size()==0){
            return DtoUtil.returnFail("城市为空", "20004");
        }
        return DtoUtil.returnDataSuccess(list);
    }
}
