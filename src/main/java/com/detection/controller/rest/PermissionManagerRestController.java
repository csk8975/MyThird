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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.detection.model.user.CrUser;
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
     * @function 注销并清除token
     * 
     */
    @RequestMapping(value = { "/userLogout" }, method = RequestMethod.GET)
    public ModelAndView userLogout(@RequestParam String userName){
        ModelAndView mv = new ModelAndView("redirect:/");
        mv.addObject("result", userControlService.userLogout(userName));
        return mv;
        
    }
    
    @RequestMapping(value = { "/submitNewPass" }, method = RequestMethod.GET)
    public ModelAndView submitNewPass(@RequestParam String oldPass,@RequestParam String newPass1,@RequestParam String newPass2, HttpServletRequest request){
        String resultPath = "redirect:changePassword";
        JSONObject result = new JSONObject();
        result = userControlService.changePassword((String)request.getSession().getAttribute("userName"),oldPass,newPass1,newPass2);
        if(result.getIntValue("code")==200){
            resultPath="redirect:main";
        }
        ModelAndView mv = new ModelAndView(resultPath);
        mv.addObject("result", result);
        return mv;
        
    }

}
