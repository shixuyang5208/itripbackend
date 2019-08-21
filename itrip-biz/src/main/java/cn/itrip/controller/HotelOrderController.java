package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.*;
import cn.itrip.beans.vo.order.*;
import cn.itrip.beans.vo.store.StoreVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.common.ValidationToken;
import cn.itrip.service.hotel.ItripHotelService;
import cn.itrip.service.hotelorder.ItripHotelOrderService;
import cn.itrip.service.hotelroom.ItripHotelRoomService;
import cn.itrip.service.hoteltempstore.ItripHotelTempStoreService;
import cn.itrip.service.orderlinkuser.ItripOrderLinkUserService;
import cn.itrip.service.tradeends.ItripTradeEndsService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/hotelorder")
public class HotelOrderController {
    private Logger logger = Logger.getLogger(HotelOrderController.class);
    @Resource
    private ItripTradeEndsService itripTradeEndsService;
    @Resource
    private ValidationToken validationToken;
    @Resource
    private ItripHotelService itripHotelService;
    @Resource
    private ItripHotelRoomService itripHotelRoomService;
    @Resource
    private ItripHotelTempStoreService itripHotelTempStoreService;
    @Resource
    private ItripHotelOrderService itripHotelOrderService;
    @Resource
    private ItripOrderLinkUserService itriporderLinkUserService;
    @Resource


