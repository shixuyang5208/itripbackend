package cn.itrip.auth.service;

import cn.itrip.auth.exception.UserLoginFailedException;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.common.RedisAPI;
import cn.itrip.dao.user.ItripUserMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    private Logger logger=Logger.getLogger(UserServiceImpl.class);

    @Resource
    private ItripUserMapper itripUserMapper;


    @Override
    public void itriptxCreateUser(ItripUser user) throws Exception {

    }

    @Override
    public ItripUser login(String name, String password) throws Exception  {
        // TODO Auto-generated method stub
        ItripUser user=this.findByUserName(name);
        if(user != null && user.getUserPassword().equals(password)) {
            if(user.getActivated()!= 1){
                throw new UserLoginFailedException("用户未激活");
            }
            return user;
        } else {
            return null;
        }
    }

    /**
     * 根据用户名查找用户
     * @param userName
     * @return
     * @throws Exception
     */
    public ItripUser findByUserName(String userName) throws Exception {
        Map<String,Object> param = new HashMap();
        param.put("userCode",userName);
        List<ItripUser> list = itripUserMapper.getItripUserListByMap(param);
        if(list.size()>0 && list != null){
            return list.get(0);
        }else {
            return null;
        }
    }
}
