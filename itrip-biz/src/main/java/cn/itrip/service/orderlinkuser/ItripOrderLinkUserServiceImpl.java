package cn.itrip.service.orderlinkuser;

import cn.itrip.beans.pojo.ItripOrderLinkUser;
import cn.itrip.dao.orderlinkuser.ItripOrderLinkUserMapper;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;
@Service
public class ItripOrderLinkUserServiceImpl implements ItripOrderLinkUserService {
    @Resource
    private ItripOrderLinkUserMapper orderLinkUserMapper;
    @Override
    public List<Long> getItripOrderLinkUserIdByOrder() throws Exception {
        return orderLinkUserMapper.getItripOrderLinkUserIdsByOrder();
    }
}
