package cn.itrip.service.hotelextendproperty;

import cn.itrip.beans.pojo.ItripHotelExtendProperty;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.hotelextendproperty.ItripHotelExtendPropertyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripHotelExtendPropertyServiceImpl implements ItripHotelExtendPropertyService {
    @Resource
    private ItripHotelExtendPropertyMapper extendPropertyMapper;
    public ItripHotelExtendProperty getItripHotelExtendPropertyById(Long id)throws Exception{
        return extendPropertyMapper.getItripHotelExtendPropertyById(id);
    }

    public List<ItripHotelExtendProperty> getItripHotelExtendPropertyListByMap(Map<String,Object> param)throws Exception{
        return extendPropertyMapper.getItripHotelExtendPropertyListByMap(param);
    }

    public Integer getItripHotelExtendPropertyCountByMap(Map<String,Object> param)throws Exception{
        return extendPropertyMapper.getItripHotelExtendPropertyCountByMap(param);
    }

    public Integer itriptxAddItripHotelExtendProperty(ItripHotelExtendProperty itripHotelExtendProperty)throws Exception{
        itripHotelExtendProperty.setCreationDate(new Date());
        return extendPropertyMapper.insertItripHotelExtendProperty(itripHotelExtendProperty);
    }

    public Integer itriptxModifyItripHotelExtendProperty(ItripHotelExtendProperty itripHotelExtendProperty)throws Exception{
        itripHotelExtendProperty.setModifyDate(new Date());
        return extendPropertyMapper.updateItripHotelExtendProperty(itripHotelExtendProperty);
    }

    public Integer itriptxDeleteItripHotelExtendPropertyById(Long id)throws Exception{
        return extendPropertyMapper.deleteItripHotelExtendPropertyById(id);
    }

    public Page<ItripHotelExtendProperty> queryItripHotelExtendPropertyPageByMap(Map<String,Object> param, Integer pageNo, Integer pageSize)throws Exception{
        Integer total = extendPropertyMapper.getItripHotelExtendPropertyCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripHotelExtendProperty> itripHotelExtendPropertyList = extendPropertyMapper.getItripHotelExtendPropertyListByMap(param);
        page.setRows(itripHotelExtendPropertyList);
        return page;
    }
}
