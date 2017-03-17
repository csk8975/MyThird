package com.detection.controller;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.detection.services.OwnerUnitService;

@Controller
public class DepartmentController {
    
    @Autowired
    private OwnerUnitService ownerUnitService;

    @GetMapping(value = "/addOwnerUnit")
    public String addOwnerUnit(@RequestParam String ownerName, @RequestParam String dutyTel,
            @RequestParam String dutyPerson, @RequestParam String email) throws AddressException, MessagingException {
        
        JSONObject result = ownerUnitService.addOwnerUnit(dutyTel, dutyPerson, ownerName, email);
        if(result.getString("message").equals("success")){
            return "redirect:finish";
        }
        return "redirect:fail";
    }
    
    @GetMapping(value = "/regist")
    public String regist(){
        return "registration/regist";
    }
    
    @GetMapping(value = "/finish")
    public String finish(){
        return "registration/finish";
    }
    
    @GetMapping(value = "/fail")
    public String fail(){
        return "registration/fail";
    }

}
