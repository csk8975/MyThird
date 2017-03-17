/*
 * File Name：CheckReportServiceImpl.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月21日 下午4:37:46
 */
package com.detection.services.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.detection.config.LevelWeightProperties;
import com.detection.config.RiskLevelBoundary;
import com.detection.model.owner.OwnerUnit;
import com.detection.model.owner.OwnerUnitRepository;
import com.detection.model.pdfparse.Cover;
import com.detection.model.pdfparse.ListResult;
import com.detection.model.pdfparse.PDFParserResult;
import com.detection.model.pdfparse.Result;
import com.detection.model.report.entities.CheckItemDetail;
import com.detection.model.report.entities.CheckReport;
import com.detection.model.report.entities.CheckReportInfo;
import com.detection.model.report.entities.CheckReportResultStat;
import com.detection.model.report.entities.CheckReportUnqualifiedItemDetail;
import com.detection.model.report.repositories.CheckItemDetailRepository;
import com.detection.model.report.repositories.CheckReportRepository;
import com.detection.model.report.repositories.CheckReportResultStatRepository;
import com.detection.services.CheckReportService;
import com.detection.services.PDFParserService;
import com.detection.util.DateUtil;
import com.detection.util.EncryptionHelper;

@Service
public class CheckReportServiceImpl implements CheckReportService {
    // @Autowired
    // private CheckReportMapper mapper;

    @Autowired
    private CheckReportRepository checkReportRepo;
    @Autowired
    private OwnerUnitRepository ownerUnitRepo;
    @Autowired
    private PDFParserService pdfParser;
    @Autowired
    private LevelWeightProperties weight;
    @Autowired
    private RiskLevelBoundary boundary;

    @Value("${uploadPath}")
    private String uploadPath;

    @Value("${downloadPath}")
    private String downloadPath;
    
    @Value("${detectionLevelPrefix}")
    private String detectionLevelPrefix;
    
    @Value("${isDebug}")
    private boolean isDebug;

    @Override
    public boolean uploadAndSaveReport(String fileName, MultipartFile file) throws Exception {
        boolean result = false;
        fileName = EncryptionHelper.encryptFileNameByMD5(fileName);
        String upFilePath = uploadPath + fileName;
        String downFilePath = downloadPath + fileName;

        File outPath = new File(uploadPath);
        if (!outPath.exists()) {
            outPath.mkdirs();
        }
        File upFile = new File(upFilePath);
        if(isDebug){
            if(upFile.canWrite()){
                System.out.println("file can write!");
            }
            else{
                System.out.println("file can not write!");
            }
        }

        
        FileOutputStream upFileOS = new FileOutputStream(upFile);
        BufferedOutputStream out = new BufferedOutputStream(upFileOS);
        
        out.write(file.getBytes());
        out.flush();
        //System.out.println("file length: "+upFile.length());
        upFileOS.close();
        out.close();
        parseAndSaveReportToDB(upFilePath, downFilePath);
        result = true;

        return result;
    }

