package cn.itrip.service.labeldic;

import cn.itrip.beans.pojo.ItripLabelDic;
import cn.itrip.beans.vo.ItripLabelDicVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.labeldic.ItripLabelDicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ItripLabelDicServiceImpl implements ItripLabelDicService {

    @Resource
    private ItripLabelDicMapper itripLabelDicMapper;

    @Override
    public ItripLabelDic getItripLabelDicById(Long id) throws Exception {
        return itripLabelDicMapper.getItripLabelDicById(id);
    }

    @Override
    public List<ItripLabelDic> getItripLabelDicListByMap(Map<String, Object> param) throws Exception {
        return itripLabelDicMapper.getItripLabelDicListByMap(param);
    }

    @Override
    public Integer getItripLabelDicCountByMap(Map<String, Object> param) throws Exception {
        return itripLabelDicMapper.getItripLabelDicCountByMap(param);
    }

    @Override
    public Integer itriptxAddItripLabelDic(ItripLabelDic itripLabelDic) throws Exception {
        return itripLabelDicMapper.insertItripLabelDic(itripLabelDic);
    }

    @Override
    public Integer itriptxModifyItripLabelDic(ItripLabelDic itripLabelDic) throws Exception {
        return itripLabelDicMapper.updateItripLabelDic(itripLabelDic);
    }

    @Override
    public Integer itriptxDeleteItripLabelDicById(Long id) throws Exception {
        return itripLabelDicMapper.deleteItripLabelDicById(id);
    }

    @Override
    public Page<ItripLabelDic> queryItripLabelDicPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripLabelDicMapper.getItripLabelDicCountByMap(param);
        pageNo = EmptyUtils.isNotEmpty(pageNo)?pageNo: Constants.DEFAULT_PAGE_NO;
        pageSize = EmptyUtils.isNotEmpty(pageSize)?pageSize:Constants.DEFAULT_PAGE_SIZE;
        Page page = new Page(pageNo,pageSize,total);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        List list = itripLabelDicMapper.getItripLabelDicListByMap(param);
        page.setRows(list);
        return page;
    }

    @Override
    public List<ItripLabelDicVO> getItripLabelDicByParentId(Long parentId) throws Exception {
        return itripLabelDicMapper.getItripLabelDicByParentId(parentId);
    }
}
