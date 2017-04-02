/*
 * File Name：ReportViewController.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月22日 下午2:00:35
 */
package com.detection.controller;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.detection.services.AuthenticationService;
import com.detection.services.CheckReportService;

/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年2月22日 下午2:00:35
 * 
 */
@Controller
public class ReportViewController {

    @Autowired
    private CheckReportService checkReportService;

    @Autowired
    private AuthenticationService authService;

    @Value("${uploadPath}")
    private String uploadPath;

    @RequestMapping(value = { "/", "/loginPage" }, method = RequestMethod.GET)
    public String index() {
        return "login";
    }

    @RequestMapping(value = { "/main" }, method = RequestMethod.GET)
    public String main(HttpServletRequest request) {
        String result = "report/main";
        int permittedRole = 1;
        if (!authService.isLoggedin(request)) {
            result = "redirect:/";
        } else if (!authService.isPermitted(request, permittedRole)) {
            result = "redirect:nopermissions";
        }
        return result;
    }

    @RequestMapping(value = "/uploadReport", method = RequestMethod.POST)
    public String uploadReport(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        String result = "redirect:main";
        int permittedRole = 1;
        if (!authService.isLoggedin(request)) {
            result = "redirect:/";
        } else if (!authService.isPermitted(request, permittedRole)) {
            result = "redirect:nopermissions";
        } else if (!file.isEmpty()) {
            String ctxPath = request.getSession().getServletContext().getRealPath("");
            checkReportService.uploadAndSaveReport(file.getOriginalFilename(), file,
                    authService.getUserRealName(request), ctxPath);
        }
        return result;
    }

    @RequestMapping(value = { "/fetchReport/{reportNum}" }, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> fetchReportFile(@PathVariable("reportNum") String reportNum,
            HttpServletResponse response) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        String filePath = checkReportService.getReportURL(reportNum);
        if (filePath != null && !filePath.equals("")) {
            FileSystemResource file = new FileSystemResource(filePath);
            if(file.exists()){
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", String.format("inline; filename=\"%s\"",
                    URLEncoder.encode(checkReportService.getOriginalName(reportNum), "UTF-8")));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok().headers(headers).contentLength(file.contentLength())
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(new InputStreamResource(file.getInputStream()));
            }
        }
        
        return ResponseEntity.notFound().headers(headers).build();
    }

    @RequestMapping(value = "/getReportPage", method = RequestMethod.GET)
    public String getReport() {
        return "report/getReportPage";
    }

    @RequestMapping(value = "/showAbstractReportPage", method = RequestMethod.GET)
    public String reportAbstract(HttpServletRequest request) {
        String result = "report/showAbstractReportPage";
        int permittedRole = 1;
        if (!authService.isLoggedin(request)) {
            result = "redirect:/";
        } else if (!authService.isPermitted(request, permittedRole)) {
            result = "redirect:nopermissions";
        }
        return result;
    }

    @RequestMapping(value = { "/deleteReportByReportNum" }, method = RequestMethod.GET)
    public String deleteReportByReportNum(@RequestParam String reportNum, HttpServletRequest request) {
        String result = "redirect:main";
        int permittedRole = 1;
        if (!authService.isLoggedin(request)) {
            result = "redirect:/";
        } else if (!authService.isPermitted(request, permittedRole)) {
            result = "redirect:nopermissions";
        } else if (reportNum != null) {
            checkReportService.deleteReportByReportNum(reportNum);
        }
        return result;
    }

    @RequestMapping("/showDetailReportPage")
    public ModelAndView frequentBusines(@RequestParam String verifyToken) {
        ModelAndView mv = new ModelAndView("report/showDetailReportPage");
        JSONObject result = checkReportService.getDetailReportInfo(verifyToken);
        mv.addObject("result", result);
        return mv;
    }

    @RequestMapping({ "/404" })
    public String pageNotFound() {
        return "errors/404";
    }

    @RequestMapping("/505")
    public String visitError() {
        return "errors/505";
    }

    @RequestMapping("/nopermissions")
    public String NoPermisssions() {
        return "errors/nopermissions";
    }
}
