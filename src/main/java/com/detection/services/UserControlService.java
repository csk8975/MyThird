package com.detection.services;

import com.alibaba.fastjson.JSONObject;

/**
 * @fileName UserControlService.java
 * @author csk
 * @createTime 2017年3月1日 下午5:18:44
 * @version 1.0
 * @function
 */

public interface UserControlService {
    public JSONObject verifyToken(String token);
    public JSONObject addUser(String userName, String userPassword, String role) throws Exception;
    public JSONObject userLogin(String loginName, String userPassword) throws Exception;
    public JSONObject userLogout(String userName);
}
