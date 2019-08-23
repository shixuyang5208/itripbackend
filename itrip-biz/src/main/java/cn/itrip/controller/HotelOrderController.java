package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.*;
import cn.itrip.beans.vo.order.*;
import cn.itrip.beans.vo.store.StoreVO;
import cn.itrip.common.*;
import cn.itrip.service.hotel.ItripHotelService;
import cn.itrip.service.hotelorder.ItripHotelOrderService;
import cn.itrip.service.hotelroom.ItripHotelRoomService;
import cn.itrip.service.hoteltempstore.ItripHotelTempStoreService;
import cn.itrip.service.orderlinkuser.ItripOrderLinkUserService;
import cn.itrip.service.tradeends.ItripTradeEndsService;
import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    private SystemConfig systemConfig;

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
     * 修改订房日期验证是否有房
     * @param roomStoreVO
     * @param request
     * @return
     */
    @RequestMapping(value = "/validateroomstore", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Map<String, Boolean>> validateRoomStore(@RequestBody ValidateRoomStoreVO roomStoreVO,HttpServletRequest request){
        String tokenString = request.getHeader("token");
        ItripUser currentUser = validationToken.getCurrentUser(tokenString);
        try {
            if (EmptyUtils.isEmpty(currentUser)) {
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }
            if (EmptyUtils.isEmpty(roomStoreVO.getHotelId())){
                return DtoUtil.returnFail("hotelId不能为空", "100515");
            } else if(EmptyUtils.isEmpty(roomStoreVO.getRoomId())){
                return DtoUtil.returnFail("roomId不能为空", "100516");
            }else {
                Map param = new HashMap();
                param.put("startTime",roomStoreVO.getCheckInDate());
                param.put("endTime",roomStoreVO.getCheckOutDate());
                param.put("roomId",roomStoreVO.getRoomId());
                param.put("hotelId",roomStoreVO.getHotelId());
                param.put("count",roomStoreVO.getCount());
                boolean flag = itripHotelTempStoreService.validateRoomStore(param);
                Map<String,Boolean> map = new HashMap<>();
                map.put("flag",flag);
                return DtoUtil.returnSuccess("操作成功",map);
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100517");
        }
    }


    /**
     * 生成订单
     * @param addHotelOrderVO
     * @param request
     * @return
     */
    @RequestMapping(value = "/addhotelorder", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Object> addHotelOrder(@RequestBody ItripAddHotelOrderVO addHotelOrderVO,HttpServletRequest request){
        String token = request.getHeader("token");
        ItripUser currentUser = validationToken.getCurrentUser(token);
        logger.debug("token name is from header : " + token);
        Map<String,Object> validateStoreMap = new HashMap<>();
        validateStoreMap.put("hotelId",addHotelOrderVO.getHotelId());
        validateStoreMap.put("roomId",addHotelOrderVO.getRoomId());
        validateStoreMap.put("startTime",addHotelOrderVO.getCheckInDate());
        validateStoreMap.put("endTime",addHotelOrderVO.getCheckOutDate());
        validateStoreMap.put("count",addHotelOrderVO.getCount());
        List<ItripUserLinkUser> userLinkUserList = addHotelOrderVO.getLinkUser();
        if (EmptyUtils.isEmpty(currentUser)) {
            return DtoUtil.returnFail("token失效，请重登录", "100000");
        }
        try {
            //判断是否有库存
            Boolean flag = itripHotelTempStoreService.validateRoomStore(validateStoreMap);
            if (flag && EmptyUtils.isNotEmpty(addHotelOrderVO)){
                //计算预订天数
                Integer days = DateUtil.getBetweenDates(addHotelOrderVO.getCheckInDate(),addHotelOrderVO.getCheckOutDate()).size()-1;
                if (days <= 0){
                    return DtoUtil.returnFail("退房日期必须大于入住日期", "100505");
                }
                ItripHotelOrder itripHotelOrder = new ItripHotelOrder();
                itripHotelOrder.setId(addHotelOrderVO.getId());
                itripHotelOrder.setUserId(currentUser.getId());
                itripHotelOrder.setOrderType(addHotelOrderVO.getOrderType());
                itripHotelOrder.setHotelId(addHotelOrderVO.getHotelId());
                itripHotelOrder.setHotelName(addHotelOrderVO.getHotelName());
                itripHotelOrder.setRoomId(addHotelOrderVO.getRoomId());
                itripHotelOrder.setCount(addHotelOrderVO.getCount());
                itripHotelOrder.setCheckInDate(addHotelOrderVO.getCheckInDate());
                itripHotelOrder.setCheckOutDate(addHotelOrderVO.getCheckOutDate());
                itripHotelOrder.setNoticePhone(addHotelOrderVO.getNoticePhone());
                itripHotelOrder.setNoticeEmail(addHotelOrderVO.getNoticeEmail());
                itripHotelOrder.setSpecialRequirement(addHotelOrderVO.getSpecialRequirement());
                itripHotelOrder.setIsNeedInvoice(addHotelOrderVO.getIsNeedInvoice());
                itripHotelOrder.setInvoiceHead(addHotelOrderVO.getInvoiceHead());
                itripHotelOrder.setInvoiceType(addHotelOrderVO.getInvoiceType());
                itripHotelOrder.setCreatedBy(currentUser.getId());

                //添加订单联系人
                StringBuffer userLinkUserName = new StringBuffer();
                for (int i = 0;i<userLinkUserList.size();i++){
                    if (i != userLinkUserList.size()-1){
                        userLinkUserName.append(userLinkUserList.get(i).getLinkUserName()+",");
                    }else {
                        userLinkUserName.append(userLinkUserList.get(i).getLinkUserName());
                    }
                }
                itripHotelOrder.setLinkUserName(userLinkUserName.toString());
                //预订天数
                itripHotelOrder.setBookingDays(days);
                //预订类型：0:WEB端 1:手机端 2:其他客户端
                if (token.startsWith("token:PC")){
                    itripHotelOrder.setBookType(0);
                }else if (token.startsWith("token:MOBILE")){
                    itripHotelOrder.setBookType(1);
                }else {
                    itripHotelOrder.setBookType(2);
                }
                //初始订单状态为默认值0：未支付
                itripHotelOrder.setOrderStatus(0);
                //【MD5】（商品IDs+毫秒数+百万随机数）
                StringBuffer md5 = new StringBuffer();
                md5.append(itripHotelOrder.getHotelId());
                md5.append(itripHotelOrder.getRoomId());
                md5.append(System.currentTimeMillis());
                md5.append(Math.random()*1000000);
                String md5No = MD5.getMd5(String.valueOf(md5),6);
                //生成订单编号:【机器码】 +【日期】+【MD5】
                StringBuffer orderNo = new StringBuffer();
                orderNo.append(systemConfig.getMachineCode());
                orderNo.append(DateUtil.format(new Date(),"yyyyMMddHHmmss"));
                orderNo.append(md5No);
                itripHotelOrder.setOrderNo(String.valueOf(orderNo));

                //订单总金额
                itripHotelOrder.setPayAmount(itripHotelOrderService.getOrderPayAmount(days*addHotelOrderVO.getCount(),itripHotelOrder.getRoomId()));
                Map param = itripHotelOrderService.itriptxAddItripHotelOrder(itripHotelOrder,userLinkUserList);
                return DtoUtil.returnSuccess("生成订单成功！",param);
            }else if (flag && EmptyUtils.isEmpty(addHotelOrderVO)){
                return DtoUtil.returnFail("不能提交空，请填写订单信息", "100506");
            }else {
                return DtoUtil.returnFail("库存不足","100507");
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100508");
        }
    }


    @RequestMapping(value = "/querysuccessorderinfo/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<Map<String, Boolean>> querySuccessOrderInfo(@PathVariable Long id,HttpServletRequest request){
        String token = request.getHeader("token");
        ItripUser currentUser = validationToken.getCurrentUser(token);
        if (EmptyUtils.isEmpty(currentUser)){
            return DtoUtil.returnFail("token失效请重新登录","100000");
        }
        if (EmptyUtils.isEmpty(id)){
            return DtoUtil.returnFail("id不能为空", "100519");
        }
        try {
            ItripHotelOrder order = itripHotelOrderService.getItripHotelOrderById(id);
            if (EmptyUtils.isEmpty(order)){
                return DtoUtil.returnFail("没有查询到相应订单", "100519");
            }
            ItripHotelRoom room = itripHotelRoomService.getItripHotelRoomById(order.getRoomId());
            Map resultMap = new HashMap();
            resultMap.put("id",order.getId());
            resultMap.put("orderNo",order.getOrderNo());
            resultMap.put("payType",order.getPayType());
            resultMap.put("payAmount",order.getPayAmount());
            resultMap.put("roomTitle",room.getRoomTitle());
            return DtoUtil.returnSuccess("获取数据成功", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取数据失败", "100520");
        }
    }


    /**
     * 修改订单的支付方式和状态
     * @param itripModifyHotelOrderVO
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateorderstatusandpaytype", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Map<String, Boolean>> updateOrderStatusAndPayType(@RequestBody ItripModifyHotelOrderVO itripModifyHotelOrderVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        logger.debug("token name is from header : " + token);
        ItripUser currentUser = validationToken.getCurrentUser(token);
        if (currentUser != null && itripModifyHotelOrderVO != null){
            ItripHotelOrder itripHotelOrder = new ItripHotelOrder();
            itripHotelOrder.setId(itripModifyHotelOrderVO.getId());
            //设置支付状态为2：完成支付
            itripHotelOrder.setOrderStatus(2);
            itripHotelOrder.setPayType(itripModifyHotelOrderVO.getPayType());
            itripHotelOrder.setModifiedBy(currentUser.getId());
            itripHotelOrder.setModifyDate(new Date());
            try {
                itripHotelOrderService.itriptxModifyItripHotelOrder(itripHotelOrder);
                return DtoUtil.returnSuccess("修改订单成功");
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("修改订单失败", "100522");
            }
        }else if (currentUser != null && itripModifyHotelOrderVO == null){
            return DtoUtil.returnFail("不能提交空，请填写订单信息", "100523");
        }else {
            return DtoUtil.returnFail("token失效请重新登录","100000");
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
            if (orderId == null){
                return DtoUtil.returnFail("请传递参数：orderId", "100525");
            }
            try {
                ItripHotelOrder order = itripHotelOrderService.getItripHotelOrderById(orderId);
                if (order != null){
                    ItripPersonalHotelOrderVO itripPersonalHotelOrderVO = new ItripPersonalHotelOrderVO();
                    itripPersonalHotelOrderVO.setId(order.getId());
                    itripPersonalHotelOrderVO.setOrderNo(order.getOrderNo());
                    itripPersonalHotelOrderVO.setCreationDate(order.getCreationDate());
                    itripPersonalHotelOrderVO.setBookType(order.getBookType());
                    itripPersonalHotelOrderVO.setOrderStatus(order.getOrderStatus());
                    itripPersonalHotelOrderVO.setPayAmount(order.getPayAmount());
                    itripPersonalHotelOrderVO.setPayType(order.getPayType());
                    itripPersonalHotelOrderVO.setNoticePhone(order.getNoticePhone());
                    //房间预订信息
                    ItripHotelRoom room = itripHotelRoomService.getItripHotelRoomById(order.getRoomId());
                    if (EmptyUtils.isNotEmpty(room)){
                        itripPersonalHotelOrderVO.setRoomPayType(room.getPayType());
                    }
                    switch (order.getOrderStatus()){
                        case 0:
                            itripPersonalHotelOrderVO.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessCancel()));
                            itripPersonalHotelOrderVO.setProcessNode("3");
                            break;
                        case 1:
                            itripPersonalHotelOrderVO.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
                            itripPersonalHotelOrderVO.setProcessNode("2");
                            break;
                        case 2:
                            itripPersonalHotelOrderVO.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
                            itripPersonalHotelOrderVO.setProcessNode("3");
                            break;
                        case 3:
                            itripPersonalHotelOrderVO.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
                            itripPersonalHotelOrderVO.setProcessNode("5");
                            break;
                        case 4:
                            itripPersonalHotelOrderVO.setOrderProcess(JSONArray.parse(systemConfig.getOrderProcessOK()));
                            itripPersonalHotelOrderVO.setProcessNode("6");
                            break;
                    }
                    return DtoUtil.returnSuccess("获取个人订单信息成功", itripPersonalHotelOrderVO);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("获取个人订单信息错误", "100527");
            }
        }else {
            return DtoUtil.returnFail("token失效，请重登录", "100000");
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
            BeanUtils.copyProperties(orderVO,itripHotelOrder);
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