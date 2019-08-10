package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripAreaDic;
import cn.itrip.beans.vo.ItripAreaDicVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.service.areadic.ItripAreaDicService;
import cn.itrip.service.areadic.ItripAreaDicServiceImpl;
import cn.itrip.service.hoteltradingarea.ItripHotelTradingAreaService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/api/hotel")
public class HotelController {

    private Logger logger = Logger.getLogger(HotelController.class);
    @Resource
    private ItripAreaDicService itripAreaDicService;
    @Resource
    private ItripHotelTradingAreaService tradingAreaService;


    /**
     * 查询热门城市
     * @param type
     * @return
     */
    @RequestMapping(value = "/queryhotcity/{type}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto queryHotCity(@PathVariable Integer type){  //@PathVariable获取的是请求路径中参数的值
        List<ItripAreaDic> listItripAreaDic = null;
        List<ItripAreaDicVO> listItripAreaDicVO = null;
        try {
            if (EmptyUtils.isNotEmpty(type)){
                Map param = new HashMap();
                param.put("isHot",1);
                param.put("isChina",type);
                listItripAreaDic = itripAreaDicService.getItripAreaDicListByMap(param);
                if (EmptyUtils.isNotEmpty(listItripAreaDic)){
                    listItripAreaDicVO = new ArrayList();
                    for (ItripAreaDic dic:listItripAreaDic){
                        ItripAreaDicVO vo = new ItripAreaDicVO();
                        BeanUtils.copyProperties(dic,vo);
                        listItripAreaDicVO.add(vo);
                    }
                }
                return DtoUtil.returnDataSuccess(listItripAreaDicVO);
            }else {
                return DtoUtil.returnFail("type不能为空","10201");
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常，查询失败","10202");
        }
    }

    @RequestMapping(value = "/querytradearea/{citiId}")
    @ResponseBody
    public Dto queryTradeArea(@PathVariable Long cityId){
        return null;
    }
}
