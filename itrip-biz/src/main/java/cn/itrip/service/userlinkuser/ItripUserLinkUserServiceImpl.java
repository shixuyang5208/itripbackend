package cn.itrip.service.userlinkuser;

import cn.itrip.beans.pojo.ItripUserLinkUser;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.userlinkuser.ItripUserLinkUserMapper;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripUserLinkUserServiceImpl implements ItripUserLinkUserService {
    @Resource
    private ItripUserLinkUserMapper userLinkUserMapper;
    @Override
    public Integer addItripUserLinkUser(ItripUserLinkUser linkUser) throws Exception {
        linkUser.setCreationDate(new Date());
        return userLinkUserMapper.insertItripUserLinkUser(linkUser);
    }

    @Override
    public Integer deletItripUserLinkUserById(Long[] ids) throws Exception {
        return userLinkUserMapper.deleteItripUserLinkUserByIds(ids);
    }

    @Override
    public Integer modifyItripUserLinkUser(ItripUserLinkUser linkUser) throws Exception {
        return userLinkUserMapper.updateItripUserLinkUser(linkUser);
    }

    @Override
    public Integer getItripUserLinkUserCountByMap(Map<String, Object> param) throws Exception {
        return userLinkUserMapper.getItripUserLinkUserCountByMap(param);
    }

    @Override
    public List<ItripUserLinkUser> getItripUserLinkUserByItself(ItripUserLinkUser linkUser) throws Exception {
        return getItripUserLinkUserByItself(linkUser);
    }

    @Override
    public Page<ItripUserLinkUser> getItripUserLinkUserPageByMap(Map<String, Object> param,Integer pageNum,Integer pageSize) throws Exception {
        Integer total = this.getItripUserLinkUserCountByMap(param);
        pageSize = EmptyUtils.isEmpty(pageSize)? Constants.DEFAULT_PAGE_SIZE:pageSize;
        pageNum = EmptyUtils.isEmpty(pageNum)? Constants.DEFAULT_PAGE_NO:pageNum;
        Page page = new Page(pageNum,pageSize,total);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        List<ItripUserLinkUser> itripUserLinkUserList = userLinkUserMapper.getItripUserLinkUserListByMap(param);
        page.setRows(itripUserLinkUserList);
        return page;
    }
}
