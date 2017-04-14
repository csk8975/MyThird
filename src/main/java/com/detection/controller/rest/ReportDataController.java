/*
 * File Name：ReportDataController.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月22日 下午2:53:28
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
import com.detection.services.CheckReportService;

/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年2月22日 下午2:53:28
 * @function 前端页面请求控制器
 */
@RestController
@RequestMapping("report")
public class ReportDataController {
    
    @Autowired
    private CheckReportService checkReportService;
    
    /**
     * @author lcc
     * @version 1.0
     * @throws Exception 
     * @function 处理风险评估报告责任提取码验证
     * 校验reportNum、责任人dutyPerson和责任人电话dutyTel
     * 返回校验token
     */
    @RequestMapping(value = {"submitExtractCode" }, method = RequestMethod.GET)
    public JSONObject submitExtractCode(@RequestParam String extracteCode, 
            @RequestParam String ownerName, @RequestParam String dutyTel, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        JSONObject result = checkReportService.submitExtractCode(extracteCode, ownerName, dutyTel);
        JSONObject finalResult  = new JSONObject();
        String token = result.getString("verifyToken");
        String dutyPerson = result.getString("dutyPerson");
        if(dutyPerson!=null && token != null && dutyTel !=null){
            session.setAttribute("ownerToken", token);
            session.setAttribute("watermark", dutyPerson+dutyTel);
        }
        finalResult.put("code", result.getString("code"));
        finalResult.put("message", result.getString("message"));
        return finalResult;
    }
    
    @RequestMapping(value = {"getDetailReportInfo" }, method = RequestMethod.GET)
    public JSONObject getDetailReportInfo(@RequestParam String verifyToken, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String ownerToken = (String)session.getAttribute("ownerToken");
        JSONObject result = new JSONObject();
        if(verifyToken.equalsIgnoreCase(ownerToken)){
            result = checkReportService.getDetailReportInfo(verifyToken);
        }
        else{
            result.put("code", 201);
            result.put("message", "fail");
        }
        return result;
    }
    
    /**
     * @author lcc
     * @version 1.0
     * @function 获取评估项目概况表的摘要信息，包括：
     * reportNum ： 项目号
     * reportDate : 评估日期
     * projectName : 被评估单位名称
     * riskLevel : 极高水平（4）、高水平（3）、中等水平（2）、低水平（1） 
     * code : 200 success, 201 failure
     */
    @RequestMapping(value = {"getAbstractReportInfo" }, method = RequestMethod.GET)
    public JSONObject getAbstractReportInfo(@RequestParam String reportNum){

        return checkReportService.getAbstractReportInfo(reportNum);
    }
    
    /**
     * @author lcc
     * @version 1.0
     * @function 根据token请求水印数据
     * @param verifyToken  
     */
    @RequestMapping(value = {"getWatermark"}, method = RequestMethod.GET)
    public JSONObject getWatermark(@RequestParam String verifyToken, HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        
        obj.put("code", 200);   //false == 201
        obj.put("message", "succes");
        String watermark = (String) request.getSession().getAttribute("watermark");
        obj.put("watermark", watermark);
        return obj;
    }
}

