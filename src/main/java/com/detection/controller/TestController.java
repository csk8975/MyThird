/*
 * File Name：TestController.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月20日 下午5:30:45
 */
package com.detection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年2月20日 下午5:30:45
 */
@Controller
public class TestController {

    @RequestMapping("/helloWorld")
    public String frequentBusines() {
        System.out.println("hello Wolrd");
        return "hello/HelloWorld";
    }
}

