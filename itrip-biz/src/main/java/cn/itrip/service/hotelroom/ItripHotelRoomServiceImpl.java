package cn.itrip.service.hotelroom;

import cn.itrip.beans.pojo.ItripHotelRoom;
import cn.itrip.beans.vo.hotelroom.ItripHotelRoomVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.hotelroom.ItripHotelRoomMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ItripHotelRoomServiceImpl implements ItripHotelRoomService {

    @Resource
    private ItripHotelRoomMapper itripHotelRoomMapper;

    @Override
    public ItripHotelRoom getItripHotelRoomById(Long id) throws Exception {
        return itripHotelRoomMapper.getItripHotelRoomById(id);
    }

    @Override
    public List<ItripHotelRoomVO> getItripHotelRoomListByMap(Map<String, Object> param) throws Exception {
        return itripHotelRoomMapper.getItripHotelRoomListByMap(param);
    }

    @Override
    public Integer getItripHotelRoomCountByMap(Map<String, Object> param) throws Exception {
        return itripHotelRoomMapper.getItripHotelRoomCountByMap(param);
    }

    @Override
    public Integer itriptxAddItripHotelRoom(ItripHotelRoom itripHotelRoom) throws Exception {
        return itripHotelRoomMapper.insertItripHotelRoom(itripHotelRoom);
    }

    @Override
    public Integer itriptxModifyItripHotelRoom(ItripHotelRoom itripHotelRoom) throws Exception {
        return itripHotelRoomMapper.updateItripHotelRoom(itripHotelRoom);
    }

    @Override
    public Integer itriptxDeleteItripHotelRoomById(Long id) throws Exception {
        return itripHotelRoomMapper.deleteItripHotelRoomById(id);
    }

    @Override
    public Page<ItripHotelRoomVO> queryItripHotelRoomPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripHotelRoomMapper.getItripHotelRoomCountByMap(param);
        pageNo = EmptyUtils.isNotEmpty(pageNo)?pageNo: Constants.DEFAULT_PAGE_NO;
        pageSize = EmptyUtils.isNotEmpty(pageSize)?pageSize:Constants.DEFAULT_PAGE_SIZE;
        Page page = new Page(pageNo,pageSize,total);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",pageSize);
        List list = this.getItripHotelRoomListByMap(param);
        page.setRows(list);
        return page;
    }
}
