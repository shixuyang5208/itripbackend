package cn.itrip.auth.controller;

import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
第三方登录控制器
 */
@Controller
@RequestMapping("/vendors")
public class VedorsController {

    @Resource
    private UserService userService;
    @Resource
    private TokenService tokenService;

    /**
     * 微信登录第一步：微信授权获取code(微信支付）
     * appid:公众号唯一标识
     * redirect_uri：返回类型
     * #wechat_redirect:无论直接打开还是做页面302重定向的时候，必须带此参数
     * @param response
     */
    @RequestMapping("/wechat/login")
    public void wechatLogin(HttpServletResponse response){
        String qrconnect = "https://open.weixin.qq.com/connect/qrconnect?appid=wx9168f76f000a0d4c&redirect_uri=" +
                "http%3a%2f%2fitrip.project.bdqn.cn%2fauth%2fvendors%2fwechat%2fcallback&response_type=code&scope=" +
                "snsapi_login&state=STATE#wechat_redirect";
        try {
            response.sendRedirect(qrconnect);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
