package cn.itrip.auth.controller;

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

}
