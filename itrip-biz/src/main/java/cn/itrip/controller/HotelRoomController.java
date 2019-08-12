package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripImage;
import cn.itrip.beans.vo.ItripImageVO;
import cn.itrip.beans.vo.ItripLabelDicVO;
import cn.itrip.beans.vo.hotelroom.ItripHotelRoomVO;
import cn.itrip.beans.vo.hotelroom.SearchHotelRoomVO;
import cn.itrip.common.DateUtil;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.service.hotelroom.ItripHotelRoomService;
import cn.itrip.service.image.ItripImageService;
import cn.itrip.service.labeldic.ItripLabelDicService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/hotelroom")
public class HotelRoomController {
    @Resource
    private ItripHotelRoomService itripHotelRoomService;
    @Resource
    private ItripLabelDicService itripLabelDicService;
    @Resource
    private ItripImageService itripImageService;
    Logger logger = Logger.getLogger(HotelRoomController.class);


    /**
     * 通过tartegId（房型）获取酒店图片
     * @param targetId
     * @return
     */
    @RequestMapping(value = "/getimg/{targetId}",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto getImgByTargetId(@PathVariable String targetId){
        logger.debug("getImgByTargetId"+targetId);
        if (EmptyUtils.isNotEmpty(targetId)){
            Map param = new HashMap();
            param.put("targetId",targetId);
            param.put("type","1");
            try {
                List<ItripImageVO> itripImageVOList = itripImageService.getItripImageListByMap(param);
                return DtoUtil.returnSuccess("获取酒店房型图片成功",itripImageVOList);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("系统异常，获取酒店房型图片失败", "100301");
            }
        }else {
            return DtoUtil.returnFail("酒店房型id不能为空", "100302");
        }
    }


    /**
     * 查询酒店房间
     * @param
     * @return
     */
    @RequestMapping(value = "/queryhotelroombyhotel", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto queryHotelRoomByHotel(@RequestBody SearchHotelRoomVO vo){
        List<ItripHotelRoomVO> list = null;

            Map<String,Object> param = new HashMap();
            //判断必填的酒店ID
            if (EmptyUtils.isEmpty(vo.getHotelId())){
                return DtoUtil.returnFail("酒店ID不能为空", "100303");
            }
            //判断必填的入住和退房时间
            if (EmptyUtils.isEmpty(vo.getStartDate()) || EmptyUtils.isEmpty(vo.getEndDate())){
                return DtoUtil.returnFail("必须填写酒店入住及退房时间", "100303");
            }
            if (EmptyUtils.isNotEmpty(vo.getStartDate()) && EmptyUtils.isNotEmpty(vo.getEndDate())) {
                if (vo.getStartDate().getTime() > vo.getEndDate().getTime()) {
                    return DtoUtil.returnFail("入住时间不能大于退房时间", "100303");
                }
            }
            List dates = DateUtil.getBetweenDates(vo.getStartDate(),vo.getEndDate());
            param.put("timesList",dates);
            param.put("hotelId", vo.getHotelId());
            param.put("isBook", vo.getIsBook());
            param.put("isHavingBreakfast", vo.getIsHavingBreakfast());
            param.put("isTimelyResponse", vo.getIsTimelyResponse());
            param.put("roomBedTypeId", vo.getRoomBedTypeId());
            param.put("isCancel", vo.getIsCancel());

            if (EmptyUtils.isEmpty(vo.getPayType()) || vo.getPayType()==3){
                param.put("payType",null);
            }else {
                param.put("payType",vo.getPayType());
            }
        try {
            list = itripHotelRoomService.getItripHotelRoomListByMap(param);
            return DtoUtil.returnSuccess("获取成功",list);
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常，获取酒店房型列表失败", "100304");
        }
    }


    /**
     * 查询酒店房间床型表
     * @return
     */
    @RequestMapping(value = "/queryhotelroombed", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto queryHotelRoomBed(){
        try {
            List<ItripLabelDicVO> labelDicVOList = itripLabelDicService.getItripLabelDicByParentId(1L);
            return DtoUtil.returnSuccess("获取成功", labelDicVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取床型失败", "100305");
        }
    }
}
