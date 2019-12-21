package com.zhaolong.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhaolong.po.*;
import com.zhaolong.service.ITripHotelOrderService;
import com.zhaolong.service.ITripHotelRoomService;
import com.zhaolong.service.ITripHotelService;
import com.zhaolong.service.ITripHotelTempService;
import com.zhaolong.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hotelorder")
public class ItripHotelOrderController {

    //缓存对象
    private Jedis jedis = new Jedis("127.0.0.1", 6379);


    @Autowired
    private ITripHotelTempService iTripHotelTempService;

    @Autowired
    private ITripHotelService iTripHotelService;


    @Autowired
    private ITripHotelRoomService iTripHotelRoomService;
    @Autowired
    private ITripHotelOrderService iTripHotelOrderService;

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public ITripHotelTempService getiTripHotelTempService() {
        return iTripHotelTempService;
    }

    public void setiTripHotelTempService(ITripHotelTempService iTripHotelTempService) {
        this.iTripHotelTempService = iTripHotelTempService;
    }

    public ITripHotelService getiTripHotelService() {
        return iTripHotelService;
    }

    public void setiTripHotelService(ITripHotelService iTripHotelService) {
        this.iTripHotelService = iTripHotelService;
    }

    public ITripHotelRoomService getiTripHotelRoomService() {
        return iTripHotelRoomService;
    }

    public void setiTripHotelRoomService(ITripHotelRoomService iTripHotelRoomService) {
        this.iTripHotelRoomService = iTripHotelRoomService;
    }

    /* @ApiOperation(value = "生成订单前,获取预订信息", httpMethod = "POST",
                        protocols = "HTTP", produces = "application/json",
                        response = Dto.class, notes = "生成订单前,获取预订信息" +
                        "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                        "<p>错误码：</p>" +
                        "<p>100000 : token失效，请重登录 </p>" +
                        "<p>100510 : hotelId不能为空</p>" +
                        "<p>100511 : roomId不能为空</p>" +
                        "<p>100512 : 暂时无房</p>" +
                        "<p>100513 : 系统异常</p>")*/
    @RequestMapping(value = "/getpreorderinfo")

    public Dto<RoomStoreVO> getPreOrderInfo(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) {
        System.out.println("订单生成前，酒店信息获取展示。。。。");
        //获取用户信息
        String token=jedis.get("token");
        System.out.println("redis的token:"+token);
        System.out.println("客户端的token"+token);
        //通过token获取Json用户对象
        JSONObject jsonObject = JSONObject.parseObject(jedis.get(token).toString());
        //将json字符串转成用户对象
        ItripUser currentUser = (ItripUser) JSONObject.toJavaObject(jsonObject,ItripUser.class);
        //通过酒店id和房型id查询库存。
        //通过酒店id查询
        ItripHotel hotel = null;
        //通过房型id查询
        ItripHotelRoom room = null;
        //返回到前台的对象
        RoomStoreVO roomStoreVO = null;

        try {
            if (EmptyUtils.isEmpty(currentUser)) {
                return DtoUtil.returnFail("token失效，请重登录","100000");
            }
            if (EmptyUtils.isEmpty(validateRoomStoreVO.getHotelId())) {
                return DtoUtil.returnFail("酒店Id不能为空", "100510");
            } else if (EmptyUtils.isEmpty(validateRoomStoreVO.getRoomId())) {
                return DtoUtil.returnFail("房间Id不能为空", "100511");
            } else {
                roomStoreVO = new RoomStoreVO();
            }
            hotel = iTripHotelService.getItripHotelById(validateRoomStoreVO.getHotelId());
            room = iTripHotelRoomService.getItripHotelRoomById(validateRoomStoreVO.getRoomId());
            Map<String,Object> param = new HashMap<>();
            //param.put("startTime", validateRoomStoreVO.getCheckInDate());
            // param.put("endTime", validateRoomStoreVO.getCheckOutDate());
            param.put("roomId", validateRoomStoreVO.getRoomId());
            param.put("hotelId", validateRoomStoreVO.getHotelId());
            roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
            roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
            roomStoreVO.setHotelName(hotel.getHotelName());
            roomStoreVO.setRoomId(room.getId());
            roomStoreVO.setPrice(room.getRoomPrice());
            roomStoreVO.setCount(1);

            System.out.println("map:"+param.get("startTime"));
            System.out.println("map:"+param.get("endTime"));
            System.out.println("map:"+param.get("roomId"));
            System.out.println("map:"+param.get("hotelId"));

            //roomStoreVO.setHotelId(validateRoomStoreVO.getHotelId());
            //List<StoreVO> storeVOList = iTripHotelTempService.queryRoomStore(param);

            Integer so=iTripHotelTempService.queryRoomStoreById(param);
            roomStoreVO.setStore(so);
            return DtoUtil.returnSuccess("获取成功", roomStoreVO);

        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100513");
        }

    }



