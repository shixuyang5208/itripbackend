package cn.itrip.auth.controller;

import cn.itrip.common.EmptyUtils;
import cn.itrip.common.MD5;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;
import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.userinfo.ItripUserVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.ErrorCode;

/**
 * 用户管理控制器
 * @author hduser
 *
 */
@Controller
@RequestMapping(value = "/api")
public class UserController {

    @Resource
    private UserService userService;

    @ApiIgnore
    @RequestMapping("/register")
    public String showRegisterForm(){
        return "register";
    }


    /**
     * 使用邮箱注册
     * @param userVO
     * @return
     */
    @RequestMapping(value = "/doregister",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Dto doRegister(@RequestBody ItripUserVO userVO){
        if (!validEmail(userVO.getUserCode())){
            return DtoUtil.returnFail("请使用正确的邮箱地址注册",ErrorCode.AUTH_ILLEGAL_USERCODE);
        }
        try {
            ItripUser user = new ItripUser();
            user.setUserCode(userVO.getUserCode());
            user.setUserPassword(userVO.getUserPassword());
            user.setUserType(0);
            user.setUserName(userVO.getUserName());

            if (userService.findByUsername(user.getUserCode()) == null){
                user.setUserPassword(MD5.getMd5(user.getUserPassword(),32));
                userService.txcreateUser(user);
                return DtoUtil.returnSuccess("注册成功");
            } else {
                return DtoUtil.returnFail("用户已存在，请更换邮箱账号注册",ErrorCode.AUTH_USER_ALREADY_EXISTS);
            }
        } catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_UNKNOWN);
        }
    }


    /**
     * 激活邮箱用户
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value = "/activate",method = RequestMethod.PUT,produces = "application/json")
    @ResponseBody
    public Dto active(@RequestParam String user,@RequestParam String code
                      ){
        try {
            if (userService.active(user,code)){
                return DtoUtil.returnSuccess("激活成功");
            } else {
                return DtoUtil.returnFail("激活失败",ErrorCode.AUTH_ACTIVATE_FAILED);
            }
        } catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("激活失败",ErrorCode.AUTH_ACTIVATE_FAILED);
        }
    }


    /**
     * 使用手机注册
     * @param itripUserVO
     * @return
     */
    @RequestMapping(value = "/registerbyphone",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Dto registerByPhone(@RequestBody ItripUserVO itripUserVO){
        try {
            if (!validPhone(itripUserVO.getUserCode())){
                return DtoUtil.returnFail("注册失败，请使用正确手机号",ErrorCode.AUTH_ILLEGAL_USERCODE);
            }

            ItripUser user = new ItripUser();
            user.setUserCode(itripUserVO.getUserCode());
            user.setUserPassword(itripUserVO.getUserPassword());
            user.setUserType(0);
            user.setUserName(itripUserVO.getUserName());
            if (userService.findByUsername(user.getUserCode()) == null){
                user.setUserPassword(MD5.getMd5(user.getUserPassword(),32));
                userService.txCreateUserByPhone(user);
                return DtoUtil.returnSuccess();
            } else {
                return DtoUtil.returnFail("用户已存在，注册失败",ErrorCode.AUTH_USER_ALREADY_EXISTS);
            }
        } catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_UNKNOWN);
        }
    }



    /**
     * 手机短信验证
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value = "/validatephone",method = RequestMethod.PUT,produces = "application/json")
    @ResponseBody
    public Dto validatePhone(@RequestParam String user,@RequestParam String code){
        try {
            if (userService.validatePhone(user,code)){
                return DtoUtil.returnSuccess("验证成功");
            } else {
                return DtoUtil.returnFail("验证失败",ErrorCode.AUTH_UNKNOWN);
            }
        } catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("验证失败",ErrorCode.AUTH_UNKNOWN);
        }
    }



    /**
     * 检查用户是否已经注册
     * @param name
     * @return
     */
    @RequestMapping(value = "/ckusr",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public Dto checkUser(@RequestParam String name){
        try {
            if (userService.findByUsername(name) == null){
                return DtoUtil.returnSuccess("用户尚未注册");
            }else {
                return DtoUtil.returnFail("用户已存在，注册失败",ErrorCode.AUTH_USER_ALREADY_EXISTS);
            }
        } catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_UNKNOWN);
        }
    }

    /*
    合法E-mail地址：
    1.必须包含一个并且只有一个符号“@”
    2.第一个字符不能是“@”或“.”
    3.不允许出现“@.”或者“.@”
    4.结尾不得是字符 “@”或“.”
    5.允许“@”前的字符中出现“＋”
    6.不允许“＋”在最前面，或者“＋@”
     */
    private boolean validEmail(String email){
        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        return Pattern.compile(regex).matcher(email).find();
    }

    /**
     * 验证手机号是否合法
     * @param phone
     * @return
     */
    private boolean validPhone(String phone){
        String regex = "^1[3578]{1}\\d{9}$";
        return Pattern.compile(regex).matcher(phone).find();
    }
}
