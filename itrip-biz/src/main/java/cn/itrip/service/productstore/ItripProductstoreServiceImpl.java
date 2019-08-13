package cn.itrip.service.productstore;

import cn.itrip.beans.pojo.ItripProductStore;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.productstore.ItripProductStoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ItripProductstoreServiceImpl implements ItripProductstoreService {

    @Resource
    private ItripProductStoreMapper itripProductStoreMapper;

    @Override
    public ItripProductStore getItripProductStoreById(Long id) throws Exception {
        return itripProductStoreMapper.getItripProductStoreById(id);
    }

    @Override
    public List<ItripProductStore> getItripProductStoreListByMap(Map<String, Object> param) throws Exception {
        return itripProductStoreMapper.getItripProductStoreListByMap(param);
    }

    @Override
    public Integer getItripProductStoreCountByMap(Map<String, Object> param) throws Exception {
        return itripProductStoreMapper.getItripProductStoreCountByMap(param);
    }

    @Override
    public Integer itriptxAddItripProductStore(ItripProductStore itripProductStore) throws Exception {
        return itripProductStoreMapper.insertItripProductStore(itripProductStore);
    }

    @Override
    public Integer itriptxModifyItripProductStore(ItripProductStore itripProductStore) throws Exception {
        return itripProductStoreMapper.updateItripProductStore(itripProductStore);
    }

    @Override
    public Integer itriptxDeleteItripProductStoreById(Long id) throws Exception {
        return itripProductStoreMapper.deleteItripProductStoreById(id);
    }

    @Override
    public Page<ItripProductStore> queryItripProductStorePageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripProductStoreMapper.getItripProductStoreCountByMap(param);
        pageNo = EmptyUtils.isNotEmpty(pageNo)?pageNo: Constants.DEFAULT_PAGE_NO;
        pageSize = EmptyUtils.isNotEmpty(pageSize)?pageSize:Constants.DEFAULT_PAGE_SIZE;
        Page page = new Page(pageNo,pageSize,total);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        List<ItripProductStore> list = itripProductStoreMapper.getItripProductStoreListByMap(param);
        page.setRows(list);
        return page;
    }
}
