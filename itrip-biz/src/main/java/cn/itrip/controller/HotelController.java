package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripAreaDic;
import cn.itrip.beans.pojo.ItripLabelDic;
import cn.itrip.beans.vo.ItripAreaDicVO;
import cn.itrip.beans.vo.ItripImageVO;
import cn.itrip.beans.vo.ItripLabelDicVO;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.beans.vo.hotel.ItripSearchDetailsHotelVO;
import cn.itrip.beans.vo.hotel.ItripSearchFacilitiesHotelVO;
import cn.itrip.beans.vo.hotel.ItripSearchPolicyHotelVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.service.areadic.ItripAreaDicService;
import cn.itrip.service.areadic.ItripAreaDicServiceImpl;
import cn.itrip.service.hotel.ItripHotelService;
import cn.itrip.service.hoteltradingarea.ItripHotelTradingAreaService;
import cn.itrip.service.image.ItripImageService;
import cn.itrip.service.labeldic.ItripLabelDicService;
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
    @Resource
    private ItripLabelDicService labelDicService;
    @Resource
    private ItripHotelService itripHotelService;
    @Resource
    private ItripImageService itripImageService;

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

    /**
     * 查询特定城市的商圈
     * @param cityId
     * @return
     */
    @RequestMapping(value = "/querytradearea/{cityId}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto queryTradeArea(@PathVariable Long cityId){
        List<ItripAreaDic> listItripAreaDic = null;
        List<ItripAreaDicVO> listItripAreaDicVO = null;
        try {
            if (EmptyUtils.isNotEmpty(cityId)) {
                Map param = new HashMap();
                param.put("isTradingArea", 1);
                param.put("cityId", cityId);
                listItripAreaDic = itripAreaDicService.getItripAreaDicListByMap(param);
                if (EmptyUtils.isNotEmpty(listItripAreaDic)) {
                    listItripAreaDicVO = new ArrayList();
                    for (ItripAreaDic dic : listItripAreaDic) {
                        ItripAreaDicVO vo = new ItripAreaDicVO();
                        BeanUtils.copyProperties(dic, vo);
                        listItripAreaDicVO.add(vo);
                    }
                }
                return DtoUtil.returnDataSuccess(listItripAreaDicVO);
            }else {
                return DtoUtil.returnFail("cityId不能为空","10203");
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常，查询失败","10204");
        }
    }


    /**
     * 查询酒店所有特色列表
     * @return
     */
    @RequestMapping(value = "/queryhotelfeature",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto queryHotelFeature(){
        List<ItripLabelDic> itripLabelDics = null;
        List<ItripLabelDicVO> itripLabelDicVOS = null;
        try {
            Map param = new HashMap();
            param.put("parent", 16);
            itripLabelDics = labelDicService.getItripLabelDicListByMap(param);
            if (EmptyUtils.isNotEmpty(itripLabelDics)) {
                itripLabelDicVOS = new ArrayList();
                for (ItripLabelDic dic : itripLabelDics) {
                    ItripLabelDicVO vo = new ItripLabelDicVO();
                    BeanUtils.copyProperties(dic, vo);
                    itripLabelDicVOS.add(vo);
                }
            }
            return DtoUtil.returnDataSuccess(itripLabelDicVOS);
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常，查询失败","10205");
        }
    }


    /**
     * 通过酒店ID查询酒店设施
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryhotelfacilities/{id}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto queryHotelFacilities(@PathVariable Long id){
        ItripSearchFacilitiesHotelVO searchFacilitiesHotelVO = null;
        try {
            if (EmptyUtils.isNotEmpty(id)){
                searchFacilitiesHotelVO = itripHotelService.getItripHotelFacilitiesById(id);
                return DtoUtil.returnDataSuccess(searchFacilitiesHotelVO.getFacilities());
            }else {
                return DtoUtil.returnFail("酒店ID不能为空","10206");
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常","10207");
        }
    }


    /**
     * 通过酒店ID查询酒店政策
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryhotelpolicy/{id}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto queryHotelPolicy(@PathVariable Long id){
        ItripSearchPolicyHotelVO searchPolicyHotelVO = null;
        try{
            if (EmptyUtils.isNotEmpty(id)){
                searchPolicyHotelVO = itripHotelService.queryHotelPolicy(id);
                return DtoUtil.returnDataSuccess(searchPolicyHotelVO);
            }else {
                return DtoUtil.returnFail("酒店ID不能为空","10208");
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常","10209");
        }
    }


    /**
     * 通过酒店ID查询酒店详情介绍和酒店特色
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryhoteldetails/{id}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto queryHotelDetails(@PathVariable Long id){
        List<ItripSearchDetailsHotelVO> list = null;
        try{
            if (EmptyUtils.isNotEmpty(id)){
                list = itripHotelService.queryHotelDetails(id);
                return DtoUtil.returnDataSuccess(list);
            }else {
                return DtoUtil.returnFail("酒店ID不能为空","10210");
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常","10211");
        }
    }


    /**
     * 通过targetId查询酒店图片
     * @param targetId
     * @return
     */
    @RequestMapping(value = "/getimg/{targetId}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto getImgByTargetId(@PathVariable String targetId){
        Dto dto = new Dto();
        logger.debug("getImgByTargetId targetId"+targetId);
        if (EmptyUtils.isNotEmpty(targetId) && !targetId.equals("")){
            List<ItripImageVO> list = new ArrayList<>();
            Map param = new HashMap();
            param.put("type",0);
            param.put("targetId",targetId);
            try {
                list = itripImageService.getItripImageListByMap(param);
                return DtoUtil.returnDataSuccess(list);
            }catch (Exception e){
                e.printStackTrace();
                return DtoUtil.returnFail("系统异常","100212");
            }
        }else {
            return DtoUtil.returnFail("targetId不能为空","10213");
        }
    }


    /**
     * 通过酒店ID查询酒店特色、商圈、酒店名称（视频描述）
     * @param hotelId
     * @return
     */
    @RequestMapping(value = "/getvideodesc/{hotelId}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto getVideoDescByHotelId(@PathVariable String hotelId){
        if (EmptyUtils.isNotEmpty(hotelId) && !hotelId.equals("")){
            try{
                HotelVideoDescVO hotelVideoDescVO = itripHotelService.getVideoDescByHotelId(Long.valueOf(hotelId));
                return DtoUtil.returnDataSuccess(hotelVideoDescVO);
            }catch (Exception e){
                e.printStackTrace();
                return DtoUtil.returnFail("获取酒店视频文字描述失败", "100214");
            }
        }else {
            return DtoUtil.returnFail("酒店id不能为空", "100215");
        }
    }
}
