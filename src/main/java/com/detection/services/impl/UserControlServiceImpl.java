package com.detection.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.detection.model.user.CrUser;
import com.detection.model.user.UserRepository;
import com.detection.services.UserControlService;
import com.detection.util.EncryptionHelper;

/**
 * @fileName UserControlServiceImpl.java
 * @author csk
 * @createTime 2017年3月1日 下午5:26:18
 * @version 1.0
 * @function
 */

@Service
public class UserControlServiceImpl implements UserControlService {

    @Autowired
    private UserRepository userRepository;

    /**
     * @author csk
     * @version 1.0
     * @function 校验用户权限接口，传入用户的token，返回用户的权限和用户名（code,200），如果对应的token不存在则，
     *           code返回201 用户权限：1表示具备查看，所有页面权限，0表示，仅仅具有查看单个页面的权限
     */
    @Override
    public JSONObject verifyToken(String token) {
        JSONObject result = new JSONObject();
        int code = 201;
        int role = 0;
        String userName = null;
        String message = "Fail.User Not Found.";
        List<CrUser> userList = userRepository.findByToken(token.toUpperCase());
        if (userList != null && userList.size() != 0) {
            if (userList.size() == 1) {
                CrUser user = userList.get(0);
                Date currentTime = new Date();
                // 1800000 = 30min = 1800s = 1800000ms
                // 表示token更新时间小于30分钟为有效
                if(user.getTokenUpdateTime()!=null){
                    if (( currentTime.getTime() - user.getTokenUpdateTime().getTime()) <= 1800000) {
                        userName = user.getUserName();
                        user.setTokenUpdateTime(currentTime);
                        userRepository.save(user);
                        code = 200;
                        role = user.getRole();
                        message = "Success.";
                    }
                    else{
                        code = 201;
                        role = 0;
                        message = "Fail. Token has expired. Try Login again.";
                    }
                }
            } else {
                code = 201;
                role = 0;
                message = "Fail.More than one user found by this token.";
            }
        }
        result.put("code", code);
        result.put("role", role);
        result.put("userName", userName);
        result.put("message", message);
        return result;
    }

    /**
     * @author csk
     * @version 1.0
     * @throws Exception
     * @function 添加用户
     * 
     */
    @Override
    public JSONObject addUser(String userName, String userPassword, String role) throws Exception {
        JSONObject result = new JSONObject();
        int code = 201;
        String message = "Fail.Unknown Reason";
        if (!userName.equals("") && userName != null && !userPassword.equals("") && userPassword != null
                && !role.equals("") && role != null) {
            if (userRepository.findByUserName(userName) != null) {
                code = 201;
                message = "User Already Exist! Try other Name.";
            } else {
                CrUser user = new CrUser();
                user.setRole(Integer.parseInt(role));
                user.setUserName(userName);
                user.setUserPassword(EncryptionHelper.encryptStringByMD5(userPassword));
                userRepository.save(user);
                code = 200;
                message = "Success!";
            }
        }
        result.put("code", code);
        result.put("message", message);

        return result;
    }

    /**
     * @author csk
     * @version 1.0
     * @throws Exception
     * @function 用户登录的API，其中loginName为登录名，userPassword前端使用md5加密后传输,
     *           账号密码正确返回code200，账号密码不正确返回code = 201，role=0, userName=null
     * 
     */
    @Override
    public JSONObject userLogin(String loginName, String userPassword) throws Exception {
        JSONObject result = new JSONObject();
        Date loginTime = new Date();
        String token = null;
        int code = 201;
        int role = 0;
        
        CrUser currentUser = userRepository.findByUserName(loginName);
        if (currentUser != null && currentUser.getUserPassword().equalsIgnoreCase(userPassword)) {
            code = 200;
            role = currentUser.getRole();
            token = EncryptionHelper.getUserToken(loginName, loginTime);
            currentUser.setLoginTime(loginTime);
            currentUser.setToken(token);
            currentUser.setTokenUpdateTime(loginTime);
            userRepository.save(currentUser);
        }

        result.put("token", token);
        result.put("role", role);
        result.put("userName", loginName);
        result.put("code", code);

        return result;
    }

    /**
     * @author csk
     * @version 1.0
     * @throws Exception
     * @function 注销并清除token
     * 
     */
    @Override
    public JSONObject userLogout(String userName) {
        JSONObject result = new JSONObject();
        int code = 201;
        String message = "Fail.";
        CrUser currentUser = userRepository.findByUserName(userName);
        // TODO Auto-generated method stub
        if (currentUser != null){
            currentUser.setLastLogin(currentUser.getLoginTime());
            currentUser.setToken("");
            currentUser.setTokenUpdateTime(null);
            currentUser.setLoginTime(null);
            userRepository.save(currentUser);
            code = 200;
            message="Success.";
        }
        result.put("code", code);
        result.put("message", message);
        return result;
    }

    
    @Override
    public JSONObject changePassword(String userName, String oldPass, String newPass1, String newPass2) {
        int code = 201;
        String message = "";
        JSONObject result = new JSONObject();
        CrUser user = userRepository.findByUserName(userName);
        if(user.getUserPassword().equalsIgnoreCase(oldPass)){
            if(newPass1.equals(newPass2)){
                user.setUserPassword(newPass1);
                userRepository.save(user);
                code = 200;
                message = "success.";
            }
            else{
                code = 201;
                message = "fail. two new password not the same.";
            }
            
        }
        else{
            code = 201;
            message ="fail,wrong pass.";
        }
        result.put("code",code);
        result.put("message", message);
        return result;
    }
}
