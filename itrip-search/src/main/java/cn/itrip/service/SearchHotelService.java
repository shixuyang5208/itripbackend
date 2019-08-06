package cn.itrip.service;

import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.Page;
import cn.itrip.common.PropertiesUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/*
solr搜索酒店的
 */
@Service
public interface SearchHotelService {
    /**
     * 分页搜索酒店
     * @param searchHotelVO
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public Page<ItripHotelVO> searchHotelPage(SearchHotelVO searchHotelVO,Integer pageNum,Integer pageSize) throws Exception;

    /**
     * 根据热门城市ID查询酒店
     * @param cityId
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<ItripHotelVO> searchHotelByHotCity(Integer cityId,Integer pageSize) throws Exception;

}
