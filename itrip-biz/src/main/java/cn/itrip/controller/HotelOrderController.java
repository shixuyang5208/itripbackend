package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.vo.order.ItripSearchOrderVO;
import cn.itrip.service.tradeends.ItripTradeEndsService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/hotelorder")
public class HotelOrderController {
    private Logger logger = Logger.getLogger(HotelOrderController.class);
    @Resource
    private ItripTradeEndsService itripTradeEndsService;



    @RequestMapping(value = "/getpersonalorderlist", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Object> getPersonalOrderList(@RequestBody ItripSearchOrderVO vo,
                                            HttpServletRequest request) {
        logger.debug("orderType : " + vo.getOrderType());
        logger.debug("orderStatus : " + vo.getOrderStatus());
        String token = request.getHeader("token");
        logger.debug("token is from header:"+token);

        return null;
    }
}
