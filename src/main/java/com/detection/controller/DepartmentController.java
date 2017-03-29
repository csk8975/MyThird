package com.detection.controller;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.detection.services.OwnerUnitService;

@Controller
public class DepartmentController {
    
    @Autowired
    private OwnerUnitService ownerUnitService;

    @RequestMapping(value = "/addOwnerUnit", method = RequestMethod.GET)
    public String addOwnerUnit(@RequestParam String ownerName, @RequestParam String dutyTel,
            @RequestParam String dutyPerson, @RequestParam String email) throws AddressException, MessagingException {
        
        JSONObject result = ownerUnitService.addOwnerUnit(dutyTel, dutyPerson, ownerName, email);
        if(result.getString("message").equals("success")){
            return "redirect:finish";
        }
        return "redirect:fail";
    }
    
    @RequestMapping(value = "/regist", method = RequestMethod.GET)
    public String regist(){
        return "registration/regist";
    }
    
    @RequestMapping(value = "/finish", method = RequestMethod.GET)
    public String finish(){
        return "registration/finish";
    }
    
    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public String fail(){
        return "registration/fail";
    }

}
