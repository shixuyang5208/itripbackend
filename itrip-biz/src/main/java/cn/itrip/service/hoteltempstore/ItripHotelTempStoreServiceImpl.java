package cn.itrip.service.hoteltempstore;

import cn.itrip.beans.pojo.ItripHotelTempStore;
import cn.itrip.beans.vo.store.StoreVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.hoteltempstore.ItripHotelTempStoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class ItripHotelTempStoreServiceImpl implements ItripHotelTempStoreService {

    @Resource
    private ItripHotelTempStoreMapper tempStoreMapper;

    @Override
    public ItripHotelTempStore getItripHotelTempStoreById(Long id) throws Exception {
        return tempStoreMapper.getItripHotelTempStoreById(id);
    }

    @Override
    public List<ItripHotelTempStore> getItripHotelTempStoreListByMap(Map<String, Object> param) throws Exception {
        return tempStoreMapper.getItripHotelTempStoreListByMap(param);
    }

    @Override
    public Integer getItripHotelTempStoreCountByMap(Map<String, Object> param) throws Exception {
        return tempStoreMapper.getItripHotelTempStoreCountByMap(param);
    }

    @Override
    public Integer itriptxAddItripHotelTempStore(ItripHotelTempStore itripHotelTempStore) throws Exception {
        return tempStoreMapper.insertItripHotelTempStore(itripHotelTempStore);
    }

    @Override
    public Integer itriptxModifyItripHotelTempStore(ItripHotelTempStore itripHotelTempStore) throws Exception {
        return tempStoreMapper.updateItripHotelTempStore(itripHotelTempStore);
    }

    @Override
    public Integer itriptxDeleteItripHotelTempStoreById(Long id) throws Exception {
        return tempStoreMapper.deleteItripHotelTempStoreById(id);
    }

    @Override
    public Page<ItripHotelTempStore> queryItripHotelTempStorePageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = tempStoreMapper.getItripHotelTempStoreCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripHotelTempStore> itripHotelTempStoreList = tempStoreMapper.getItripHotelTempStoreListByMap(param);
        page.setRows(itripHotelTempStoreList);
        return page;
    }

    /*
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param roomId    房间ID
     * @param hotelId   酒店ID
     * @return
     * @throws Exception
     */
    @Override
    public List<StoreVO> queryRoomStore(Map<String, Object> param) throws Exception {
        tempStoreMapper.flushStore(param);
        return tempStoreMapper.queryRoomStore(param);
    }


    /*
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param roomId    房间ID
     * @param hotelId   酒店ID
     * @param count     数目
     * @return
     * @throws Exception
     */
    @Override
    public boolean validateRoomStore(Map<String, Object> param) throws Exception {
        Integer count = (Integer)param.get("count");
        tempStoreMapper.flushStore(param);
        List<StoreVO> storeVOList = tempStoreMapper.queryRoomStore(param);
        if (EmptyUtils.isEmpty(storeVOList)){
            return false;
        }
        for (StoreVO vo:storeVOList){
            if (vo.getStore()<count){
                return false;
            }
        }
        return true;
    }


    /*
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param roomId    房间ID
     * @param count     数目
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateRoomStore(Map<String, Object> param) throws Exception {
        Integer flag = tempStoreMapper.updateRoomStore(param);

        return flag == 0?false:true;
    }
}
