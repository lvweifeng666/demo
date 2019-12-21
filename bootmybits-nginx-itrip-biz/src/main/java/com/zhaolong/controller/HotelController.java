package com.zhaolong.controller;

import com.zhaolong.po.Dto;
import com.zhaolong.po.ItripAreaDic;
import com.zhaolong.po.ItripLabelDic;
import com.zhaolong.service.ITripHotelService;
import com.zhaolong.service.ITripLabelDicService;
import com.zhaolong.service.ItripAreaDicService;
import com.zhaolong.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/api/hotel")
public class HotelController {
    @Autowired
    private ItripAreaDicService itripAreaDicService;
    @Autowired
    private ITripLabelDicService iTripLabelDicService;
    @Autowired
    private ITripHotelService iTripHotelService;

    public ITripHotelService getiTripHotelService() {
        return iTripHotelService;
    }



    public void setiTripHotelService(ITripHotelService iTripHotelService) {
        this.iTripHotelService = iTripHotelService;
    }

    public ITripLabelDicService getiTripLabelDicService() {
        return iTripLabelDicService;
    }

    public void setiTripLabelDicService(ITripLabelDicService iTripLabelDicService) {
        this.iTripLabelDicService = iTripLabelDicService;
    }



    public ItripAreaDicService getItripAreaDicService() {
        return itripAreaDicService;
    }

    public void setItripAreaDicService(ItripAreaDicService itripAreaDicService) {
        this.itripAreaDicService = itripAreaDicService;
    }


    /* @ApiOperation(value = "根据热门城市查询酒店", httpMethod = "POST",
                 protocols = "HTTP", produces = "application/json",
                 response = Dto.class, notes = "根据热门城市查询酒店" +
                 "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                 "<p>错误码: </p>" +
                 "<p>20003: 系统异常,获取失败</p>" +
                 "<p>20004: 城市id不能为空</p>")*/

    //根据热门城市查询酒店
    @RequestMapping(value = "/queryhotcity/{type}")

    public Dto<ItripAreaDicVO> queryHotCity(@PathVariable Integer type) {
        System.out.println("查询热门城市。。。。");
        List<ItripAreaDic> itripAreaDics = null;
        List<ItripAreaDicVO> itripAreaDicVOs = null;
        try {
            if (EmptyUtils.isNotEmpty(type)) {
                Map param = new HashMap();
                param.put("isHot", 1);
                param.put("isChina", type);
                itripAreaDics = itripAreaDicService.getITripAreaDicListByMap(param);
                if (EmptyUtils.isNotEmpty(itripAreaDics)) {
                    itripAreaDicVOs = new ArrayList();
                    for (ItripAreaDic dic : itripAreaDics) {
                        ItripAreaDicVO vo = new ItripAreaDicVO();
                        BeanUtils.copyProperties(dic, vo);
                        itripAreaDicVOs.add(vo);
                    }
                }

            } else {
                DtoUtil.returnFail("type不能为空", "10201");
            }
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", "10202");
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOs);
    }


    /***
     * 根据酒店id查询酒店特色和介绍 -add by donghai
     *
     * @return
     * @throws Exception
     */
  /*  @ApiOperation(value = "根据酒店id查询酒店特色和介绍", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店特色和介绍" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10210: 酒店id不能为空</p>" +
            "<p>10211: 系统异常,获取失败</p>")*/

  //查询酒店特色列表
    @RequestMapping(value = "/queryhotelfeature")
    @ResponseBody
    public Dto<ItripLabelDicVO> queryHotelFeature() {
        System.out.println("查询酒店特色列表。。。。");
        List<ItripLabelDic> itripLabelDics = null;
        List<ItripLabelDicVO> itripAreaDicVOs = null;
        try {
            Map param = new HashMap();
            param.put("parentId", 16);
            itripLabelDics = iTripLabelDicService.getItripLabelDicListByMap(param);
            if (EmptyUtils.isNotEmpty(itripLabelDics)) {
                itripAreaDicVOs = new ArrayList();
                for (ItripLabelDic dic : itripLabelDics) {
                    ItripLabelDicVO vo = new ItripLabelDicVO();
                    BeanUtils.copyProperties(dic, vo);
                    itripAreaDicVOs.add(vo);
                }
            }

        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", "10205");
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOs);
    }