    @Override
    public boolean parseAndSaveReportToDB(String upFilePath, String downloadPath) throws IOException {

        boolean result = false;
        // 解析报告并入库
        File upFile = new File(upFilePath);
        if(isDebug){
            if(upFile.canRead()){
                System.out.println("file can read!");
            }
            else{
                System.out.println("file can not read!");
            }
        
            System.out.println("start parsing=====>>>>>>>>>>>");
        }
        PDFParserResult parseResult = pdfParser.parse(upFile);
        //System.out.println("file length: "+upFile.length());
        Cover reportCover = parseResult.getCover();
        List<Result> firstPart = parseResult.getFirstPart();
        // String reportConclusion = parseResult.getSecondPart();
        List<Result> thirdPart = parseResult.getThirdPart();
        List<ListResult> forthPart = parseResult.getForthPart();
        // TODO delete old report
        if (reportCover.getReportNum() != null) {
            deleteReportByReportNum(reportCover.getReportNum());
        }

        // 保存report对象
        CheckReport checkReport = new CheckReport();
        CheckReportInfo checkReportInfo = new CheckReportInfo();
        List<CheckReportResultStat> checkReportStatList = new ArrayList<CheckReportResultStat>();
        List<CheckItemDetail> checkItemDetailList = new ArrayList<CheckItemDetail>();
        List<CheckReportUnqualifiedItemDetail> checkReportUnqualifiedItemList = new ArrayList<CheckReportUnqualifiedItemDetail>();
        // report basic info
        checkReport.setReportNum(reportCover.getReportNum());
        checkReport.setCreateDate(new Date());
        checkReport.setModifyDate(new Date());

        // process on report cover info
        checkReportInfo.setReportNum(reportCover.getReportNum());
        checkReportInfo.setAgentName(reportCover.getAgentName());
        checkReportInfo.setContactFax(reportCover.getContactFax());
        checkReportInfo.setContactPostCode(reportCover.getContactPostcode());
        checkReportInfo.setContactTel(reportCover.getContactTel());
        checkReportInfo.setProjectAddress(reportCover.getProjectAddress());
        checkReportInfo.setProjectName(reportCover.getProjectName());
        checkReportInfo.setQaAddress(reportCover.getQaAddress());
        checkReportInfo.setQaName(reportCover.getQaName());
        checkReportInfo.setReportConclusion(reportCover.getReportConclusion());
        checkReportInfo.setFilePath(downloadPath);

        // process on first part
        Iterator<Result> it1 = firstPart.iterator();
        //System.out.println("第一部分处理开始=====>>>>>>>>>>>");
        while (it1.hasNext()) {
            CheckReportResultStat element = new CheckReportResultStat();
            Result nextItem = it1.next();
            element.setImportantGrade(nextItem.getLevel());
            if (nextItem.getValue1() != null && !nextItem.getValue1().equals("")) {
                element.setCheckNum(Integer.parseInt(nextItem.getValue1()));
            }
            element.setItemCode(nextItem.getLabel());
            element.setItemName(nextItem.getName());
            if (nextItem.getValue1() != null && !nextItem.getValue1().equals("")) {
                element.setUnqualifiedNum(Integer.parseInt(nextItem.getValue2()));
            }
            if(isDebug){
                System.out.println(element.getItemName() +"   " +element.getItemCode() +"   " +element.getCheckNum() +"   " +element.getUnqualifiedNum());
            }
            checkReportStatList.add(element);
        }

        // process on third part
        Iterator<Result> it2 = thirdPart.iterator();
        //System.out.println("\n\n第三部分处理开始=====>>>>>>>>>>>");
        while (it2.hasNext()) {
            CheckItemDetail element = new CheckItemDetail();
            Result nextItem = it2.next();
            element.setImportantGrade(nextItem.getLevel());
            if (nextItem.getValue1() != null && !nextItem.getValue1().equals("")) {
                element.setCheckNum(Integer.parseInt(nextItem.getValue1()));
            }
            element.setItemCode(nextItem.getLabel());
            element.setItemName(nextItem.getName());
            if (nextItem.getValue1() != null && !nextItem.getValue1().equals("")) {
                element.setUnqualifiedNum(Integer.parseInt(nextItem.getValue2()));
            }
            if(isDebug){
                System.out.println(element.getItemName() +"   " +element.getItemCode() +"   " +element.getCheckNum() +"   " +element.getUnqualifiedNum());
            }
            checkItemDetailList.add(element);
        }
        // TODO 第四第五部分保存内容
        // process on fourth part
        Iterator<ListResult> it3 = forthPart.iterator();
        //System.out.println("\n\n第四部分处理开始=====>>>>>>>>>>>");
        while (it3.hasNext()) {
            CheckReportUnqualifiedItemDetail element = new CheckReportUnqualifiedItemDetail();
            ListResult nextItem = it3.next();
            element.setImportantGrade(nextItem.getImportantGrade());
            element.setRequirements(nextItem.getRequirements());
            element.setTestItem(nextItem.getTestItem());
            element.setUnqualifiedCheckPointByStringList(nextItem.getNonstandardItems());
            if(isDebug){
                System.out.println(element.getTestItem() +"   " +element.getRequirements() +"   " +element.getImportantGrade());
            }
            checkReportUnqualifiedItemList.add(element);
        }

        checkReport.setCheckReportInfo(checkReportInfo);
        checkReport.setCheckItemDetail(checkItemDetailList);
        checkReport.setCheckReportResultStat(checkReportStatList);
        checkReport.setUnqualifiedItemDetail(checkReportUnqualifiedItemList);
        
        float riskScore = computeRiskScore(checkReport);
        checkReport.getCheckReportInfo().setRiskScore(riskScore);
        checkReport.getCheckReportInfo().setRiskLevel(computRiskLevel(riskScore));
        
        System.out.println("完成报告解析：\n风险评分: " +riskScore );
        System.out.println("报告号码："+reportCover.getReportNum());
        checkReportRepo.save(checkReport);
        result = true;

        return result;
    }