    @RequestMapping(value = "/addhotelorder")
    public Dto<Object> addHotelOrder(@RequestBody ItripAddHotelOrderVO itripAddHotelOrderVO, HttpServletRequest request) {
        Dto<Object> dto = new Dto<Object>();
        System.out.println("生成未付款订单。。。。");
       /* //获取redis中存储的key值---token
        String tokenstr=jedis.get("token");
        //将key值token（pc-XXX）的value转化为json字符串
        String jsontoken=jedis.get(tokenstr).toString();
        System.out.println("objectjson:"+jsontoken);
        JSONObject jsonObject = JSONObject.parseObject(jedis.get(tokenstr).toString());
        //将json字符串转成用户对象
        ItripUser currentUser = (ItripUser) JSONObject.toJavaObject(jsonObject,ItripUser.class);*/
        String token=request.getHeader("token");
        System.out.println("token:"+token);
        JSONObject jsonObject = JSONObject.parseObject(jedis.get(token).toString());

        //将json字符串转成用户对象
        ItripUser currentUser = (ItripUser) JSONObject.toJavaObject(jsonObject,ItripUser.class);
        /**
         * 条件字符串
         * startTime 开始时间
         * endTime 结束时间
         * roomId 房间ID
         * count 预订数目
         * */
        Map<String, Object> validateStoreMap = new HashMap<String, Object>();
        validateStoreMap.put("startTime", itripAddHotelOrderVO.getCheckInDate());
        validateStoreMap.put("endTime", itripAddHotelOrderVO.getCheckOutDate());
        validateStoreMap.put("hotelId", itripAddHotelOrderVO.getHotelId());
        validateStoreMap.put("roomId", itripAddHotelOrderVO.getRoomId());
        validateStoreMap.put("count", itripAddHotelOrderVO.getCount());
        List<ItripUserLinkUser> linkUserList = itripAddHotelOrderVO.getLinkUser();
        if(EmptyUtils.isEmpty(currentUser)){
            return DtoUtil.returnFail("token失效，请重登录", "100000");
        }
        try {
            //判断库存是否充足
            Boolean flag =true; //ItripHotelTempStoreService.validateRoomStore(validateStoreMap);
            if (flag && null != itripAddHotelOrderVO) {
                //计算订单的预定天数
                Integer days = DateUtil.getBetweenDates(
                        itripAddHotelOrderVO.getCheckInDate(), itripAddHotelOrderVO.getCheckOutDate()
                ).size()-1;
                if(days<=0){
                    return DtoUtil.returnFail("退房日期必须大于入住日期", "100505");
                }
                ItripHotelOrder itripHotelOrder = new ItripHotelOrder();
                itripHotelOrder.setId(itripAddHotelOrderVO.getId());
                itripHotelOrder.setUserId(currentUser.getId());
                itripHotelOrder.setOrderType(itripAddHotelOrderVO.getOrderType());
                itripHotelOrder.setHotelId(itripAddHotelOrderVO.getHotelId());
                itripHotelOrder.setHotelName(itripAddHotelOrderVO.getHotelName());
                itripHotelOrder.setRoomId(itripAddHotelOrderVO.getRoomId());
                itripHotelOrder.setCount(itripAddHotelOrderVO.getCount());
                itripHotelOrder.setCheckInDate(itripAddHotelOrderVO.getCheckInDate());
                itripHotelOrder.setCheckOutDate(itripAddHotelOrderVO.getCheckOutDate());
                itripHotelOrder.setNoticePhone(itripAddHotelOrderVO.getNoticePhone());
                itripHotelOrder.setNoticeEmail(itripAddHotelOrderVO.getNoticeEmail());
                itripHotelOrder.setSpecialRequirement(itripAddHotelOrderVO.getSpecialRequirement());
                itripHotelOrder.setIsNeedInvoice(itripAddHotelOrderVO.getIsNeedInvoice());
                itripHotelOrder.setInvoiceHead(itripAddHotelOrderVO.getInvoiceHead());
                itripHotelOrder.setInvoiceType(itripAddHotelOrderVO.getInvoiceType());
                itripHotelOrder.setCreatedBy(currentUser.getId());
                StringBuilder linkUserName = new StringBuilder();
                int size = linkUserList.size();
                for (int i = 0; i < size; i++) {
                    if (i != size - 1) {
                        linkUserName.append(linkUserList.get(i).getLinkUserName() + ",");
                    } else {
                        linkUserName.append(linkUserList.get(i).getLinkUserName());
                    }
                }
                itripHotelOrder.setLinkUserName(linkUserName.toString());
                itripHotelOrder.setBookingDays(days);
                if (token.startsWith("token:PC")) {
                    itripHotelOrder.setBookType(0);
                } else if (token.startsWith("token:MOBILE")) {
                    itripHotelOrder.setBookType(1);
                } else {
                    itripHotelOrder.setBookType(2);
                }
                //支付之前生成的订单的初始状态为未支付
                itripHotelOrder.setOrderStatus(0);
                try {
                    //生成订单号：机器码 +日期+（MD5）（商品IDs+毫秒数+1000000的随机数）
                    StringBuilder md5String = new StringBuilder();
                    md5String.append(itripHotelOrder.getHotelId());
                    md5String.append(itripHotelOrder.getRoomId());
                    md5String.append(System.currentTimeMillis());
                    md5String.append(Math.random() * 1000000);
                    String md5 = MD5.getMd5(md5String.toString(), 6);

                    //生成订单编号
                    StringBuilder orderNo = new StringBuilder();
                    orderNo.append("D1000001");
                    orderNo.append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
                    orderNo.append(md5);
                    itripHotelOrder.setOrderNo(orderNo.toString());
                    //计算订单的总金额
                    itripHotelOrder.setPayAmount(iTripHotelOrderService.getOrderpayAmount(days * itripAddHotelOrderVO.getCount(), itripAddHotelOrderVO.getRoomId()));

                    Map<String, String> map = iTripHotelOrderService.itripadditriphotelOrder(itripHotelOrder, linkUserList);

                    DtoUtil.returnSuccess();
                    dto = DtoUtil.returnSuccess("生成订单成功", map);
                } catch (Exception e) {
                    e.printStackTrace();
                    dto = DtoUtil.returnFail("生成订单失败", "100505");
                }
            } else if (flag && null == itripAddHotelOrderVO) {
                dto = DtoUtil.returnFail("不能提交空，请填写订单信息", "100506");
            } else {
                dto = DtoUtil.returnFail("库存不足", "100507");
            }
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100508");
        }
    }

}