    //根据酒店ID查询酒店特色和介绍  details  细节介绍
    @RequestMapping(value = "/queryhoteldetails/{id}")
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelDetails(@PathVariable Long id) {
        System.out.println("根据酒店id查询特色和介绍....");
        List<ItripSearchDetailsHotelVO> itripSearchDetailsHotelVOList = null;
        try {
            if (EmptyUtils.isNotEmpty(id)) {
                itripSearchDetailsHotelVOList = iTripHotelService.queryHotelDetails(id);
                return DtoUtil.returnDataSuccess(itripSearchDetailsHotelVOList);
            } else {
                return DtoUtil.returnFail("酒店id不能为空", "10210");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10211");
        }

    }


    //根据酒店id查询酒店设施   actility  设施
    @RequestMapping(value = "/queryhotelfacilities/{id}")
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelFacilities(@PathVariable Long id) {
        System.out.println("根据酒店id查询酒店设施....");
        ItripSearchFacilitiesHotelVO itripSearchFacilitiesHotelVO = null;
        try {
            if (EmptyUtils.isNotEmpty(id)) {
                itripSearchFacilitiesHotelVO = iTripHotelService.getItripHotelFacilitiesById(id);
                return DtoUtil.returnDataSuccess(itripSearchFacilitiesHotelVO.getFacilities());
            } else {
                return DtoUtil.returnFail("酒店id不能为空", "10206");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10207");
        }

    }






    /***
     * 根据酒店id查询酒店政策 -add by donghai
     *
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "根据酒店id查询酒店政策", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店政策" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10208: 酒店id不能为空</p>" +
            "<p>10209: 系统异常,获取失败</p>")*/
   //根据酒店ID查询酒店政策
    @RequestMapping(value = "/queryhotelpolicy/{id}")
    public Dto<ItripSearchPolicyHotelVO> queryHotelPolicy(@PathVariable Long id) {
            /*  @ApiParam(required = true, name = "id", value = "酒店ID")*/

        System.out.println("根据酒店id查询酒店政策。。。。");
        ItripSearchPolicyHotelVO itripSearchPolicyHotelVO = null;
        try {
            if (EmptyUtils.isNotEmpty(id)) {
                itripSearchPolicyHotelVO = iTripHotelService.queryHotelPolicy(id);
                return DtoUtil.returnDataSuccess(itripSearchPolicyHotelVO.getHotelPolicy());
            } else {
                return DtoUtil.returnFail("酒店id不能为空", "10208");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10209");
        }
    }



//根据城市查询商圈
    /* @ApiOperation(value = "查询商圈", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据城市查询商圈" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>10203 : cityId不能为空 </p>" +
            "<p>10204 : 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/querytradearea/{cityId}" )
    public Dto<ItripAreaDicVO> queryTradeArea(@PathVariable Long cityId) {
        System.out.println("根据城市查询商圈....");
        List<ItripAreaDic> itripAreaDics = null;
        List<ItripAreaDicVO> itripAreaDicVOs = null;
        try {
            if (EmptyUtils.isNotEmpty(cityId)) {
                Map param = new HashMap();
                param.put("isTradingArea", 1);
                param.put("parent", cityId);
                itripAreaDics = itripAreaDicService.getItripAreaDicListByMap(param);
                if (EmptyUtils.isNotEmpty(itripAreaDics)) {
                    itripAreaDicVOs = new ArrayList();
                    for (ItripAreaDic dic : itripAreaDics) {
                        ItripAreaDicVO vo = new ItripAreaDicVO();
                        BeanUtils.copyProperties(dic, vo);
                        itripAreaDicVOs.add(vo);
                    }
                }
            } else {
                DtoUtil.returnFail("cityId不能为空", "10203");
            }
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常", "10204");
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOs);
    }




    @RequestMapping(value = "/getvideodesc/{hotelId}")

    public Dto<Object> getVideoDescByHotelId(
            @PathVariable String hotelId) {
        /*@ApiParam(required = true, name = "hotelId", value = "酒店ID")*/

        System.out.println("根据酒店id查询酒店商圈....");
        Dto<Object> dto = new Dto<Object>();
        /* logger.debug("getVideoDescByHotelId hotelId : " + hotelId);*/
        if (null != hotelId && !"".equals(hotelId)) {
            HotelVideoDescVO hotelVideoDescVO = null;
            try {
                hotelVideoDescVO = iTripHotelService.getVideoDescByHotelId(Long.valueOf(hotelId));
                System.out.println("视频文字获取成功");
                dto = DtoUtil.returnSuccess("获取酒店视频文字描述成功", hotelVideoDescVO);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取酒店视频文字描述失败", "100214");
            }

        } else {
            dto = DtoUtil.returnFail("酒店id不能为空", "100215");
        }
        return dto;
    }
}