    @Override
    public void deleteReportByReportNum(String reportNum) {
        if (checkReportRepo.findOne(reportNum) != null) {
            checkReportRepo.delete(reportNum);
        }
    }

    @Override
    public JSONObject getAllReports() {
        JSONObject result = new JSONObject();
        int code = 200;
        String message = "success";

        List<CheckReport> reportlist = checkReportRepo.findAll();
        List<CheckReportInfo> dataList = new ArrayList<CheckReportInfo>();
        Iterator<CheckReport> it = reportlist.iterator();
        while (it.hasNext()) {
            dataList.add(it.next().getCheckReportInfo());
        }
        result.put("code", code);
        result.put("message", message);
        result.put("data", dataList);

        return result;
    }

    @Override
    public JSONObject getReportByCondition(String projectName, String reportNum, String riskLevel, String qaName) {
        // TODO Auto-generated method stub
        if (reportNum != null && !reportNum.equals("")) {
            CheckReport result = checkReportRepo.findOne(reportNum);
        }
        return null;
    }

    @Override
    public JSONObject getAbstractReportInfo(String reportNum) {
        // TODO Auto-generated method stub
        JSONObject result = new JSONObject();
        int code = 201;
        String message = "Fail.Report Not Found.";
        String reportDate = null;
        String projectName = null;
        String riskLevel = null;

        CheckReport report = checkReportRepo.findOne(reportNum);
        if (report != null) {
            code = 200;
            message = "success";
            reportDate = DateUtil.getYearMonthDateByHyphen(report.getCreateDate());
            projectName = report.getCheckReportInfo().getProjectName();
            riskLevel = report.getCheckReportInfo().getRiskLevel();
        }
        result.put("code", code);
        result.put("message", message);
        result.put("reportNum", reportNum);
        result.put("reportDate", reportDate);
        result.put("projectName", projectName);
        if(riskLevel.equalsIgnoreCase("危险等级1")){
            result.put("riskLevel", 1);
        }
        else if(riskLevel.equalsIgnoreCase("危险等级2")){
            result.put("riskLevel", 2);
        }
        else if(riskLevel.equalsIgnoreCase("危险等级3")){
            result.put("riskLevel", 3);
        }
        else{
            result.put("riskLevel", 4);
        }

        return result;
    }

    @Override
    public JSONObject getDetailReportInfo(String verifyToken) {
        // TODO Auto-generated method stub
        JSONObject result = new JSONObject();
        int code = 201;
        String message = "Fail";
        String reportNum = null;
        // int riskLevel = 0;
        String riskLevel = null;
        String riskScore = "";
        String reportDate = null;
        String reportConclusion = null;
        String rectifyComments = null;
        List<CheckReportUnqualifiedItemDetail> unqualifiedItemList = new ArrayList<CheckReportUnqualifiedItemDetail>();
        String projectName = null;
        String dutyTel = null;
        String dutyPerson = null;

        List<OwnerUnit> ownerUnits = ownerUnitRepo.findByToken(verifyToken);
        if (ownerUnits != null && ownerUnits.size() == 1) {
            OwnerUnit ownerUnit = ownerUnits.get(0);
            CheckReport report = checkReportRepo.findOne(ownerUnit.getAuthorizedReportNum());
            if (report != null) {
                CheckReportInfo reportInfo = report.getCheckReportInfo();
                code = 200;
                message = "success";
                reportNum = report.getReportNum();
                riskLevel = reportInfo.getRiskLevel();
                riskScore = String.format("%.2f",reportInfo.getRiskScore());
                reportDate = DateUtil.getYearMonthDateByChinese(report.getCreateDate());
                reportConclusion = reportInfo.getReportConclusion();
                rectifyComments = "暂无";
                unqualifiedItemList = report.getUnqualifiedItemDetail();
                projectName = reportInfo.getProjectName();
                dutyTel = ownerUnit.getDutyTel();
                dutyPerson = ownerUnit.getDutyPerson();
                ownerUnit.setTokenTime(new Date());
            }
        }

        result.put("code", code);
        result.put("message", message);
        result.put("reportNum", reportNum);
        result.put("riskLevel", riskLevel);
        result.put("riskScore", riskScore);
        result.put("reportDate", reportDate);
        result.put("reportConclusion", reportConclusion);
        result.put("rectifyComments", rectifyComments);
        result.put("unqualifiedItemList", unqualifiedItemList);
        result.put("projectName", projectName);
        result.put("verifyToken", verifyToken);
        result.put("dutyTel", dutyTel);
        result.put("dutyPerson", dutyPerson);

        return result;
    }

