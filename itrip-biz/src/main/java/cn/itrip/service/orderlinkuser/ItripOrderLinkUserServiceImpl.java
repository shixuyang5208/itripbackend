package cn.itrip.service.orderlinkuser;

import cn.itrip.beans.pojo.ItripOrderLinkUser;
import cn.itrip.beans.vo.order.ItripOrderLinkUserVo;
import cn.itrip.dao.orderlinkuser.ItripOrderLinkUserMapper;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripOrderLinkUserServiceImpl implements ItripOrderLinkUserService {
    @Resource
    private ItripOrderLinkUserMapper orderLinkUserMapper;

    @Override
    public Integer ItriptxAddOrderLinkUser(ItripOrderLinkUser orderLinkUser) throws Exception {
        orderLinkUser.setCreationDate(new Date());
        return orderLinkUserMapper.insertItripOrderLinkUser(orderLinkUser);
    }

    @Override
    public Integer ItriptxDeleteOderLinkUser(Long id) throws Exception {
        return orderLinkUserMapper.deleteItripOrderLinkUserById(id);
    }

    @Override
    public Integer ItriptxModifyOrderLinkUser(ItripOrderLinkUser orderLinkUser) throws Exception {
        orderLinkUser.setCreationDate(new Date());
        return orderLinkUserMapper.updateItripOrderLinkUser(orderLinkUser);
    }

    @Override
    public ItripOrderLinkUser getItripOrderLinkUserById(Long id) throws Exception {
        return orderLinkUserMapper.getItripOrderLinkUserById(id);
    }

    @Override
    public List<ItripOrderLinkUserVo> getItripOrderLinkUserListByMap(Map<String, Object> param) throws Exception {
        return orderLinkUserMapper.getItripOrderLinkUserListByMap(param);
    }

    @Override
    public Integer getItripOrderLinkUserCountByMap(Map<String, Object> param) throws Exception {
        return orderLinkUserMapper.getItripOrderLinkUserCountByMap(param);
    }

    @Override
    public List<Long> getItripOrderLinkUserIdByOrder() throws Exception {
        return orderLinkUserMapper.getItripOrderLinkUserIdsByOrder();
    }
}
