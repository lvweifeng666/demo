package com.zhaolong.service;

import com.zhaolong.po.ItripHotelOrder;
import com.zhaolong.po.ItripUserLinkUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ITripHotelOrderService {

    //（1）添加订单数据到数据库
    //（2）计算总金额  天数*房间单价*房间数
    //BigDecimal(代表高精度钱的单位)  money保留小数点后2两位  代表一般金额
    //计算总金额
    public BigDecimal getOrderpayAmount(int count,Long roomId)throws  Exception;
    //添加订单数据到数据路
    public Map<String,String> itripadditriphotelOrder(ItripHotelOrder itripHotelOrder, List<ItripUserLinkUser> itripUserLinkUserList) throws Exception;
}
