package cn.itrip.service.userlinkuser;

import cn.itrip.beans.pojo.ItripUserLinkUser;
import cn.itrip.common.Page;

import java.util.List;
import java.util.Map;
/*
常用联系人增删改查
 */
public interface ItripUserLinkUserService {
    //增
    public Integer addItripUserLinkUser(ItripUserLinkUser linkUser) throws Exception;
    // 删
    public Integer deletItripUserLinkUserById(Long[] ids) throws Exception;
    //改
    public Integer modifyItripUserLinkUser(ItripUserLinkUser linkUser) throws Exception;
    //查
    public Integer getItripUserLinkUserCountByMap(Map<String, Object> param) throws Exception;
    public List<ItripUserLinkUser> getItripUserLinkUserByItself(ItripUserLinkUser linkUser) throws Exception;
    public Page<ItripUserLinkUser> getItripUserLinkUserPageByMap(Map<String, Object> param, Integer pageNum, Integer pageSize) throws Exception;
}
