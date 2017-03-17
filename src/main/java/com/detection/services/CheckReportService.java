/*
 * File Name：CheckReportService.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月21日 下午4:37:13
 */
package com.detection.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.detection.model.report.entities.CheckReport;

/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年2月21日 下午4:37:13
 */
public interface CheckReportService {

    /**
     * @author csk
     * @version 1.0
     * @throws IOException 
     * @throws Exception 
     * @function 上传报告文件，解析并保存到数据库
     */
    public boolean uploadAndSaveReport(String path, MultipartFile file) throws IOException, Exception;
    
    /**
     * @author csk
     * @version 1.0
     * @throws IOException 
     * @function 解析文件并保存到数据库
     */
    public boolean parseAndSaveReportToDB(String upFilePath, String downloadPath) throws IOException;
    
    /**
     * @author csk
     * @version 1.0
     * @throws IOException 
     * @function 按报告编号删除一份报告
     */
    public void deleteReportByReportNum(String reportNum);
    
    /**
     * @author csk
     * @version 1.0
     * @throws IOException 
     * @function 查找所有报告
     */
    public JSONObject getAllReports();
    
    /**
     * @author csk
     * @version 1.0
     * @throws IOException 
     * @function 按条件查找报告
     */
    public JSONObject getReportByCondition(String projectName, String reportNum, String riskLevel, String qaName);
    
    /**
     * @author csk
     * @version 1.0
     * @function 获取评估项目概况表的摘要信息，包括：
     * reportNum ： 项目号
     * reportDate : 评估日期
     * projectName : 被评估单位名称
     * riskLevel : 极高水平（4）、高水平（3）、中等水平（2）、低水平（1） 
     * code : 200 success, 201 failure
     */
    public JSONObject getAbstractReportInfo(String reportNum);
    
    /**
     * @author csk
     * @version 1.0
     * @function 获取报告详情列表
     */
    public JSONObject getDetailReportInfo(String verifyToken);
    
    /**
     * @author csk
     * @version 1.0
     * @throws Exception 
     * @function 处理风险评估报告责任提取码验证
     * 校验reportNum、责任人dutyPerson和责任人电话dutyTel
     * 返回校验token
     */
    public JSONObject submitExtractCode(String reportNum, String dutyPerson, String dutyTel) throws Exception;
    
    /**
     * @author csk
     * @version 1.0
     * @function 获取文件路径
     */
    public JSONObject getReportPath(String fetchCode);
    
    public boolean updateRiskLevel(String reportNum);
    
    public void updateAllRiskLevel();
}

