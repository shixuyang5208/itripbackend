package cn.itrip.auth.service;

import javax.annotation.Resource;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import cn.itrip.common.RedisAPI;
/**
 * 邮件发送接口的实现
 * @author hduser
 *
 */
@Service("mailservice")
public class MailServiceImpl implements MailService {

	@Resource
	private MailSender mailSender;
	@Resource
	private RedisAPI redisAPI;
	@Resource
	private SimpleMailMessage simpleMailMessage;

	public void saveActivationInfo(String key,String value){
		redisAPI.set(key,30*60, value);
	}

	/**
	 * 发送注册的激活邮件
	 * @param mailTo
	 * @param activationCode
	 */
	public void sendActivationMail(String mailTo, String activationCode) {
		simpleMailMessage.setTo(mailTo);
		simpleMailMessage.setText("热车迫在眉睫！！！"+mailTo+"激活码："+activationCode);
		mailSender.send(simpleMailMessage);
		this.saveActivationInfo("activation"+mailTo,activationCode);
	}

}
