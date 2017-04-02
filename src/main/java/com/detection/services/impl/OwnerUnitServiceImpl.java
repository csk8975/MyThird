package com.detection.services.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.detection.model.owner.CrOwnerUnit;
import com.detection.model.owner.OwnerUnitRepository;
import com.detection.model.report.entities.CrCheckRecord;
import com.detection.model.report.entities.CrCheckReport;
import com.detection.services.OwnerUnitService;
import com.detection.util.FormCheck;

/**
 * @fileName OwnerUnitServiceImpl.java
 * @author csk
 * @createTime 2017年3月3日 下午5:09:22
 * @version 1.0
 * @function
 */
@Service
public class OwnerUnitServiceImpl implements OwnerUnitService {
    
    @Autowired
    private OwnerUnitRepository ownerUnitRepo;
    @Value("${reportNumForDemo}")
    private String reportNumForDemo;

    @Override
    public JSONObject addOwnerUnit(String dutyTel, String dutyPerson, String ownerName, String email) {
        JSONObject result = new JSONObject();
        int code = 201;
        String message = "Fail.";
        
        if(!FormCheck.isEmailValid(email)||!FormCheck.isPhoneValid(dutyTel))
        {
            code = 201;
            message = "Invalid phone or email.";
        }
        else{
            if(ownerUnitRepo.findOne(dutyTel) != null){
                message = "This phone has been registered.";
            }
            else {
                Date registTime = new Date();
                CrOwnerUnit owner = new CrOwnerUnit();
                
                owner.setDutyTel(dutyTel);
                owner.setDutyPerson(dutyPerson);
                owner.setEmail(email);
                owner.setOwnerName(ownerName);
                owner.setRegistTime(registTime);
                ownerUnitRepo.save(owner);
                message = "success";
            }
        }
        
        result.put("code", code);
        result.put("message", message);

        // TODO Auto-generated method stub
        return result;
    }

    @Override
    public JSONObject deleteOwnerUnit(String dutyTel) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JSONObject getAllOwnerUnit() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JSONObject getOwnerUnitByName(String ownerName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JSONObject getOwnerUnitByTel(String dutyTel) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JSONObject testFetchReport() {
        // TODO Auto-generated method stub
        JSONObject result = new JSONObject();
        int updatedNum = 0;
        List<CrOwnerUnit> ownerList = ownerUnitRepo.findAll();
        Iterator<CrOwnerUnit> it = ownerList.iterator();
        while(it.hasNext()){
            CrOwnerUnit owner = it.next();
            CrCheckRecord item = new CrCheckRecord();
            Iterator<CrCheckRecord> recordIt = owner.getCheckRecords().iterator();
            boolean hasRecord = false;
            while(recordIt.hasNext()){
                item = recordIt.next();
                if(item.getReportNum().equals(reportNumForDemo)){
                    hasRecord = true;
                    break;
                }
            }
            if(!hasRecord){
                updatedNum++;
                item.setRecordDate(new Date());
                item.setReportNum(reportNumForDemo);
                owner.addOneCheckRecord(item);
            }
        }
        ownerUnitRepo.save(ownerList);
        result.put("result", "finish");
        result.put("updated", updatedNum);
        return result;
    }

}