    /**
     *根据条件查询个人订单列表，并分页显示
     * 订单类型(orderType)（-1：全部订单 0:旅游订单 1:酒店订单 2：机票订单）：</p>" +
     * 订单状态(orderStatus)（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）
     * @param vo
     * @param request
     * @return
     */
    @RequestMapping(value = "/getpersonalorderlist", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Object> getPersonalOrderList(@RequestBody ItripSearchOrderVO vo,
                                            HttpServletRequest request) {
        logger.debug("orderType : " + vo.getOrderType());
        logger.debug("orderStatus : " + vo.getOrderStatus());
        String token = request.getHeader("token");
        logger.debug("token is from header:"+token);
        ItripUser currentUser = validationToken.getCurrentUser(token);
        if (EmptyUtils.isNotEmpty(currentUser)){
            if (vo.getOrderType() == null){
                return DtoUtil.returnFail("请传递参数：orderType","100501");
            }
            if (vo.getOrderStatus() == null){
                return DtoUtil.returnFail("请传递参数：orderType","100502");
            }
            Map<String,Object> param = new HashMap<>();
            param.put("orderType",vo.getOrderType() == -1?null:vo.getOrderType());
            param.put("orderStatus",vo.getOrderStatus() == -1?null:vo.getOrderStatus());
            param.put("userId",currentUser.getId());
            param.put("linkUserName",vo.getLinkUserName());
            param.put("startDate",vo.getStartDate());
            param.put("endDate",vo.getEndDate());
            try {
                Page page = itripTradeEndsService.queryItripTradeEndsPageByMap(param,vo.getPageNo(),vo.getPageSize());
                return DtoUtil.returnSuccess("获取个人订单列表成功", page);
            }catch (Exception e){
                e.printStackTrace();
                return DtoUtil.returnFail("获取个人订单列表错误", "100503");
            }
        }else {
            return DtoUtil.returnFail("token失效，请重登录", "100000");
        }
    }


    /**
     * 生成订单前,获取预订信息
     * @param validateRoomStoreVO
     * @param request
     * @return
     */
    @RequestMapping(value = "/getpreorderinfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<RoomStoreVO> getPreOrderInfo(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        ItripUser currentUser = validationToken.getCurrentUser(token);
        ItripHotel itripHotel = null;
        ItripHotelRoom room = null;
        RoomStoreVO roomStoreVO = null;
        try {
            if (EmptyUtils.isEmpty(currentUser)) {
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }else if (EmptyUtils.isEmpty(validateRoomStoreVO.getHotelId())){
                return DtoUtil.returnFail("hotelId不能为空", "100510");
            }else {
                roomStoreVO = new RoomStoreVO();
                itripHotel = itripHotelService.getItripHotelById(validateRoomStoreVO.getHotelId());
                room = itripHotelRoomService.getItripHotelRoomById(validateRoomStoreVO.getRoomId());
                Map param = new HashMap();
                param.put("startTime",validateRoomStoreVO.getCheckInDate());
                param.put("endTime",validateRoomStoreVO.getCheckOutDate());
                param.put("hotelId",validateRoomStoreVO.getHotelId());
                param.put("roomId",validateRoomStoreVO.getRoomId());
                List<StoreVO> storeVOList = itripHotelTempStoreService.queryRoomStore(param);
                roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
                roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
                roomStoreVO.setHotelId(validateRoomStoreVO.getHotelId());
                roomStoreVO.setHotelName(itripHotel.getHotelName());
                roomStoreVO.setRoomId(room.getId());
                roomStoreVO.setPrice(room.getRoomPrice());
                roomStoreVO.setCount(1);
                if (EmptyUtils.isNotEmpty(storeVOList)){
                    roomStoreVO.setStore(storeVOList.get(0).getStore());
                    return DtoUtil.returnSuccess("获取成功",roomStoreVO);
                }else {
                    return DtoUtil.returnFail("暂时无房", "100512");
                }
            }
        }catch (Exception e){
            return DtoUtil.returnFail("系统异常", "100513");
        }
    }


    /**
     * 通过订单ID查看个人订单详情
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/getpersonalorderinfo/{orderId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> getPersonalOrderInfo(@PathVariable Long orderId, HttpServletRequest request){
        logger.debug("orderId : " + orderId);
        String token = request.getHeader("token");
        logger.debug("token name is from header : " + token);
        ItripUser currentUser = validationToken.getCurrentUser(token);
        if (EmptyUtils.isNotEmpty(currentUser)){

        }else {
            return DtoUtil.returnFail("没有相关订单信息", "100526");
        }
        return null;
    }



    /**
     * 根据订单ID获取订单信息
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryOrderById/{orderId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto queryOrderById(@PathVariable Long orderId,HttpServletRequest request){
        logger.debug("orderId : " + orderId);
        String token = request.getHeader("token");
        ItripUser currentUser = validationToken.getCurrentUser(token);
        if (EmptyUtils.isEmpty(currentUser)){
            return DtoUtil.returnFail("token失效，请重登录", "100000");
        }
        try {
            ItripHotelOrder itripHotelOrder = itripHotelOrderService.getItripHotelOrderById(orderId);
            if (EmptyUtils.isEmpty(itripHotelOrder)){
                return DtoUtil.returnFail("100533", "没有查询到相应订单");
            }
            ItripModifyHotelOrderVO orderVO = new ItripModifyHotelOrderVO();
            BeanUtils.copyProperties(itripHotelOrder,orderVO);
            Map<String,Object> param = new HashMap<>();
            param.put("orderId",itripHotelOrder.getId());
            List<ItripOrderLinkUserVo> orderLinkUserList = itriporderLinkUserService.getItripOrderLinkUserListByMap(param);
            orderVO.setItripOrderLinkUserList(orderLinkUserList);
            return DtoUtil.returnSuccess("查询成功",orderVO);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100534");
        }
    }


    /**
     * 通过订单ID查询个人订单详情的房间信息
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "/getpersonalorderroominfo/{orderId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto getPersonalOrderRoomInfo(@PathVariable Long orderId,HttpServletRequest request){
        logger.debug("orderId : " + orderId);
        String token = request.getHeader("token");
        ItripUser currentUser = validationToken.getCurrentUser(token);
        logger.debug("token name is from header : " + token);
        try{
            if (EmptyUtils.isEmpty(currentUser)){
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }else {
                if (orderId == null){
                    return DtoUtil.returnFail("请传递参数：orderId", "100529");
                }
                ItripPersonalOrderRoomVO vo = itripHotelOrderService.getItripHotelOrderRoomInfoById(orderId);
                if (vo == null){
                    return DtoUtil.returnFail("没有相关订单房型信息", "100530");
                }
                return DtoUtil.returnSuccess("获取个人订单房型信息成功", vo);
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("获取个人订单房型信息错误", "100531");
        }
    }




    /**
     * 扫描中间表，执行库存操作
     * @return
     */
    @RequestMapping(value = "/scanTradeEnd", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Object> scanTradeEnd() {
        Map param = new HashMap();
        try{
            param.put("flag",1);
            param.put("oldFlag",0);
            itripTradeEndsService.itriptxModifyItripTradeEnds(param);
            List<ItripTradeEnds> itripTradeEndsList = itripTradeEndsService.getItripTradeEndsListByMap(param);
            if (EmptyUtils.isNotEmpty(itripTradeEndsList)){
                for (ItripTradeEnds ends : itripTradeEndsList){
                    Map<String,Object> orderMap = new HashMap<>();
                    orderMap.put("orderNo",ends.getOrderNo());
                    List<ItripHotelOrder> orderList = itripHotelOrderService.getItripHotelOrderListByMap(orderMap);
                    for (ItripHotelOrder order : orderList){
                        Map<String,Object> roomStoreMap = new HashMap<>();
                        roomStoreMap.put("startTime",order.getCheckInDate());
                        roomStoreMap.put("endTime",order.getCheckOutDate());
                        roomStoreMap.put("count",order.getCount());
                        roomStoreMap.put("roomId",order.getRoomId());
                        itripHotelTempStoreService.updateRoomStore(roomStoreMap);
                    }
                }
                param.put("flag",2);
                param.put("oldFlag",1);
                itripTradeEndsService.itriptxModifyItripTradeEnds(param);
                return DtoUtil.returnSuccess();
            }else {
                return DtoUtil.returnFail("100535", "没有查询到相应记录");
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100536");
        }

    }
}