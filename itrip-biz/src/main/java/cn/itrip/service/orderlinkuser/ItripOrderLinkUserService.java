package cn.itrip.service.orderlinkuser;

import cn.itrip.beans.pojo.ItripOrderLinkUser;

import java.util.List;

public interface ItripOrderLinkUserService {
    /*public Integer addItripOrderLinkUser(ItripOrderLinkUser orderLinkUser) throws Exception;
    public Integer deleteItripOderLinkUser(Long id) throws Exception;
    public Integer modifyItripOrderLinkUser(ItripOrderLinkUser orderLinkUser) throws Exception;*/

    /**
     * 查询订单联系人（Mapper里已经限定了查询条件：订单状态为'0'---未支付状态）
     * @return
     * @throws Exception
     */
    public List<Long> getItripOrderLinkUserIdByOrder() throws Exception;
}