    @Override
    public JSONObject submitExtractCode(String reportNum, String dutyPerson, String dutyTel) throws Exception {
        // TODO Auto-generated method stub
        JSONObject result = new JSONObject();
        int code = 201;
        String message = "Fail. Unkonwn Owner Unit.";
        String token = null;
        OwnerUnit ownerUnit = ownerUnitRepo.findOne(dutyTel);
        if (ownerUnit != null) {
            if (ownerUnit.getDutyPerson().equals(dutyPerson) && ownerUnit.hasRecord(reportNum)) {
                Date date = new Date();
                token = EncryptionHelper.encryptStringByMD5(dutyTel + date.getTime());
                ownerUnit.setToken(token);
                ownerUnit.setLoginTime(date);
                ownerUnit.setTokenTime(date);
                ownerUnit.setAuthorizedReportNum(reportNum);
                ownerUnitRepo.save(ownerUnit);
                code = 200;
                message = "success";
            } else {
                message = "Fail.Validation Fail.";
            }
        }
        result.put("code", code);
        result.put("message", message);
        result.put("verifyToken", token);

        return result;
    }

    @Override
    public JSONObject getReportPath(String fetchCode) {

        // TODO Auto-generated method stub
        JSONObject result = new JSONObject();
        List<CheckReport> reportList = checkReportRepo.findByFetchCode(fetchCode);

        return result;
    }

    @Override
    public boolean updateRiskLevel(String reportNum) {
        // TODO Auto-generated method stub
        boolean result = false;
        CheckReport report = checkReportRepo.findOne(reportNum);
        if (report != null) {
            float riskScore = computeRiskScore(report);
            String riskLevel = computRiskLevel(riskScore);
            if (riskLevel != null) {
                report.getCheckReportInfo().setRiskLevel(riskLevel);
                report.getCheckReportInfo().setRiskScore(riskScore);
                checkReportRepo.save(report);
                result = true;
            }
        }
        return result;
    }

    @Override
    public void updateAllRiskLevel() {
        // TODO Auto-generated method stub
        List<CheckReport> reportList = checkReportRepo.findAll();
        Iterator<CheckReport> it = reportList.iterator();
        while (it.hasNext()) {
            CheckReport report = it.next();
            float riskScore = computeRiskScore(report);
            String riskLevel = computRiskLevel(riskScore);
            if (riskLevel != null) {
                report.getCheckReportInfo().setRiskScore(riskScore);
                report.getCheckReportInfo().setRiskLevel(riskLevel);
            }
        }
        checkReportRepo.save(reportList);
    }

    private String computRiskLevel(float riskScore) {
        // TODO Auto-generated method stub
        String result = null;
        if(riskScore>0 && riskScore<=100){
            if (riskScore <= boundary.getFirstLevelBoundary()) {
                result = detectionLevelPrefix+"4";
            } else if (riskScore <= boundary.getSecondLevelBoundary()) {
                result = detectionLevelPrefix+"3";
            } else if (riskScore <= boundary.getThirdLevelBoundary()) {
                result = detectionLevelPrefix+"2";
            } else {
                result = detectionLevelPrefix+"1";
            }
        }
        return result;
    }
    
    private String computeRiskLevel(CheckReport report){
        float score = computeRiskScore(report);
        return computRiskLevel(score);
    }

    private float computeRiskScore(CheckReport report) {
        // TODO Auto-generated method stub
        float score = 0;
        if (report != null && !report.getCheckReportResultStat().isEmpty()) {
            Iterator<CheckReportResultStat> it = report.getCheckReportResultStat().iterator();
            int sum = 0;
            int points = 0;
            while (it.hasNext()) {
                CheckReportResultStat item = it.next();
                if (item.getImportantGrade().equalsIgnoreCase("A")) {
                    sum = sum + item.getCheckNum() * weight.getLevelA();
                    points = points + item.getUnqualifiedNum() * weight.getLevelA();
                } else if (item.getImportantGrade().equalsIgnoreCase("B")) {
                    sum = sum + item.getCheckNum() * weight.getLevelB();
                    points = points + item.getUnqualifiedNum() * weight.getLevelB();
                } else if (item.getImportantGrade().equalsIgnoreCase("C")) {
                    sum = sum + item.getCheckNum() * weight.getLevelC();
                    points = points + item.getUnqualifiedNum() * weight.getLevelC();
                }
            }
            score = ((float) (sum - points) / (float) sum) * 100;
        }
        return score;
    }
}
