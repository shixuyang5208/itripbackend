package cn.itrip.auth.service;

import cn.itrip.beans.pojo.ItripUser;

import java.util.List;
import java.util.Set;

/**
 * 用户管理接口
 * @author hduser
 *
 */
public interface UserService {
	public void txcreateUser(ItripUser user)throws Exception;
	public void createUser(ItripUser user)throws Exception;

	public void deleteUser(Long userId)throws Exception;

	public void changePassword(Long userId,String newPassword)throws Exception;

	public ItripUser login(String name, String password) throws Exception;
	public ItripUser findByUsername(String username) throws Exception;
	public ItripUser findOne(Long userId) throws Exception;
	public List<ItripUser> findAll() throws Exception;

	/**
	 * 激活邮箱账户
	 * @param mail 邮箱账号
	 * @param activationCode 激活码
	 * @return 是否激活成功
	 * @throws Exception
	 */
	public boolean active(String mail,String activationCode)throws Exception;

	/**
	 * 手机注册账户
	 * @param user
	 * @throws Exception
	 */
	public void txCreateUserByPhone(ItripUser user)throws Exception;

	/**
	 * 短信验证手机号
	 * @param phoneNum
	 * @param verificationCode 验证码
	 * @return
	 * @throws Exception
	 */
	public boolean validatePhone(String phoneNum,String verificationCode)throws Exception;
}
