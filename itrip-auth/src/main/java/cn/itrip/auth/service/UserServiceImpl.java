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

	@Resource
	SmsService smsService;

	private int expire=5;//过期时间分钟

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
		return itripUserMapper.getItripUserById(userId);
	}

	@Override
	public List<ItripUser> findAll() throws Exception {
		return itripUserMapper.getItripUserListByMap(null);
	}

	/**
	 * 激活邮箱用户
	 * @param mail 邮箱账号
	 * @param activationCode 激活码
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean active(String mail, String activationCode) throws Exception {
		String key = "activation:"+mail;
		if(redisAPI.exist(key)){
			if(redisAPI.get(key).equals(activationCode)){
				ItripUser user = this.findByUsername(mail);
				if(EmptyUtils.isNotEmpty(user)) {
					logger.debug("激活用户："+mail);
					user.setActivated(1);//激活用户
					user.setUserType(0);//自注册用户
					user.setFlatID(user.getId());
					return true;
				}
			}
		}
    	return false;
	}


    /**
     * 使用手机注册
     * @param user
     * @throws Exception
     */
	@Override
	public void txCreateUserByPhone(ItripUser user) throws Exception {
		String code = String.valueOf(MD5.getRandomCode());
		smsService.send(user.getUserCode(),"1",new String[]{code,String.valueOf(expire)});
		//缓存验证码
		String key = "activation:" + user.getUserCode();
		redisAPI.set(key,expire*60,code);
		//保存用户信息
		itripUserMapper.insertItripUser(user);
	}

	/**
	 * 短信验证手机号
	 * @param phoneNum
	 * @param verificationCode 验证码
	 * @return 是否激活成功
	 * @throws Exception
	 */
	@Override
	public boolean validatePhone(String phoneNum, String verificationCode) throws Exception {
		String key = "activation:" + phoneNum;
		if (redisAPI.exist(key)){
			if (redisAPI.get(key).equals(verificationCode)){
				ItripUser user = this.findByUsername(phoneNum);
				if (EmptyUtils.isNotEmpty(user)){
					logger.debug("用户手机验证通过："+phoneNum);
					user.setActivated(1);
					user.setFlatID(user.getId());
					itripUserMapper.insertItripUser(user);
					return true;
				}
			}
		}
		return false;
	}


	public void createUser(ItripUser user) throws Exception {
		itripUserMapper.insertItripUser(user);
	}

	/**
	 * 创建邮箱用户
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
		itripUserMapper.deleteItripUserById(userId);
	}

	/**
	 * 修改密码
	 * @param userId
	 * @param newPassword 新密码
	 * @throws Exception
	 */
	@Override
	public void changePassword(Long userId, String newPassword) throws Exception {
		ItripUser user = itripUserMapper.getItripUserById(userId);
		user.setUserPassword(newPassword);
		itripUserMapper.updateItripUser(user);
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
