package cn.itrip.service.orderlinkuser;

import cn.itrip.beans.pojo.ItripOrderLinkUser;
import cn.itrip.beans.vo.order.ItripOrderLinkUserVo;

import java.util.List;
import java.util.Map;

public interface ItripOrderLinkUserService {
    public Integer ItriptxAddOrderLinkUser(ItripOrderLinkUser orderLinkUser) throws Exception;
    public Integer ItriptxDeleteOderLinkUser(Long id) throws Exception;
    public Integer ItriptxModifyOrderLinkUser(ItripOrderLinkUser orderLinkUser) throws Exception;


    public ItripOrderLinkUser getItripOrderLinkUserById(Long id)throws Exception;

    public List<ItripOrderLinkUserVo>	getItripOrderLinkUserListByMap(Map<String,Object> param)throws Exception;

    public Integer getItripOrderLinkUserCountByMap(Map<String,Object> param)throws Exception;
    /**
     * 查询订单联系人（Mapper里已经限定了查询条件：订单状态为'0'---未支付状态）
     * @return
     * @throws Exception
     */
    public List<Long> getItripOrderLinkUserIdByOrder() throws Exception;
}
