package cn.itrip.service.hoteltradingarea;

import cn.itrip.beans.pojo.ItripHotelTradingArea;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.areadic.ItripAreaDicMapper;
import cn.itrip.dao.hoteltradingarea.ItripHotelTradingAreaMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class ItripHotelTradingAreaServiceImpl implements ItripHotelTradingAreaService {
    @Resource
    private ItripHotelTradingAreaMapper tradingAreaMapper;
    @Override
    public ItripHotelTradingArea getItripHotelTradingAreaById(Long id) throws Exception {
        return tradingAreaMapper.getItripHotelTradingAreaById(id);
    }

    @Override
    public List<ItripHotelTradingArea> getItripHotelTradingAreaListByMap(Map<String, Object> param) throws Exception {
        return tradingAreaMapper.getItripHotelTradingAreaListByMap(param);
    }

    @Override
    public Integer getItripHotelTradingAreaCountByMap(Map<String, Object> param) throws Exception {
        return tradingAreaMapper.getItripHotelTradingAreaCountByMap(param);
    }

    @Override
    public Integer itriptxAddItripHotelTradingArea(ItripHotelTradingArea itripHotelTradingArea) throws Exception {
        return tradingAreaMapper.insertItripHotelTradingArea(itripHotelTradingArea);
    }

    @Override
    public Integer itriptxModifyItripHotelTradingArea(ItripHotelTradingArea itripHotelTradingArea) throws Exception {
        return tradingAreaMapper.updateItripHotelTradingArea(itripHotelTradingArea);
    }

    @Override
    public Integer itriptxDeleteItripHotelTradingAreaById(Long id) throws Exception {
        return tradingAreaMapper.deleteItripHotelTradingAreaById(id);
    }

    @Override
    public Page<ItripHotelTradingArea> queryItripHotelTradingAreaPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = tradingAreaMapper.getItripHotelTradingAreaCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo)? Constants.DEFAULT_PAGE_NO:pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize)?Constants.DEFAULT_PAGE_SIZE:pageSize;
        Page page = new Page(pageNo,pageSize,total);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        List<ItripHotelTradingArea> list = tradingAreaMapper.getItripHotelTradingAreaListByMap(param);
        page.setRows(list);
        return page;
    }
}