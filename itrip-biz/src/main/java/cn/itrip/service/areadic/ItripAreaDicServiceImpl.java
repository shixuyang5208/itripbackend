package cn.itrip.service.areadic;

import cn.itrip.beans.pojo.ItripAreaDic;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.areadic.ItripAreaDicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ItripAreaDicServiceImpl implements ItripAreaDicService {

    @Resource
    private ItripAreaDicMapper itripAreaDicMapper;

    @Override
    public ItripAreaDic getItripAreaDicById(Long id) throws Exception {
        return itripAreaDicMapper.getItripAreaDicById(id);
    }

    @Override
    public List<ItripAreaDic> getItripAreaDicListByMap(Map<String, Object> param) throws Exception {
        return itripAreaDicMapper.getItripAreaDicListByMap(param);
    }

    @Override
    public Integer getItripAreaDicCountByMap(Map<String, Object> param) throws Exception {
        return itripAreaDicMapper.getItripAreaDicCountByMap(param);
    }

    @Override
    public Integer itriptxAddItripAreaDic(ItripAreaDic itripAreaDic) throws Exception {
        return itripAreaDicMapper.insertItripAreaDic(itripAreaDic);
    }

    @Override
    public Integer itriptxModifyItripAreaDic(ItripAreaDic itripAreaDic) throws Exception {
        return itripAreaDicMapper.updateItripAreaDic(itripAreaDic);
    }

    @Override
    public Integer itriptxDeleteItripAreaDicById(Long id) throws Exception {
        return itripAreaDicMapper.deleteItripAreaDicById(id);
    }

    @Override
    public Page<ItripAreaDic> queryItripAreaDicPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripAreaDicMapper.getItripAreaDicCountByMap(param);
        pageNo = EmptyUtils.isNotEmpty(pageNo)?pageNo:Constants.DEFAULT_PAGE_NO;
        pageSize = EmptyUtils.isNotEmpty(pageSize)?pageSize:Constants.DEFAULT_PAGE_SIZE;
        Page page = new Page(total,pageNo,pageSize);
        param.put("getBeginPos",page.getBeginPos());
        param.put("getPageSize",page.getPageSize());
        List<ItripAreaDic> list = itripAreaDicMapper.getItripAreaDicListByMap(param);
        page.setRows(list);
        return page;
    }
}
