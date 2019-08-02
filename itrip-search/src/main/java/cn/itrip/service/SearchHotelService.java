package cn.itrip.service;

import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.Page;

import java.util.List;
//solr
public interface SearchHotelService {
    /**
     *
     * @param vo
     * @param pageNum
     * @param pageSize
     * @return酒店实体
     * @throws Exception
     */
    public Page<ItripHotelVO> searchItripHotelPage(SearchHotelVO vo, Integer pageNum,Integer pageSize) throws Exception;

    /**
     *
     * @param cityId
     * @param pageSize
     * @return 酒店集合
     */
    public List<ItripHotelVO> searchItripHotelListByHotCity(Integer cityId,Integer pageSize);
}
