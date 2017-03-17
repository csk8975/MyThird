/*
 * File Name：PermissionManagerRestController.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年3月1日 上午9:23:11
 */
package com.detection.controller.rest;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.detection.model.user.User;
import com.detection.model.user.UserRepository;
import com.detection.services.UserControlService;
import com.detection.util.EncryptionHelper;

/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年3月1日 上午9:23:11
 */

@RestController
public class PermissionManagerRestController {

    @Autowired
    private UserControlService userControlService;

    /**
     * @author lcc
     * @version 1.0
     * @function 校验用户权限接口，传入用户的token，返回用户的权限和用户名（code,200），如果对应的token不存在则，
     *           code返回201 用户权限：1表示具备查看，所有页面权限，0表示，仅仅具有查看单个页面的权限
     *
     */
    @RequestMapping(value = { "/verifyToken" }, method = RequestMethod.GET)
    public JSONObject verifyToken(@RequestParam String token) {
        return userControlService.verifyToken(token);
    }

    /**
     * @author lcc
     * @version 1.0
     * @throws Exception
     * @function 用户登录的API，其中loginName为登录名，userPassword前端使用md5加密后传输,
     *           账号密码正确返回code200，账号密码不正确返回code = 201，role=0, userName=null
     * 
     */
    @RequestMapping(value = { "/userLogin" }, method = RequestMethod.GET)
    public JSONObject userLogin(@RequestParam String loginName, @RequestParam String userPassword, HttpServletRequest request) throws Exception {
        JSONObject result = userControlService.userLogin(loginName, userPassword);
        HttpSession session = request.getSession();
        if(result.getIntValue("code") == 200){
            session.setAttribute("userName", loginName);
            session.setAttribute("token", result.getString("token"));
            session.setAttribute("role", result.getString("role"));
        }
        return result;
    }

    /**
     * @author csk
     * @version 1.0
     * @throws Exception
     * @function 添加用户
     * 
     */
    @RequestMapping(value = { "/addUser" }, method = RequestMethod.GET)
    public JSONObject addUser(@RequestParam String userName, String userPassword, String role) throws Exception {
        return userControlService.addUser(userName, userPassword, role);
    }
    
    /**
     * @author csk
     * @version 1.0
     * @throws Exception
     * @function 注销并清除token
     * 
     */
    @RequestMapping(value = { "/userLogout" }, method = RequestMethod.GET)
    public JSONObject userLogout(@RequestParam String userName){
        return userControlService.userLogout(userName);
        
    }
}
