/*
 * File Name：ReportRestController.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月21日 上午9:34:03
 */
package com.detection.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.detection.services.AuthenticationService;
import com.detection.services.CheckReportService;

/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年2月21日 上午9:34:03
 */

@RestController
public class ReportRestController {

    @Autowired
    private CheckReportService service;
    @Autowired
    private AuthenticationService authService;
    
    
    @RequestMapping(value = {"/getReportList"} , method = RequestMethod.GET)
    public JSONObject getReportList( HttpServletRequest request ){
        //
        int permittedRole = 1;
        JSONObject result = new JSONObject();
        result.put("code", 201);
        result.put("message", "fail");
        if(authService.isLoggedin(request) && authService.isPermitted(request, permittedRole)){
            result = service.getAllReports();
        }
        return result;
    }
    
    @RequestMapping(value = { "/deleteReportByReportNum" }, method = RequestMethod.GET)
    public JSONObject deleteReportByReportNum(@RequestParam String reportNum, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        int permittedRole = 1;
        if (!authService.isLoggedin(request)) {
            result.put("code", 201);
            result.put("message", "请先登录！");
        } else if (!authService.isPermitted(request, permittedRole)) {
            result.put("code", 201);
            result.put("message", "您没有权限！");
        } else if (reportNum != null) {
            result = service.deleteReportByReportNum(reportNum);
        }
        return result;
    }

}

