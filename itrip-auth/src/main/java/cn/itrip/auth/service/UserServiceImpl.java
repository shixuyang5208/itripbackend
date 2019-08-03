package cn.itrip.auth.service;


import cn.itrip.common.MD5;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.itrip.auth.exception.UserLoginFailedException;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.RedisAPI;
import cn.itrip.dao.user.ItripUserMapper;

import java.util.*;

import javax.annotation.Resource;

/**
 * 用户管理接口的实现
 * @author hduser
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	private Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Resource
	private ItripUserMapper itripUserMapper;
	@Resource
	private RedisAPI redisAPI;
	@Resource
	private MailService mailService;


    /**
     * 根据用户名查找用户
     * @param username
     * @return
     * @throws Exception 
     */
    public ItripUser findByUsername(String username) throws Exception {
        Map<String, Object> param=new HashMap();
        param.put("userCode", username);
		List<ItripUser> list= itripUserMapper.getItripUserListByMap(param);
		if(list.size()>0)
			return list.get(0);
		else
			return null;
    }

	@Override
	public ItripUser findOne(Long userId) throws Exception {
		return null;
	}

	@Override
	public List<ItripUser> findAll() throws Exception {
		return null;
	}

	@Override
	public boolean isActive(String mail, String activationCode) throws Exception {
		return false;
	}

	@Override
	public void txCreateUserByPhone(ItripUser user) throws Exception {

	}

	@Override
	public boolean validatePhone(String phoneNum, String verificationCode) throws Exception {
		return false;
	}


	public void createUser(ItripUser user) throws Exception {
		itripUserMapper.insertItripUser(user);
	}

	/**
	 * 创建用户
	 * @param user
	 * @throws Exception
	 */
	@Override
	public void txcreateUser(ItripUser user) throws Exception {
		//发送激活邮件
		String activationCode = MD5.getMd5(new Date().toLocaleString(),32);
		mailService.sendActivationMail(user.getUserCode(),activationCode);
		//保存（新增）用户信息
		itripUserMapper.insertItripUser(user);
	}

	@Override
	public void deleteUser(Long userId) throws Exception {

	}

	@Override
	public void changePassword(Long userId, String newPassword) throws Exception {

	}

	@Override
	public ItripUser login(String name, String password) throws Exception  {
		// TODO Auto-generated method stub
		ItripUser user=this.findByUsername(name);
		if(null!=user&&user.getUserPassword().equals(password))
		{
			if(user.getActivated()!=1){
				throw new UserLoginFailedException("用户未激活");
			}
			return user;
		}
		else
			return null;
	}


	

	
	
   
}
