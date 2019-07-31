package cn.itrip.auth.controller;

import cn.itrip.auth.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserService userService;

}
