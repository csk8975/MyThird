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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.juli.FileHandler;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.FileEditor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.detection.config.LevelWeightProperties;
import com.detection.config.RiskLevelBoundary;
import com.detection.model.owner.CrOwnerUnit;
import com.detection.model.owner.OwnerUnitRepository;
import com.detection.model.pdfparse.Cover;
import com.detection.model.pdfparse.ListResult;
import com.detection.model.pdfparse.PDFParserResult;
import com.detection.model.pdfparse.Result;
import com.detection.model.report.entities.CrCheckItemDetail;
import com.detection.model.report.entities.CrCheckReport;
import com.detection.model.report.entities.CrCheckReportInfo;
import com.detection.model.report.entities.CrCheckReportResultStat;
import com.detection.model.report.entities.CrCheckReportUnqualifiedItemDetail;
import com.detection.model.report.repositories.CheckReportInfoRepository;
import com.detection.model.report.repositories.CheckReportRepository;
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
    @Autowired
    private CheckReportInfoRepository checkReportInfoRepo;

    @Value("${uploadPath}")
    private String uploadPath;

    @Value("${detectionLevelPrefix}")
    private String detectionLevelPrefix;
    
    @Value("${isDebug}")
    private boolean isDebug;

    @Override
    public boolean uploadAndSaveReport(String fileName, MultipartFile file, String operatorName, String ctxPath) throws Exception {
        boolean result = false;
        String encryptedFileName = EncryptionHelper.encryptStringByMD5(fileName);
        String upFilePath = ctxPath+ uploadPath + encryptedFileName;
        //String downFilePath = downloadPath + fileName;

        File outPath = new File(ctxPath+uploadPath);
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
        //upFileOS.close();
        out.close();
        parseAndSaveReportToDB(upFilePath, fileName,encryptedFileName,operatorName);
        result = true;

        return result;
    }

    @Override
    public boolean parseAndSaveReportToDB(String upFilePath, String fileName,String encryptedFileName, String operatorName) throws IOException {

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
        // 保存report对象
        CrCheckReport checkReport = new CrCheckReport();
        CrCheckReportInfo checkReportInfo = new CrCheckReportInfo();
        List<CrCheckReportResultStat> checkReportStatList = new ArrayList<CrCheckReportResultStat>();
        List<CrCheckItemDetail> checkItemDetailList = new ArrayList<CrCheckItemDetail>();
        List<CrCheckReportUnqualifiedItemDetail> checkReportUnqualifiedItemList = new ArrayList<CrCheckReportUnqualifiedItemDetail>();
        // report basic info
        checkReport.setReportNum(reportCover.getReportNum());
        checkReport.setFilePath(upFilePath);
        checkReport.setFileName(encryptedFileName);
        checkReport.setOriginalName(fileName);
        checkReport.setCreateDate(reportCover.getReportDate());
        checkReport.setModifyDate(new Date());
        checkReport.setFetchCode(reportCover.getReportNum().substring(4)+upFilePath.substring(upFilePath.length()-4));

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

        // process on first part
        Iterator<Result> it1 = firstPart.iterator();
        Pattern digitsPattern = Pattern.compile("^\\d+$");
        //System.out.println("第一部分处理开始=====>>>>>>>>>>>");
        while (it1.hasNext()) {
            CrCheckReportResultStat element = new CrCheckReportResultStat();
            Result nextItem = it1.next();
            element.setImportantGrade(nextItem.getLevel());
            if(digitsPattern.matcher(nextItem.getValue1()).matches()){
                element.setCheckNum(Integer.parseInt(nextItem.getValue1()));
            }
            if(digitsPattern.matcher(nextItem.getValue2()).matches()){
                element.setUnqualifiedNum(Integer.parseInt(nextItem.getValue2()));
            }
            element.setItemCode(nextItem.getLabel());
            element.setItemName(nextItem.getName());
            if(isDebug){
                System.out.println(element.getItemName() +"   " +element.getItemCode() +"   " +element.getCheckNum() +"   " +element.getUnqualifiedNum());
            }
            checkReportStatList.add(element);
        }

        // process on third part
        Iterator<Result> it2 = thirdPart.iterator();
        //System.out.println("\n\n第三部分处理开始=====>>>>>>>>>>>");
        while (it2.hasNext()) {
            CrCheckItemDetail element = new CrCheckItemDetail();
            Result nextItem = it2.next();
            element.setImportantGrade(nextItem.getLevel());
            element.setItemCode(nextItem.getLabel());
            //deprecated
            //element.setItemName(nextItem.getName());
            if(digitsPattern.matcher(nextItem.getValue1()).matches()){
                element.setCheckNum(Integer.parseInt(nextItem.getValue1()));
            }
            if(digitsPattern.matcher(nextItem.getValue2()).matches()){
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
            CrCheckReportUnqualifiedItemDetail element = new CrCheckReportUnqualifiedItemDetail();
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
        checkReport.setCreatorName(operatorName);
        
        //风险评分计算
/*        float riskScore = computeRiskScore(checkReport);
        checkReport.getCheckReportInfo().setRiskScore(riskScore);
        checkReport.getCheckReportInfo().setRiskLevel(computRiskLevel(riskScore));
        
        System.out.println("完成报告解析：\n风险评分: " +riskScore );
        System.out.println("报告号码："+reportCover.getReportNum());*/
        
        deleteReportRecordByReportNum(reportCover.getReportNum());
        checkReportRepo.save(checkReport);
        System.out.println("Finish Parsing report:" + reportCover.getReportNum());
        result = true;

        return result;
    }
  //删除数据记录及删除文件。用于彻底删除报告
    @Override
    public JSONObject deleteReportByReportNum(String reportNum) {
        JSONObject result = new JSONObject();
        int code = 201;
        String message = "记录不存在。";
        if (checkReportRepo.findOne(reportNum) != null) {
            System.out.println(">>>>>>Warning: Both report record and file will be deleted:" +reportNum);
            String filePath = checkReportRepo.findFilePathByReportNum(reportNum);
            File file = new File(filePath);
            if(file.exists() && file.isFile()){
                file.delete();
            }
            checkReportRepo.delete(reportNum);
            code = 200;
            message = "删除成功";
        }
        result.put("code", code);
        result.put("message", message);
        return result;
    }
    
    //仅删除数据记录，不删除文件。用于重新上传覆盖
    @Override
    public void deleteReportRecordByReportNum(String reportNum) {
        if (checkReportRepo.findOne(reportNum) != null) {
            System.out.println(">>>>>>Conflicts exist..Old record will be deleted:" +reportNum);
            checkReportRepo.delete(reportNum);
        }
    }

    @Override
    public JSONObject getAllReports() {
        JSONObject result = new JSONObject();
        int code = 200;
        String message = "success";

        List<CrCheckReport> reportlist = checkReportRepo.findAll();
        List<JSONObject> dataList = new ArrayList<JSONObject>();
        Iterator<CrCheckReport> it = reportlist.iterator();
        while (it.hasNext()) {
            CrCheckReport checkReport = it.next();
            CrCheckReportInfo checkReportInfo = checkReport.getCheckReportInfo();
            JSONObject item = new JSONObject();
            item.put("reportNum", checkReportInfo.getReportNum());
            item.put("projectName", checkReportInfo.getProjectName());
            item.put("projectAddress", checkReportInfo.getProjectAddress());
            item.put("riskLevel", checkReportInfo.getRiskLevel());
            item.put("riskLevel", checkReportInfo.getRiskLevel());
            item.put("qaName", checkReportInfo.getQaName());
            item.put("contactTel", checkReportInfo.getContactTel());
            dataList.add(item);
        }
        result.put("code", code);
        result.put("message", message);
        result.put("data", dataList);

        return result;
    }

    @Override
    public JSONObject getAbstractReportInfo(String reportNum) {
        // TODO Auto-generated method stub
        JSONObject result = new JSONObject();
        int code = 201;
        String message = "Fail.Report Not Found.";
        String reportDate = null;
        String fetchCode = null;
        String projectName = null;
        String riskLevel = null;

        CrCheckReport report = checkReportRepo.findOne(reportNum);
        if (report != null) {
            code = 200;
            message = "success";
            reportDate = DateUtil.getYearMonthDateByHyphen(report.getCreateDate());
            fetchCode = report.getFetchCode();
            projectName = report.getCheckReportInfo().getProjectName();
            riskLevel = report.getCheckReportInfo().getRiskLevel();
        }
        result.put("code", code);
        result.put("message", message);
        result.put("reportNum", reportNum);
        result.put("fetchCode", fetchCode);
        result.put("reportDate", reportDate);
        result.put("projectName", projectName);
        if(riskLevel != null){
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
        }
        else{
            result.put("riskLevel", -1);
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
        List<CrCheckReportUnqualifiedItemDetail> unqualifiedItemList = new ArrayList<CrCheckReportUnqualifiedItemDetail>();
        String projectName = null;
        String dutyTel = null;
        String dutyPerson = null;

        List<CrOwnerUnit> ownerUnits = ownerUnitRepo.findByToken(verifyToken);
        if (ownerUnits != null && ownerUnits.size() == 1) {
            CrOwnerUnit ownerUnit = ownerUnits.get(0);
            CrCheckReport report = checkReportRepo.findOne(ownerUnit.getAuthorizedReportNum());
            if (report != null) {
                CrCheckReportInfo reportInfo = report.getCheckReportInfo();
                code = 200;
                message = "success";
                reportNum = report.getReportNum();
                riskLevel = reportInfo.getRiskLevel();
                //riskScore = String.format("%.2f",reportInfo.getRiskScore());
                reportDate = DateUtil.getYearMonthDateByChinese(report.getCreateDate());
                reportConclusion = reportInfo.getReportConclusion();
                rectifyComments = "暂无";
                unqualifiedItemList = report.getUnqualifiedItemDetail();
                projectName = reportInfo.getProjectName();
                dutyTel = ownerUnit.getDutyTel();
                dutyPerson = ownerUnit.getDutyPerson();
                ownerUnit.setTokenTime(new Date());
                ownerUnit.setAuthorizedReportNum(null);
                ownerUnit.setToken(null);
                ownerUnitRepo.save(ownerUnit);
                
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
    public JSONObject submitExtractCode(String extracteCode, String ownerName, String dutyTel) throws Exception {
        // TODO Auto-generated method stub
        JSONObject result = new JSONObject();
        int code = 201;
        String message = "Fail. Unkonwn Owner Unit.";
        String token = null;
        String dutyPerson = null;
        //CrOwnerUnit ownerUnit = ownerUnitRepo.findOne(dutyTel);
        CrOwnerUnit ownerUnit = ownerUnitRepo.findByDutyTelAndOwnerNameLike(dutyTel,ownerName);
        if(ownerUnit != null){
            String reportNum = checkReportRepo.findReportNumByFetchCode(extracteCode.toUpperCase());
            dutyPerson = ownerUnit.getDutyPerson();
            if(reportNum != null && !reportNum.equals("")){
                String projectName = checkReportInfoRepo.findbyOwnerNameLikeProjectName(ownerName);
                if(projectName!=null && !projectName.equals("")){
                    Date date = new Date();
                    token = EncryptionHelper.encryptStringByMD5(dutyTel + extracteCode + ownerName + date.getTime());
                    ownerUnit.setToken(token);
                    ownerUnit.setLoginTime(date);
                    ownerUnit.setTokenTime(date);
                    ownerUnit.setAuthorizedReportNum(reportNum);
                    ownerUnitRepo.save(ownerUnit);
                    code = 200;
                    message = "success";
                }
            }
            else {
                code = 201;
                message = "Fail.Validation Fail.";
            }
            result.put("code", code);
            result.put("message", message);
            result.put("verifyToken", token);
            result.put("dutyPerson", dutyPerson);
        }
        return result;
    }

    @Override
    public boolean updateRiskLevel(String reportNum) {
        // TODO Auto-generated method stub
        boolean result = false;
        CrCheckReport report = checkReportRepo.findOne(reportNum);
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
        List<CrCheckReport> reportList = checkReportRepo.findAll();
        Iterator<CrCheckReport> it = reportList.iterator();
        while (it.hasNext()) {
            CrCheckReport report = it.next();
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
    
    private String computeRiskLevel(CrCheckReport report){
        float score = computeRiskScore(report);
        return computRiskLevel(score);
    }

    private float computeRiskScore(CrCheckReport report) {
        // TODO Auto-generated method stub
        float score = 0;
        if (report != null && !report.getCheckReportResultStat().isEmpty()) {
            Iterator<CrCheckReportResultStat> it = report.getCheckReportResultStat().iterator();
            int sum = 0;
            int points = 0;
            while (it.hasNext()) {
                CrCheckReportResultStat item = it.next();
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

    @Override
    public String getReportURL(String reportNum) {
        // TODO Auto-generated method stub
        return checkReportRepo.findFilePathByReportNum(reportNum);
    }

    @Override
    public String getOriginalName(String reportNum) {
        // TODO Auto-generated method stub
        return checkReportRepo.findOriginalNameByReportNum(reportNum);
    }

    @Override
    public void uploadRiskLevel(MultipartFile file) throws IOException {
        // TODO Auto-generated method stub
        InputStream in = file.getInputStream();
        HSSFWorkbook wb = new HSSFWorkbook(in);
        HSSFSheet sheet = wb.getSheetAt(0);
        String queryQAName = "";
        String reportNumPattern = "";
        
        if(sheet !=null){
            int rowIndex=0;
            HSSFRow row = sheet.getRow(rowIndex);
            while(queryQAName.equals("")){
                row = sheet.getRow(rowIndex);
                if(row.getCell(0).getStringCellValue().contains("检测中心")){
                    queryQAName = "广东建筑消防设施检测中心有限公司";
                    reportNumPattern = "(\\d\\d\\w{2,3})(\\d{3})";
                    rowIndex++;
                    break;
                }
                rowIndex++;
            }
            int failCount = 0;
            int successCount = 0;
            int ambiguousCount = 0;
            int overallCount = 0;
            for(; rowIndex<=sheet.getLastRowNum();rowIndex++){
                row = sheet.getRow(rowIndex);
                String currentName = row.getCell(0).getStringCellValue();
                String currentGrade = row.getCell(1).getStringCellValue();
                if(currentName !=null){
                    if(currentName.contains("清大")){
                        queryQAName = "清大安质消防安全管理质量";
                        reportNumPattern = "(A|B)(\\d{3})";
                    }
                    else if(currentName.contains("华建")){
                        queryQAName = "广东华建电气消防安全检测有限公司";
                        reportNumPattern = "(A|B)(\\d{3})";
                    }
                    else if(currentGrade != null){
                        Pattern pattern = Pattern.compile(reportNumPattern);
                        Matcher matcher = pattern.matcher(currentName);
                        if(matcher.find()){
                            String reportNum = matcher.group(2);
                            List<CrCheckReportInfo> reportInfos = checkReportInfoRepo.findbyReportNumLikeAndQaNameLike(reportNum, queryQAName);
                            Iterator<CrCheckReportInfo> it = reportInfos.iterator();
                            if(reportInfos.size()>1){
                                System.out.println(">>>>>>>>>>Warning: More than one report info find by this report name >>>" + currentName);
                                while(it.hasNext()){
                                    CrCheckReportInfo reportInfo = it.next();
                                    System.out.println("    Report Found: " + reportInfo.getReportNum());
                                }
                                System.out.println("    Nothing will be changed by these reports!<<<<");
                                ambiguousCount++;
                            }
                            else if(reportInfos.size()==1){
                                CrCheckReportInfo reportInfo = reportInfos.get(0);
                                System.out.println("Inserting risk leve into report: "+reportInfo.getReportNum());
                                System.out.println("    Found by report name:" + currentName);
                                reportInfo.setRiskLevel("危险" + currentGrade);
                                checkReportInfoRepo.save(reportInfo);
                                successCount++;
                            }
                            else{
                                System.out.println(">>>>>>>>>>ERROR: Report not found : " + currentName);
                                failCount++;
                            }
                        }
                        else{
                            System.out.println("unkown");
                        }
                        overallCount++;
                    }
                }
            }
            System.out.println("Overall Count : " +overallCount);
            System.out.println("Success Count : " +successCount);
            System.out.println("Fail Count : " +failCount);
            System.out.println("Ambiguous Count : " +ambiguousCount);
        }
        wb.close();
    }

    
}
