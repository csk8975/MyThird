package com.detection.services;

import javax.servlet.http.HttpServletRequest;

import com.detection.model.user.User;

/**
 * @fileName AuthenticationService.java
 * @author csk
 * @createTime 2017年3月6日 下午2:54:42
 * @version 1.0
 * @function
 */

public interface AuthenticationService {

    public boolean isTokenValid(User user, String token);
    public boolean isPermitted(HttpServletRequest request, int permittedRole);
    public boolean isLoggedin(HttpServletRequest request);
    
}
