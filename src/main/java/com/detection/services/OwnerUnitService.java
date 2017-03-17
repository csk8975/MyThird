package com.detection.services;

import com.alibaba.fastjson.JSONObject;


/**
 * @fileName OwnerUnitService.java
 * @author csk
 * @createTime 2017年3月3日 下午5:08:54
 * @version 1.0
 * @function
 */

public interface OwnerUnitService {

    public JSONObject addOwnerUnit(String dutyTel, String dutyPerson, String ownerName, String email);
    
    public JSONObject deleteOwnerUnit(String dutyTel);
    
    public JSONObject getAllOwnerUnit();
    
    public JSONObject getOwnerUnitByName(String ownerName);
    
    public JSONObject getOwnerUnitByTel(String dutyTel);
    
    public JSONObject testFetchReport();
    
}
