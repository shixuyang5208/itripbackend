package cn.itrip.service.hotel;

import cn.itrip.beans.pojo.ItripAreaDic;
import cn.itrip.beans.pojo.ItripHotel;
import cn.itrip.beans.pojo.ItripLabelDic;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.beans.vo.hotel.ItripSearchDetailsHotelVO;
import cn.itrip.beans.vo.hotel.ItripSearchFacilitiesHotelVO;
import cn.itrip.beans.vo.hotel.ItripSearchPolicyHotelVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.hotel.ItripHotelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ItripHotelServiceImpl implements ItripHotelService {

    @Resource
    private ItripHotelMapper itripHotelMapper;


    @Override
    public HotelVideoDescVO getVideoDescByHotelId(Long id) throws Exception {
        HotelVideoDescVO hotelVideoDescVO = new HotelVideoDescVO();
        //通过ID获取商圈
        List<ItripAreaDic> itripAreaDicList = itripHotelMapper.getHotelAreaByHotelId(id);
        List list1 = new ArrayList();
        for (ItripAreaDic dic:itripAreaDicList){
            list1.add(dic.getName());
        }
        hotelVideoDescVO.setTradingAreaNameList(list1);
        //通过ID获取酒店特色
        List<ItripLabelDic> itripLabelDicList = itripHotelMapper.getHotelFeatureByHotelId(id);
        List list2 = new ArrayList();
        for (ItripLabelDic dic:itripLabelDicList){
            list2.add(dic.getName());
        }
        hotelVideoDescVO.setHotelFeatureList(list2);
        //获取酒店名称
        hotelVideoDescVO.setHotelName(itripHotelMapper.getItripHotelById(id).getHotelName());

        return hotelVideoDescVO;
    }

    @Override
    public ItripHotel getItripHotelById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelById(id);
    }

    @Override
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelFacilitiesById(id);
    }

    @Override
    public ItripSearchPolicyHotelVO queryHotelPolicy(Long id) throws Exception {
        return itripHotelMapper.queryHotelPolicy(id);
    }

    @Override
    public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id) throws Exception {
        //通过ID查询对应酒店的细节描述，放入集合
        List<ItripLabelDic> itripLabelDicList = itripHotelMapper.getHotelFeatureByHotelId(id);
        //实例化一个vo集合，用来存储酒店细节介绍和酒店所有特色
        List<ItripSearchDetailsHotelVO> searchDetailsHotelVOList = new ArrayList<>();
        //先实例【酒店细节介绍】，将所有酒店固有特色传入,再放入集合
        ItripSearchDetailsHotelVO vo1 = new ItripSearchDetailsHotelVO();
        vo1.setName("酒店细节介绍");
        vo1.setDescription(itripHotelMapper.getItripHotelById(id).getDetails());
        searchDetailsHotelVOList.add(vo1);
        //再循环取出酒店的所有特色放入vo2，再将vo2放入集合
        for (ItripLabelDic dic:itripLabelDicList){
            ItripSearchDetailsHotelVO vo2 = new ItripSearchDetailsHotelVO();
            vo2.setName(dic.getName());
            vo2.setDescription(dic.getDescription());
            searchDetailsHotelVOList.add(vo2);
        }
        return searchDetailsHotelVOList;
    }

    @Override
    public List<ItripHotel> getItripHotelListByMap(Map<String, Object> param) throws Exception {
        return itripHotelMapper.getItripHotelListByMap(param);
    }

    @Override
    public Integer getItripHotelCountByMap(Map<String, Object> param) throws Exception {
        return itripHotelMapper.getItripHotelCountByMap(param);
    }

    @Override
    public Integer itriptxAddItripHotel(ItripHotel itripHotel) throws Exception {
        return itripHotelMapper.insertItripHotel(itripHotel);
    }

    @Override
    public Integer itriptxModifyItripHotel(ItripHotel itripHotel) throws Exception {
        return itripHotelMapper.updateItripHotel(itripHotel);
    }

    @Override
    public Integer itriptxDeleteItripHotelById(Long id) throws Exception {
        return itripHotelMapper.deleteItripHotelById(id);
    }

    @Override
    public Page<ItripHotel> queryItripHotelPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripHotelMapper.getItripHotelCountByMap(param);
        pageNo = EmptyUtils.isNotEmpty(pageNo)?pageNo: Constants.DEFAULT_PAGE_NO;
        pageSize = EmptyUtils.isNotEmpty(pageSize)?pageSize:Constants.DEFAULT_PAGE_SIZE;
        Page page = new Page(pageNo,pageSize,total);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        List list = itripHotelMapper.getItripHotelListByMap(param);
        page.setRows(list);
        return page;
    }
}
