package cn.itrip.service.hotelorder;

import cn.itrip.beans.pojo.ItripHotelOrder;
import cn.itrip.beans.pojo.ItripOrderLinkUser;
import cn.itrip.beans.pojo.ItripUserLinkUser;
import cn.itrip.beans.vo.order.ItripListHotelOrderVO;
import cn.itrip.beans.vo.order.ItripPersonalOrderRoomVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.hotelorder.ItripHotelOrderMapper;

import cn.itrip.dao.hoteltempstore.ItripHotelTempStoreMapper;
import cn.itrip.dao.orderlinkuser.ItripOrderLinkUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItripHotelOrderServiceImpl implements ItripHotelOrderService {
    @Resource
    private ItripHotelOrderMapper itripHotelOrderMapper;
    @Resource
    private ItripHotelTempStoreMapper itripHotelTempStoreMapper;
    @Resource
    private ItripOrderLinkUserMapper itripOrderLinkUserMapper;

    @Override
    public ItripHotelOrder getItripHotelOrderById(Long id) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderById(id);
    }

    @Override
    public List<ItripHotelOrder> getItripHotelOrderListByMap(Map<String, Object> param) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderListByMap(param);
    }

    @Override
    public Integer getItripHotelOrderCountByMap(Map<String, Object> param) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderCountByMap(param);
    }

    @Override
    public Map<String, String> itriptxAddItripHotelOrder(ItripHotelOrder itripHotelOrder, List<ItripUserLinkUser> userLinkUserList) throws Exception {
        Map map = new HashMap();
        if (itripHotelOrder != null){
            int flag = 0;
            if (EmptyUtils.isNotEmpty(itripHotelOrder.getId())){
                /**
                 * 订单ID存在的情况下，说明是已经生成过的订单信息（即需要修改订单信息），
                 * 为避免后续增删修改订单联系人麻烦，故先将订单联系人全部删除，后续选增需要的联系人即可
                 */
                itripOrderLinkUserMapper.deleteItripOrderLinkUserById(itripHotelOrder.getId());
                itripHotelOrder.setCreationDate(new Date());
                flag = itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);
            }else {
                itripHotelOrder.setCreationDate(new Date());
                flag = itripHotelOrderMapper.insertItripHotelOrder(itripHotelOrder);
            }
            if (flag > 0){
                if (itripHotelOrder.getId() > 0){
                    ItripOrderLinkUser itripOrderLinkUser = new ItripOrderLinkUser();
                    itripOrderLinkUser.setOrderId(itripHotelOrder.getId());
                    itripOrderLinkUser.setCreationDate(new Date());
                    itripOrderLinkUser.setCreatedBy(itripHotelOrder.getCreatedBy());
                    for (ItripUserLinkUser userLinkUser:userLinkUserList){
                        itripOrderLinkUser.setLinkUserId(userLinkUser.getId());
                        itripOrderLinkUser.setLinkUserName(userLinkUser.getLinkUserName());
                        itripOrderLinkUserMapper.insertItripOrderLinkUser(itripOrderLinkUser);
                    }
                }
                map.put("id",String.valueOf(itripHotelOrder.getId()));
                map.put("orderNo",itripHotelOrder.getOrderNo());
            }
        }
        return null;
    }

    @Override
    public Integer itriptxModifyItripHotelOrder(ItripHotelOrder itripHotelOrder) throws Exception {
        ItripHotelOrder order = itripHotelOrderMapper.getItripHotelOrderById(itripHotelOrder.getId());
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("roomId",order.getRoomId());
        param.put("count",order.getCount());
        param.put("checkInDate",order.getCheckInDate());
        param.put("checkOutDate",order.getCheckOutDate());
        itripHotelTempStoreMapper.updateRoomStore(param);
        return itripHotelOrderMapper.updateItripHotelOrder(itripHotelOrder);
    }

    @Override
    public Integer itriptxDeleteItripHotelOrderById(Long id) throws Exception {
        return itripHotelOrderMapper.deleteItripHotelOrderById(id);
    }

    @Override
    public Page<ItripHotelOrder> queryItripHotelOrderPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripHotelOrderMapper.getItripHotelOrderCountByMap(param);
        pageNo = EmptyUtils.isNotEmpty(pageNo)?pageNo: Constants.DEFAULT_PAGE_NO;
        pageSize = EmptyUtils.isNotEmpty(pageSize)?pageSize:Constants.DEFAULT_PAGE_SIZE;
        Page page = new Page(pageNo,pageSize,total);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",pageSize);
        List list = this.getItripHotelOrderListByMap(param);
        page.setRows(list);
        return page;
    }

    @Override
    public boolean updateHotelOrderStatus(Long id) throws Exception {
        return false;
    }

    @Override
    public int getRoomNumByRoomIdTypeAndDate(Integer roomId, String startDate, String endDate) throws Exception {
        return 0;
    }

    @Override
    public BigDecimal getOrderPayAmount(int count, Long roomId) throws Exception {
        return null;
    }

    @Override
    public Page<ItripListHotelOrderVO> queryOrderPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        return null;
    }

    @Override
    public boolean flushOrderStatus(Integer type) throws Exception {
        return false;
    }

    @Override
    public ItripPersonalOrderRoomVO getItripHotelOrderRoomInfoById(Long orderId) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderRoomInfoById(orderId);
    }
}
