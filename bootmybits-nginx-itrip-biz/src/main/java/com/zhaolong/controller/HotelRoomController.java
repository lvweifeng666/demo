package com.zhaolong.controller;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.zhaolong.po.Dto;
import com.zhaolong.po.ItripHotelRoom;
import com.zhaolong.service.ITripHotelRoomService;
import com.zhaolong.service.ITripImageService;
import com.zhaolong.service.ITripLabelDicService;
import com.zhaolong.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/api/hotelroom")
public class HotelRoomController {
    @Autowired
    private ITripLabelDicService iTripLabelDicService;
    @Autowired
    private ITripHotelRoomService iTripHotelRoomService;


    @Autowired
    private ITripImageService iTripImageService;

    public ITripLabelDicService getiTripLabelDicService() {
        return iTripLabelDicService;
    }

    public void setiTripLabelDicService(ITripLabelDicService iTripLabelDicService) {
        this.iTripLabelDicService = iTripLabelDicService;
    }

    public ITripHotelRoomService getiTripHotelRoomService() {
        return iTripHotelRoomService;
    }

    public void setiTripHotelRoomService(ITripHotelRoomService iTripHotelRoomService) {
        this.iTripHotelRoomService = iTripHotelRoomService;
    }


    public ITripImageService getiTripImageService() {
        return iTripImageService;
    }

    public void setiTripImageService(ITripImageService iTripImageService) {
        this.iTripImageService = iTripImageService;
    }

    /* @ApiOperation(value = "查询酒店房间床型列表", httpMethod = "GET",
                        protocols = "HTTP", produces = "application/json",
                        response = Dto.class, notes = "查询酒店床型列表" +
                        "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                        "<p>错误码：</p>" +
                        "<p>100305 : 获取酒店房间床型失败</p>")*/
    @RequestMapping(value = "/queryhotelroombed")

    public Dto<Object> queryHotelRoomBed() {
        System.out.println("查询酒店房间床型列表.....");
        try {
            List<ItripLabelDicVO> itripLabelDicList = iTripLabelDicService.getItripLabelDicByParentId(new Long(1));
            return DtoUtil.returnSuccess("获取成功", itripLabelDicList);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取床型失败", "100305");
        }
    }

    /*@ApiOperation(value = "查询酒店房间列表", httpMethod = "POST",
                protocols = "HTTP", produces = "application/json",
                response = Dto.class, notes = "查询酒店房间列表" +
                "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                "<p>错误码：</p>" +
                "<p>100303 : 酒店id不能为空,酒店入住及退房时间不能为空,入住时间不能大于退房时间</p>" +
                "<p>100304 : 系统异常</p>")*/
    @RequestMapping(value = "/queryhotelroombyhotel")
    public Dto<List<ItripHotelRoomVO>> queryHotelRoomByHotel(@RequestBody SearchHotelRoomVO vo) throws Exception {
        System.out.println("查询酒店房间列表....注意修改了mapper中集合判null");
        List<List<ItripHotelRoomVO>> hotelRoomVOList = null;
        try {
            //条件集合
            Map<String, Object> param = new HashMap<>();
            if (EmptyUtils.isEmpty(vo.getHotelId())) {
                return DtoUtil.returnFail("酒店id不能为空", "100303");
            }
            if (EmptyUtils.isEmpty(vo.getStartDate()) || EmptyUtils.isEmpty(vo.getEndDate())) {
                return DtoUtil.returnFail("必须填写酒店入住及退房时间", "100303");
            }
            if (EmptyUtils.isNotEmpty(vo.getStartDate()) && EmptyUtils.isNotEmpty(vo.getEndDate())) {
                if (vo.getStartDate().getTime() > vo.getEndDate().getTime()) {
                    System.out.println("入住之间必须大于退房时间");
                    return DtoUtil.returnFail("入住时间必须大于退房时间", "100303");

                }
                List<Date> dates = DateUtil.getBetweenDates(vo.getStartDate(), vo.getEndDate());
                param.put("timesList", dates);

            }
            vo.setIsHavingBreakfast(EmptyUtils.isEmpty(vo.getIsHavingBreakfast()) ? null : vo.getIsHavingBreakfast());
            vo.setIsBook(EmptyUtils.isEmpty(vo.getIsBook()) ? null : vo.getIsBook());
            vo.setIsTimelyResponse(EmptyUtils.isEmpty(vo.getIsTimelyResponse()) ? null : vo.getIsTimelyResponse());
            vo.setIsCancel(EmptyUtils.isEmpty(vo.getIsCancel()) ? null : vo.getIsCancel());
            vo.setPayType(EmptyUtils.isEmpty(vo.getPayType()) ? null : vo.getPayType());

            param.put("hotelId", vo.getHotelId());
            param.put("isBook", vo.getIsBook());
            param.put("isHavingBreakfast", vo.getIsHavingBreakfast());
            param.put("roomBedTypeId", vo.getRoomBedTypeId());
            param.put("isCancel", vo.getIsCancel());
            if (EmptyUtils.isEmpty(vo.getPayType()) || vo.getPayType() == 3) {
                param.put("payType", null);

            } else {
                param.put("payType", vo.getPayType());
            }
            System.out.println("酒店房型传入MAP" + param.toString());

            List<ItripHotelRoomVO> originalRoomList = iTripHotelRoomService.getItripHotelRoomListByMap(param);
            hotelRoomVOList = new ArrayList();
            for (ItripHotelRoomVO roomVO : originalRoomList) {
                List<ItripHotelRoomVO> tempList = new ArrayList<ItripHotelRoomVO>();
                tempList.add(roomVO);
                hotelRoomVOList.add(tempList);
            }
            return DtoUtil.returnSuccess("获取成功", hotelRoomVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取酒店房型列表失败", "100304");
        }
    }
    @RequestMapping(value = "/getimg/{targetId}")

    public Dto<Object> getImgByTargetId(@PathVariable String targetId) {
        System.out.println("根据targetId查询酒店房型图片...");
        Dto<Object> dto = null;
        if (null != targetId && !"".equals(targetId)) {
            List<ItripImageVO> itripImageVOList = null;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("type", "1");
            param.put("targetId", targetId);
            try {
                itripImageVOList = iTripImageService.getItripImageListByMap(param);
                dto = DtoUtil.returnSuccess("获取酒店图片房型成功", itripImageVOList);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取酒店房型图片失败", "100301");
            }
        } else {
            dto = DtoUtil.returnFail("酒店房型id不能为空", "100302");
        }
        return dto;
    }
}