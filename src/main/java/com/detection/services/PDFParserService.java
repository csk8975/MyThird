/*
 * File Name：PDFParserService.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月21日 下午2:46:13
 */
package com.detection.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;

import com.detection.model.pdfparse.Cover;
import com.detection.model.pdfparse.ListResult;
import com.detection.model.pdfparse.PDFParserResult;
import com.detection.model.pdfparse.Result;



/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年2月21日 下午2:46:13
 */
public interface PDFParserService {
    
    /**
     * @author lcc
     * @version 1.0
     * @function 根据文件解析PDF文件结果
     */
    public PDFParserResult parse(File pdfFile) throws IOException;
    
    /**
     * @author lcc
     * @version 1.0
     * @throws ParseException 
     * @function 根据文件解析检测报告的PDF封面
     */
    public Cover processOnCover(String paragraph) throws ParseException;
    
    /**
     * @author lcc
     * @version 1.0
     * @function 根据文件解析检测报告的PDF第五部分
     */
    public List<ListResult> processOnFifthParagraph(String paragraph);
    
    /**
     * @author lcc
     * @version 1.0
     * @function 根据文件解析检测报告PDF的第四部分
     */
    public List<ListResult> processOnForthParagraph(String paragraph);
    
    /**
     * 检 测 项: 7.1.14(消火栓系统、消防供水设施、水泵故障信号反馈)
     * 重要等级: B
     * 规范要求: 水泵发生故障时,应有信号反馈回消防控制室
     * 以下是不符合规范要求的检测点：
     * 00000D2101 地下室2层、第101号检测点 (1#)
     * 00000D2102 地下室2层、第102号检测点 (2#)
     * @param src
     * @param reportNum
     * @return
     */
    public ListResult parseFourthPart(String src, String reportNum);
    
    /**
     * @author lcc
     * @version 1.0
     * function 
     * 项目编号 检测项 重要等级 检测标准(规范要求) 检测点数 不合格点数
        6 消防给水(消防水源)
        6.2 消防水池
        6.2.1 消防水池自动补水设施设置 应按设计要求设置，其补水设施应正常(应
        B 设水泵自动启停装置或浮球阀等自动补水设施)  1  0
        6.2.2 消防水池有效容积、格数 应符合规范及设计的要求
        B
         1  0
        6.2.6 消防用水与其他用水共用水池的技术 应采取确保消防用水量不作他用的技术措施措施 B
         1  0
        6.2.7 消防水池出水管 应保证消防水池的有效容积能被全部利用
        B
         1  0
        6.2.9 消防水池的溢流水管、排水设施 消防水池应设置溢流水管和排水设施，并应
        C 采用间接排水
         1  0
        7 消火栓系统
        7.1 消防供水设施
        7.1.1 消防水泵设置及选型 应按设计要求设置，选型应满足消防给水系
        A 统的流量和压力需求
         2  0
        7.1.2 消防水泵备用泵的设置 消防水泵应设置备用泵(除建筑高度小
        B 于54m的住宅和室外消防给水设计流 量≤25L/s的建筑、室内消防给水设计流  1  0量≤10L/s的建筑外
        7.1.3 水泵控制柜 消防水泵控制柜在平时应使消防水泵处于自
        B 动启泵状态，应注明所属系统编号的标 志，按钮、指示灯及仪表应正常  1 0
        广东建筑消防设施检测中心有限公司 2017年1月18日 16GJA153 第 6 页，共 44 页
        天河区开展第三方消防设施检测项目技术咨询报告
        项目编号 检测项 重要等级 检测标准(规范要求) 检测点数 不合格点数
        7.1.4 主备泵的切换 主泵不能正常投入运行时,应自动切换启动
        A 备用泵
         2  0
        7.1.5 水泵外观质量及安装质量 泵及电机的外观表面不应有碰损，轴心不应有偏心；水泵之间及其与墙或其他设备之间 的间距应满足安装、运行、维护管理要求  2  0
     */
    public List<Result> processOnThirdParagraph(String paragraph);
    
    /**
     * @author lcc
     * @version 1.0
     */
    public void prtMacher(Matcher m);
    
    /**
     * @author lcc
     * @version 1.0
     * @function 处理检测报告的第二段
     */
    public String processOnSecondParagraph(String paragraph);
    
    /**
     * @author lcc
     * @version 1.0
     * @function 处理检测报告的第一段
     */
    public List<Result> processOnFirstParagraph(String paragraph);
}

