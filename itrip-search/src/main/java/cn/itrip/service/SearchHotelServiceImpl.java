package cn.itrip.service;

import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.Page;

import java.util.List;

public class SearchHotelServiceImpl implements SearchHotelService {
    @Override
    public Page<ItripHotelVO> searchItripHotelPage(SearchHotelVO vo, Integer pageNum, Integer pageSize) throws Exception {
        return null;
    }

    @Override
    public List<ItripHotelVO> searchItripHotelListByHotCity(Integer cityId, Integer pageSize) {
        return null;
    }
}
