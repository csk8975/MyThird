package com.detection.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private String userName;
    @Column(length = 32)
    private String userPassword;
    @Column(length = 32)
    private String token;
    private Date tokenUpdateTime;
    private Date lastLogin;
    private Date loginTime;
    private int role;

    public User() {

    }

    public User(User user) {
        this.userName = user.getUserName();
        this.userPassword = user.getUserPassword();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenUpdateTime() {
        return tokenUpdateTime;
    }

    public void setTokenUpdateTime(Date tokenUpdateTime) {
        this.tokenUpdateTime = tokenUpdateTime;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}
