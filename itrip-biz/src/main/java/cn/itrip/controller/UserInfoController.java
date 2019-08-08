package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.pojo.ItripUserLinkUser;
import cn.itrip.beans.vo.userinfo.ItripAddUserLinkUserVO;
import cn.itrip.beans.vo.userinfo.ItripModifyUserLinkUserVO;
import cn.itrip.beans.vo.userinfo.ItripSearchUserLinkUserVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ValidationToken;
import cn.itrip.service.orderlinkuser.ItripOrderLinkUserServiceImpl;
import cn.itrip.service.userlinkuser.ItripUserLinkUserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/api/userinfo")
public class UserInfoController {
    private Logger logger = Logger.getLogger(UserInfoController.class);
    @Resource
    private ItripUserLinkUserService linkUserService;
    @Resource
    private ItripOrderLinkUserServiceImpl itripOrderLinkUserService;
    @Resource
    private ValidationToken validationToken;


    @RequestMapping(value = "/adduserlinkuser",method= RequestMethod.POST)
    @ResponseBody
    public Dto addItripUserLinkUser(@RequestBody ItripAddUserLinkUserVO addUserLinkUserVO, HttpServletRequest request){
        String tokenString = request.getHeader("token");
        logger.debug("token name is from header"+tokenString);
        ItripUser currentUser = validationToken.getCurrentUser(tokenString);
        if (currentUser != null && addUserLinkUserVO != null){
            ItripUserLinkUser itripUserLinkUser = new ItripUserLinkUser();
            itripUserLinkUser.setLinkUserName(addUserLinkUserVO.getLinkUserName());
            itripUserLinkUser.setLinkIdCardType(addUserLinkUserVO.getLinkIdCardType());
            itripUserLinkUser.setLinkIdCard(addUserLinkUserVO.getLinkIdCard());
            itripUserLinkUser.setUserId(currentUser.getId());
            itripUserLinkUser.setLinkPhone(addUserLinkUserVO.getLinkPhone());
            itripUserLinkUser.setCreatedBy(currentUser.getId());//itripUserLinkUser.setCreatedBy(addUserLinkUserVO.getId());
            try {
                linkUserService.addItripUserLinkUser(itripUserLinkUser);
            } catch (Exception e){
                return DtoUtil.returnFail("新增联系人失败","100411");
            }
            return DtoUtil.returnSuccess("新增成功！");
        }else if (currentUser != null && addUserLinkUserVO == null){
            return DtoUtil.returnFail("不能提交空值，请填写联系人信息","100412");
        }else {
            return DtoUtil.returnFail("登录失效（token失效），请重新登录","100000");
        }
    }

    @RequestMapping("deluserlinkuser")
    @ResponseBody
    public Dto deleteItripUserLinkUser(@RequestParam Long[] ids,HttpServletRequest request) {
        String tokenString = request.getHeader("token");
        logger.debug("token name is from header" + tokenString);
        ItripUser currentUser = validationToken.getCurrentUser(tokenString);
        List<Long> idsList = new ArrayList<Long>();
        if (currentUser != null && EmptyUtils.isNotEmpty(ids)) {
            try {
                List linkUserIds = itripOrderLinkUserService.getItripOrderLinkUserIdByOrder();
                Collections.addAll(idsList, ids);
                linkUserIds.retainAll(idsList);
                if (linkUserIds.size() > 0) {
                    return DtoUtil.returnFail("所选删除联系人中有未完成订单，无法删除", "100431");
                } else {
                    linkUserService.deletItripUserLinkUserById(ids);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("删除常用联系人失败", "100432");
            }
            return DtoUtil.returnSuccess("删除联系人成功！");
        } else if (currentUser != null && EmptyUtils.isEmpty(ids)) {
            return DtoUtil.returnFail("请选择要删除的联系人", "100433");
        } else {
            return DtoUtil.returnFail("登录失效（token失效），请重新登录", "100000");
        }
    }


    @RequestMapping("modifyuserlinkuser")
    @ResponseBody
    public Dto updateUserLinkUser(@RequestBody ItripModifyUserLinkUserVO modifyUserLinkUserVO,HttpServletRequest request){
        String tokenString = request.getHeader("token");
        logger.debug("token name is from header"+tokenString);
        ItripUser currentUser = validationToken.getCurrentUser(tokenString);
        if (currentUser != null && modifyUserLinkUserVO != null){
            ItripUserLinkUser itripUserLinkUser = new ItripUserLinkUser();
            itripUserLinkUser.setUserId(modifyUserLinkUserVO.getId());
            itripUserLinkUser.setLinkUserName(modifyUserLinkUserVO.getLinkUserName());
            itripUserLinkUser.setLinkPhone(modifyUserLinkUserVO.getLinkPhone());
            itripUserLinkUser.setLinkIdCard(modifyUserLinkUserVO.getLinkIdCard());
            itripUserLinkUser.setLinkIdCardType(modifyUserLinkUserVO.getLinkIdCardType());
            itripUserLinkUser.setModifyDate(new Date(System.currentTimeMillis()));
            itripUserLinkUser.setModifiedBy(currentUser.getId());
            try {
                linkUserService.modifyItripUserLinkUser(itripUserLinkUser);
                return DtoUtil.returnSuccess("修改联系人成功！");
            } catch ( Exception e){
                e.printStackTrace();
                return DtoUtil.returnFail("新增联系人失败","100421");
            }
        }
        return DtoUtil.returnFail("登录失效（token失效），请重新登录","100000");
    }





    @RequestMapping("queryuserlinkuser")
    @ResponseBody
    public Dto queryItripUserLinkUser(@RequestBody ItripSearchUserLinkUserVO searchUserLinkUserVO, HttpServletRequest request){
        String tokenString = request.getHeader("token");
        logger.debug("token name is from header"+tokenString);
        ItripUser currentUser = validationToken.getCurrentUser(tokenString);
        List<ItripUserLinkUser> userLinkUsersList = new ArrayList<ItripUserLinkUser>();
        String linkUserName = (searchUserLinkUserVO == null)?null:searchUserLinkUserVO.getLinkUserName();
        Dto dto = null;
        if (currentUser != null){
            ItripUserLinkUser itripUserLinkUser = new ItripUserLinkUser();
            itripUserLinkUser.setUserId(currentUser.getId());
            itripUserLinkUser.setLinkUserName(linkUserName);
            try {
                userLinkUsersList = linkUserService.getItripUserLinkUserByItself(itripUserLinkUser);
                return DtoUtil.returnSuccess("获取联系人成功！");
            } catch (Exception e){
                e.printStackTrace();
                return DtoUtil.returnFail("获取联系人失败","100401");
            }
        } else {
            return DtoUtil.returnFail("登录失效（token失效）","100000");
        }
    }
}
