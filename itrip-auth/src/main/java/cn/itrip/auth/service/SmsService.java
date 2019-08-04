package cn.itrip.auth.service;

/**
 * 短信发送接口 
 * @author hduser
 *
 */
public interface SmsService {

	/**
	 *
	 * @param to 要发送的手机号
	 * @param templatedId 短信模板ID
	 * @param datas 替换模板
	 * @throws Exception
	 */
	public void send(String to,String templatedId,String[] datas)throws Exception;
}
