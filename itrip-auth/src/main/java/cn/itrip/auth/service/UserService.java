package cn.itrip.auth.service;

import cn.itrip.beans.pojo.ItripUser;

public interface UserService {
    public void itriptxCreateUser(ItripUser user) throws Exception;
    public ItripUser login(String name,String password) throws Exception;
    public ItripUser findByUserName(String userName) throws Exception;
}
