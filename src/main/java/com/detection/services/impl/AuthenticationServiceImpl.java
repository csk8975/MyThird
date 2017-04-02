package com.detection.services.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.detection.model.user.CrUser;
import com.detection.model.user.UserRepository;
import com.detection.services.AuthenticationService;

/**
 * @fileName AuthenticationServiceImpl.java
 * @author csk
 * @createTime 2017年3月6日 下午3:01:28
 * @version 1.0
 * @function
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean isPermitted(HttpServletRequest request, int permittedRole) {
        // TODO Auto-generated method stub
        boolean result = false;
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");
        String token = (String) session.getAttribute("token");
        int role = Integer.parseInt((String) session.getAttribute("role"));
        if (userName != null && token != null) {
            CrUser user = userRepo.findByUserName(userName);
            if (user != null && isTokenValid(user,token)
                    && role == permittedRole) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean isTokenValid(CrUser user, String token) {
        // TODO Auto-generated method stub
        boolean result = false;
        if(user.getToken().equalsIgnoreCase(token)){
            Date currentTime = new Date();
            if((currentTime.getTime() - user.getTokenUpdateTime().getTime())<=1800000){
                user.setTokenUpdateTime(currentTime);
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean isLoggedin(HttpServletRequest request) {
        // TODO Auto-generated method stub
        boolean result = false;
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");
        String token = (String) session.getAttribute("token");
        if (userName != null && token != null ) {
            CrUser user = userRepo.findByUserName(userName);
            if (user != null && isTokenValid(user,token)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String getUserRealName(HttpServletRequest request) {
        // TODO Auto-generated method stub
        String result = null;
        String userName = (String)request.getSession().getAttribute("userName");
        if(userName!=null && !userName.equals("")){
            CrUser user = userRepo.findByUserName(userName);
            if(user!=null){
                result = user.getRealName();
            }
        }
        return result;
    }
    

}
