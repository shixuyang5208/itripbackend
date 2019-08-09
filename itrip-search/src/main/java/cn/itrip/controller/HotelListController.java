package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.vo.hotel.SearchHotCityVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.service.SearchHotelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/hotellist")
public class HotelListController {

    @Resource
    private SearchHotelService searchHotelService;


    @RequestMapping("searchItripHotelPage")
    @ResponseBody
    public Dto searchItripHotelPage(@RequestBody SearchHotelVO vo){
        Page page = null;
        if (EmptyUtils.isEmpty(vo) || EmptyUtils.isEmpty(vo.getDestination())){
            return DtoUtil.returnFail("目的地不能为空","20002");
        }
        try {
            page = searchHotelService.searchHotelPage(vo,vo.getPageNo(),vo.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常，获取酒店信息失败","20001");
        }
        return DtoUtil.returnDataSuccess(page);
    }


    @RequestMapping("searchItripHotelListByHotCity")
    @ResponseBody
    public Dto searchItripHotelListByHotCity(@RequestBody SearchHotCityVO vo){
        if (EmptyUtils.isEmpty(vo) || EmptyUtils.isEmpty(vo.getCityId())){
            return DtoUtil.returnFail("城市ID不能为空","20004");
        }
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("cityId",vo.getCityId());
        try {
            List list = searchHotelService.searchHotelByHotCity(vo.getCityId(),vo.getCount());
            return  DtoUtil.returnDataSuccess(list);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常，查询失败","20001");
        }
    }
}
