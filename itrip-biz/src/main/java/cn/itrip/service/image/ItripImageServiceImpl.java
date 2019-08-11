package cn.itrip.service.image;

import cn.itrip.beans.pojo.ItripImage;
import cn.itrip.beans.vo.ItripImageVO;
import cn.itrip.dao.hotel.ItripHotelMapper;
import cn.itrip.dao.image.ItripImageMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItripImageServiceImpl implements ItripImageService {

    private ItripImageMapper itripImageMapper;
    @Override
    public ItripImage getItripImageById(Long id) throws Exception {
        return itripImageMapper.getItripImageById(id);
    }

    @Override
    public List<ItripImageVO> getItripImageListByMap(Map<String, Object> param) throws Exception {
        return itripImageMapper.getItripImageListByMap(param);
    }

    @Override
    public Integer getItripImageCountByMap(Map<String, Object> param) throws Exception {
        return itripImageMapper.getItripImageCountByMap(param);
    }

    @Override
    public Integer itriptxAddItripImage(ItripImage itripImage) throws Exception {
        return itripImageMapper.insertItripImage(itripImage);
    }

    @Override
    public Integer itriptxModifyItripImage(ItripImage itripImage) throws Exception {
        return itripImageMapper.updateItripImage(itripImage);
    }

    @Override
    public Integer itriptxDeleteItripImageById(Long id) throws Exception {
        return itripImageMapper.deleteItripImageById(id);
    }
}
