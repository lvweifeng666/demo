package com.zhaolong.service.serviceImpl;

import com.zhaolong.mapper.ItripHotelOrderMapper;
import com.zhaolong.mapper.ItripHotelRoomMapper;
import com.zhaolong.mapper.ItripOrderLinkUserMapper;
import com.zhaolong.po.ItripHotelOrder;
import com.zhaolong.po.ItripOrderLinkUser;
import com.zhaolong.po.ItripUserLinkUser;
import com.zhaolong.service.ITripHotelOrderService;
import com.zhaolong.util.BigDecimalUtil;
import com.zhaolong.util.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_DOWN;

@Service
public class ITripHotelOrderServiceImpl implements ITripHotelOrderService {
  @Autowired
   private ItripHotelOrderMapper itripHotelOrderMapper;
  @Autowired
  private ItripHotelRoomMapper itripHotelRoomMapper;
  @Autowired
  private ItripOrderLinkUserMapper itripOrderLinkUserMapper;

    public ItripOrderLinkUserMapper getItripOrderLinkUserMapper() {
        return itripOrderLinkUserMapper;
    }

    public void setItripOrderLinkUserMapper(ItripOrderLinkUserMapper itripOrderLinkUserMapper) {
        this.itripOrderLinkUserMapper = itripOrderLinkUserMapper;
    }

    public ItripHotelRoomMapper getItripHotelRoomMapper() {
        return itripHotelRoomMapper;
    }

    public void setItripHotelRoomMapper(ItripHotelRoomMapper itripHotelRoomMapper) {
        this.itripHotelRoomMapper = itripHotelRoomMapper;
    }

    public ItripHotelOrderMapper getItripHotelOrderMapper() {
        return itripHotelOrderMapper;
    }

    public void setItripHotelOrderMapper(ItripHotelOrderMapper itripHotelOrderMapper) {
        this.itripHotelOrderMapper = itripHotelOrderMapper;
    }

    @Override
    public BigDecimal getOrderpayAmount(int count, Long roomId) throws Exception {
        BigDecimal payAmount=null;
        BigDecimal roomprice=itripHotelRoomMapper.getItripHotelRoomById(roomId).getRoomPrice();
        payAmount = BigDecimalUtil.OperationASMD(count, roomprice,
                BigDecimalUtil.BigDecimalOprations.multiply,
                2, ROUND_DOWN);
        return payAmount;
    }

    @Override
    public Map<String, String> itripadditriphotelOrder(ItripHotelOrder itripHotelOrder, List<ItripUserLinkUser> itripUserLinkUserList) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        if (null != itripHotelOrder) {
            int flag=0;
            if (EmptyUtils.isNotEmpty(itripHotelOrder.getId())) {
                //删除联系人
                itripOrderLinkUserMapper.deleteItripOrderLinkUserByOrderId(itripHotelOrder.getId());
                itripHotelOrder.setModifyDate(new Date());
                flag=itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);
            } else {
                itripHotelOrder.setCreationDate(new Date());
                flag=itripHotelOrderMapper.insertItripHotelOrder(itripHotelOrder);
            }
            if (flag > 0) {
                Long orderId = itripHotelOrder.getId();
                //添加订单之后还需要往订单与常用联系人关联表中添加记录
                if (orderId > 0) {
                    for (ItripUserLinkUser itripUserLinkUser : itripUserLinkUserList) {
                        ItripOrderLinkUser itripOrderLinkUser = new ItripOrderLinkUser();
                        itripOrderLinkUser.setOrderId(orderId);
                        itripOrderLinkUser.setLinkUserId(itripUserLinkUser.getId());
                        itripOrderLinkUser.setLinkUserName(itripUserLinkUser.getLinkUserName());
                        itripOrderLinkUser.setCreationDate(new Date());
                        itripOrderLinkUser.setCreatedBy(itripHotelOrder.getCreatedBy());
                        itripOrderLinkUserMapper.insertItripOrderLinkUser(itripOrderLinkUser);
                    }
                }
                map.put("id", itripHotelOrder.getId().toString());
                map.put("orderNo", itripHotelOrder.getOrderNo());
                return map;
            }
        }
        return map;
    }
}
