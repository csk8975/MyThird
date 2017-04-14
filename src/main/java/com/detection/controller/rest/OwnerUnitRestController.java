package com.detection.controller.rest;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.detection.services.OwnerUnitService;

/**
 * @fileName OwnerUnitRestController.java
 * @author csk
 * @createTime 2017年3月3日 下午5:59:34
 * @version 1.0
 * @function
 */

@RestController
public class OwnerUnitRestController {
    @Autowired
    private OwnerUnitService ownerUnitService;
    

}
