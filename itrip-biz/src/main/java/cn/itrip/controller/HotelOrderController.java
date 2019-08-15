package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripHotel;
import cn.itrip.beans.pojo.ItripHotelRoom;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.order.ItripSearchOrderVO;
import cn.itrip.beans.vo.order.RoomStoreVO;
import cn.itrip.beans.vo.order.ValidateRoomStoreVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.common.ValidationToken;
import cn.itrip.service.tradeends.ItripTradeEndsService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/hotelorder")
public class HotelOrderController {
    private Logger logger = Logger.getLogger(HotelOrderController.class);
    @Resource
    private ItripTradeEndsService itripTradeEndsService;
    @Resource
    private ValidationToken validationToken;

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




    @RequestMapping(value = "/getpreorderinfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<RoomStoreVO> getPreOrderInfo(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        ItripUser currentUser = validationToken.getCurrentUser(token);
        ItripHotel itripHotel = null;
        ItripHotelRoom itripHotelRoom = null;
        RoomStoreVO vo = null;
        try {
            if (EmptyUtils.isEmpty(currentUser)) {
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }

        }catch (Exception e){
            return DtoUtil.returnFail("系统异常", "100513");
        }
    }
}
