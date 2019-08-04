package cn.itrip.auth.service;

import java.util.HashMap;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.itrip.common.SystemConfig;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

@Service("smsService")
public class SmsServiceImpl implements SmsService {

	private Logger logger = Logger.getLogger(SmsServiceImpl.class);
	@Resource
	private SystemConfig systemConfig;

	@Override
	public void send(String to, String templatedId, String[] datas) throws Exception {
		HashMap<String,Object> result = null;

		//初始化SDK
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

		/*
		初始化服务器地址和端口
		沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com","8883");
		生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com","8883");
		 */
		restAPI.init(systemConfig.getSmsServerIP(),systemConfig.getSmsServerPort());


		/*
		初始化主账号和账号令牌，对应官网开发者主账号下的ACCOUNT SID 和 AUTH TOKEN
		ACCOUNT SID 和 AUTH TOKEN 在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取
		参数顺序：第一个参数是ACCOUNT SID,第二个参数是AUTH TOKEN
		 */
		restAPI.setAccount(systemConfig.getSmsAccountSid(),systemConfig.getSmsAuthToken());


		/*
		初始化应用ID
		测试开发科使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的APP ID
		应用ID的获取：登陆官网，在“应用-应用列表”点击应用名称，看应用详情获取APP ID
		 */
		restAPI.setAppId(systemConfig.getSmsAppID());


		/*
		调用发送模板短信的接口发送短信
		参数顺序：
		第一个参数：要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号；
		第二个参数：模板ID，在平台创建的短信模板的ID值；测试时可使用系统的默认模板，ID=1；
		（系统默认模板内容：【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入）
		第三个 参数：要替换的内容数组。
		 */
		result = restAPI.sendTemplateSMS(to,templatedId,datas);

		System.out.println("SDKTestGetSubAccounts result="+ result);

		if ("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key:keySet){
				Object object = data.get(key);
				System.out.println(key + "=" + object);
			}
		}else {
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode")+"错误信息="+result.get("statusMsg"));
			logger.error("错误码=" + result.get("statusCode")+"错误信息="+result.get("statusMsg"));
			throw new Exception("错误码=" + result.get("statusCode")+"错误信息="+result.get("statusMsg"));

		}
	}
}
