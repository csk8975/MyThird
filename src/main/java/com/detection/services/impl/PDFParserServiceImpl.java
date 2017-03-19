/*
 * File Name：PDFParserServiceImpl.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月21日 下午3:12:26
 */
package com.detection.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.detection.model.pdfparse.Cover;
import com.detection.model.pdfparse.ListResult;
import com.detection.model.pdfparse.PDFParserResult;
import com.detection.model.pdfparse.Result;
import com.detection.services.PDFParserService;

@Service
public class PDFParserServiceImpl implements PDFParserService {

    @Value("${isDebug}")
    static boolean isDebug;
    static String globalQAName = null;
    static String globalReportNum = null;

    @Override
    public PDFParserResult parse(File pdfFile) throws IOException {
        if (isDebug) {
            if (pdfFile.canRead()) {
                System.out.println("file can read!");
            } else {
                System.out.println("file can not read!");
            }
        }
        PDDocument pdfDocument = PDDocument.load(pdfFile);
        PDFParserResult returnObj = new PDFParserResult();
        int lastPage = pdfDocument.getNumberOfPages();
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        stripper.setStartPage(1);
        stripper.setEndPage(lastPage);
        String allText = stripper.getText(pdfDocument);
        int sIndex = 0;
        int eIndex = 0;
        {
            // TODO
            String end = "建筑消防设施检测报告";
            eIndex = allText.indexOf(end);
            String paragraph = allText.substring(0, eIndex);
            Cover cover = null;
            try {
                cover = this.processOnCover(paragraph);
            } catch (Exception e) {
                e.printStackTrace();
            }
            returnObj.setCover(cover);
            returnObj.setReportNum(cover.getReportNum());

        }
        {
            // TODO first part single item result
            String start = "单项评定结果";
            String end = "检测结论说明";
            sIndex = allText.indexOf(start) + start.length();
            eIndex = allText.indexOf(end);
            String paragraph = allText.substring(sIndex, eIndex);
            List<Result> rs = null;
            try {
                rs = processOnFirstParagraph(paragraph);
            } catch (Exception e) {
                e.printStackTrace();
            }
            returnObj.setFirstPart(rs);
        }
        {
            // TODO second part conclusion
            String start = "检测结论说明";
            String end = "检测情况统计表";
            sIndex = allText.indexOf(start) + start.length();
            eIndex = allText.indexOf(end);
            String paragraph = allText.substring(sIndex, eIndex);
            String rs = null;
            try {
                rs = processOnSecondParagraph(paragraph);
            } catch (Exception e) {
                e.printStackTrace();
            }
            returnObj.setSecondPart(rs);
            returnObj.getCover().setReportConclusion(rs);
        }
        {
            // TODO check item detail
            String start = "检测情况统计表";
            String end = "消防设施检测不符合规范要求项目";
            sIndex = allText.indexOf(start) + start.length();
            eIndex = allText.indexOf(end);
            String paragraph = allText.substring(sIndex, eIndex);
            List<Result> rs = null;
            try {
                rs = processOnThirdParagraph(paragraph);
            } catch (Exception e) {
                e.printStackTrace();
            }
            returnObj.setThirdPart(rs);
        }

        { // TODO unqualified
            String start = "消防设施检测不符合规范要求项目";
            String end = "消防设备登记";
            sIndex = allText.indexOf(start) + start.length();
            eIndex = allText.lastIndexOf(end);
            if (eIndex < 0 || eIndex < sIndex) {
                eIndex = allText.length();
            }
            String paragraph = allText.substring(sIndex, eIndex);
            List<ListResult> rs = null;
            try {
                rs = processOnForthParagraph(paragraph);
            } catch (Exception e) {
                e.printStackTrace();
            }
            returnObj.setForthPart(rs);
        }
        globalQAName = null;
        globalReportNum = null;
        return returnObj;
    }

    @Override
    public Cover processOnCover(String paragraph) {
        Cover cover = new Cover();
        String[] lines = paragraph.split(getLineEndByOS());
        if (isDebug) {
            System.out.println("Entered process on cover...");
            System.out.println("the paragraph is: " + paragraph);
            System.out.println("using \\n to split:" + lines.length);
            String[] liness = paragraph.split("\r\n");
            System.out.println("using \\n to split:" + liness.length);
        }
        Pattern projectName = Pattern.compile("项目名称(:|：| )\\s*(.*)\\s*$");
        Pattern projectAddress = Pattern.compile("项目地址(:|：| )\\s*(.*)\\s*$");
        Pattern agentName = Pattern.compile("委托单位\\s*(.*)\\s*$");
        Pattern qaName = Pattern.compile("检测单位(:|：| )\\s*(.*)\\s*$");
        Pattern reportNum = Pattern.compile("天消\\s*([a-z0-9A-Z]{8})\\s*$");
        Pattern qaAddress = Pattern.compile("检测单位地址(:|：| )\\s*(.*)\\s*$");
        Pattern contactTel = Pattern.compile("电\\s+话(:|：| )\\s*(.*)\\s*$");
        Pattern contactFax = Pattern.compile("传\\s+真(:|：| )\\s*(.*)\\s*$");
        Pattern contactPostcode = Pattern.compile("邮\\s+编(:|：| )\\s*(.*)\\s*$");

        int projectNameLine = 0;
        int projectAddrLine = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (isDebug)
                System.out.println("LINE=>" + line);
            Matcher m = projectName.matcher(line);
            if (m.find()) {
                cover.setProjectName(m.group(2).replace(" ", "").trim());
                projectNameLine = i;
                continue;
            }
            m = projectAddress.matcher(line);
            if (m.find()) {
                cover.setProjectAddress(m.group(2).replace(" ", "").trim());
                projectAddrLine = i;
                continue;
            }
            m = agentName.matcher(line);
            if (m.find()) {
                if (m.group(1).replace(" ", "").equals("") || m.group(1).replace(" ", "") == null)
                    cover.setAgentName(lines[i + 1].replaceAll("：|:", "").trim());
                else
                    cover.setAgentName(m.group(1).replaceAll("：|:", "").trim());
                continue;
            }
            m = qaName.matcher(line);
            if (m.find()) {
                cover.setQaName(m.group(2).replace(" ", "").trim());
                // 记录检测机构名，用于第三部分作差异化处理
                globalQAName = cover.getQaName();
                continue;
            }
            m = reportNum.matcher(line);
            if (m.find()) {
                cover.setReportNum(m.group(1).replace(" ", "").trim());
                globalReportNum = cover.getReportNum();
                continue;
            }
            m = qaAddress.matcher(line);
            if (m.find()) {
                cover.setQaAddress(m.group(2).replace(" ", "").trim());
                continue;
            }
            m = contactTel.matcher(line);
            if (m.find()) {
                cover.setContactTel(m.group(2).replace(" ", "").trim());
                continue;
            }
            m = contactFax.matcher(line);
            if (m.find()) {
                cover.setContactFax(m.group(2).replace(" ", "").trim());
                continue;
            }
            m = contactPostcode.matcher(line);
            if (m.find()) {
                cover.setContactPostcode(m.group(2).replace(" ", "").trim());
                continue;
            }
        }
        // 修改项目名称
        if (projectAddrLine - projectNameLine > 1) {
            StringBuffer sb = new StringBuffer(cover.getProjectName());
            for (int j = projectNameLine + 1; j < projectAddrLine; j++) {
                sb.append(lines[j].trim());
            }
            cover.setProjectName(sb.toString());
        }
        return cover;
    }

    @Override
    public List<ListResult> processOnFifthParagraph(String paragraph) {
        List<ListResult> rs = new ArrayList<ListResult>();
        String[] lines = paragraph.split(getLineEndByOS());
        String label = "";
        List<String> nums = new ArrayList<String>();
        List<String> strings = new ArrayList<String>();
        Pattern labpat = Pattern.compile("^\\d+  .*$");
        Pattern txtpat = Pattern.compile("^(\\d+) ([^ ].*)$");
        Pattern skppat = Pattern.compile("^\\s*第 \\d+ 页\\s*.*$");
        int position = 1;// 1=label line, 2=start text line, 3=continue text
                         // line.
        String num = "", text = "";

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (isDebug)
                System.out.println("LINE[" + position + "]=>" + line);
            {
                Matcher m = skppat.matcher(line);
                if (m.find()) {
                    if (isDebug)
                        System.out.println("    跳过");
                    continue;
                }
            }
            if (position == 1) {
                Matcher m = labpat.matcher(line);
                if (m.find()) {
                    label = m.group(0);
                    position = 2;
                    if (isDebug)
                        System.out.println("  编号=>" + label);
                }
            } else if (position == 2) {
                Matcher m = txtpat.matcher(line);
                if (m.find()) {
                    num = m.group(1);
                    text = m.group(2);
                    position = 3;
                    if (isDebug)
                        System.out.println("    S_F>>" + num + ":" + text);
                }
            } else if (position == 3) {
                Matcher m = txtpat.matcher(line);
                if (m.find()) {
                    if (!"".equals(text)) {
                        nums.add(num);
                        strings.add(text);
                        if (isDebug)
                            System.out.println("    END=>" + num + ":" + text);
                    }
                    num = m.group(1);
                    text = m.group(2);
                    if (isDebug)
                        System.out.println("    S_N>>" + num + ":" + text);
                } else {
                    Matcher m2 = labpat.matcher(line);
                    if (m2.find()) {
                        if (!"".equals(text)) {
                            nums.add(num);
                            strings.add(text);
                            if (isDebug)
                                System.out.println("    END=>" + num + ":" + text);
                        }
                        position = 1;
                        ListResult r = new ListResult(label, nums, strings);
                        rs.add(r);
                        nums = new ArrayList<String>();
                        strings = new ArrayList<String>();
                        label = m2.group(0);
                        position = 2;
                        if (isDebug)
                            System.out.println("  编号=>" + label);
                    } else {
                        text += line;
                        if (isDebug)
                            System.out.println("    MID>>" + num + ":" + text);
                    }
                }
            }
        }
        if (!"".equals(text)) {
            nums.add(num);
            strings.add(text);
            if (isDebug)
                System.out.println("    END=>" + num + ":" + text);
            ListResult r = new ListResult(label, nums, strings);
            rs.add(r);
        }
        return rs;
    }

    @Override
    public List<ListResult> processOnForthParagraph(String paragraph) {
        List<ListResult> rs = new ArrayList<ListResult>();
        int startIdx = 0;
        int endIdx = paragraph.length();
        Pattern itemPattern = Pattern.compile("\\s*检\\s*测\\s*项\\s*(：|:)");
        Pattern importantGradePattern = Pattern.compile("\\s*重\\s*要\\s*等\\s*级\\s*(：|:)\\s*(A|B|C)");
        Pattern regularPattern = Pattern.compile("\\s*规\\s*范\\s*要\\s*求\\s*(：|:)");
        Pattern unqualifiedItemPattern = Pattern.compile("\\s*以下是不符合规范要求的检测点\\s*(：|:)");
        // Pattern unqualifiedCheckPointPattern = Pattern.compile("\\d+\\s*.*");
        Matcher itemMatcher = itemPattern.matcher(paragraph);
        boolean findFlag = itemMatcher.find();
        while (findFlag) {
            int itemNameStartIdx = itemMatcher.end() - itemMatcher.start();
            startIdx = itemMatcher.start();
            if (itemMatcher.find()) {
                endIdx = itemMatcher.start();
                findFlag = true;
            } else {
                endIdx = paragraph.length();
                findFlag = false;
            }
            if (startIdx > 0 && endIdx > 0 && startIdx < endIdx) {
                String tempStr = paragraph.substring(startIdx, endIdx);
                String[] lines = tempStr.split(getLineEndByOS());
                tempStr = "";
                for (String line : lines) {
                    if (!line.contains("广东华健") && !line.contains("广东建筑消防") && !line.contains("清大安质")
                            && !line.contains("有限公司") && !line.contains("天河区开展") && !line.contains("消防设施检测项目技术咨询报告")
                            && !line.contains("工程编号：") && !line.contains("工程编号:")) {
                        tempStr = tempStr + line + getLineEndByOS();
                    }
                }

                Matcher importantGradeMatcher = importantGradePattern.matcher(tempStr);
                Matcher regularMatcher = regularPattern.matcher(tempStr);
                Matcher unqualifiedItemMatcher = unqualifiedItemPattern.matcher(tempStr);
                if (importantGradeMatcher.find() && regularMatcher.find() && unqualifiedItemMatcher.find()) {
                    String checkItem = tempStr.substring(itemNameStartIdx, importantGradeMatcher.start()).trim()
                            .replaceAll(" |" + getLineEndByOS(), "");
                    String importantGrade = importantGradeMatcher.group(2);
                    String regular = tempStr.substring(regularMatcher.end(), unqualifiedItemMatcher.start()).trim()
                            .replaceAll(getLineEndByOS(), "");
                    String[] unqualifiedCheckPoint = null;
                    if (globalQAName.contains("华建")) {
                        unqualifiedCheckPoint = tempStr.substring(unqualifiedItemMatcher.end(), tempStr.length())
                                .replaceAll(getLineEndByOS(), "").split(getLineEndByOS());
                    } else {
                        unqualifiedCheckPoint = tempStr.substring(unqualifiedItemMatcher.end(), tempStr.length())
                                .split(getLineEndByOS());
                    }
                    List<String> checkPointList = new ArrayList<String>();
                    for (String point : unqualifiedCheckPoint) {
                        if (!point.equals("") && null != point) {
                            checkPointList.add(point);
                        }
                    }
                    rs.add(new ListResult(globalReportNum, checkItem, importantGrade, regular, checkPointList));
                }
            }
        }

        return rs;
    }

    @Override
    public ListResult parseFourthPart(String src, String reportNum) {
        String[] lines = src.split(getLineEndByOS());
        String line = null;
        String testItem = null;
        String importantGrade = null;
        String requirements = null;
        List<String> nonstandardItems = new ArrayList<String>();

        int position = 0;
        for (int i = 0; i < lines.length; i++) {
            line = lines[i];
            if (null == line || "".equals(line) || line.contains(reportNum)
                    || line.contains("天河区开展第三方消防设施检测项目技术咨询报告")) {
                continue;
            }

            if (position == 0) {
                if (line.contains("检 测 项:")) {
                    testItem = line.split(":")[1].trim();
                    position = 1;
                }
            } else if (position == 1) {
                if (line.contains("重要等级:")) {
                    importantGrade = line.split(":")[1].trim();
                    position = 2;
                } else {
                    testItem += line.trim();
                }
            } else if (position == 2) {
                if (line.contains("规范要求:")) {
                    requirements = line.split(":")[1].trim();
                    position = 3;
                } else {
                    if (!line.trim().contains("广东华建") && !line.trim().contains("清大安质")
                            && !line.trim().contains("广东建筑")) {
                        importantGrade += line.trim();
                    }
                }
            } else if (position == 3) {
                if (line.contains("以下是不符合规范要求的检测点")) {
                    position = 4;
                } else {
                    if (!line.trim().contains("广东华建") && !line.trim().contains("清大安质")
                            && !line.trim().contains("广东建筑")) {
                        requirements += line.trim();
                    }
                }
            } else if (position == 4) {
                // 过滤 页脚
                if (!line.trim().contains("广东华建") && !line.trim().contains("清大安质") && !line.trim().contains("广东建筑")) {
                    nonstandardItems.add(line);
                }
            }
        }

        return new ListResult(reportNum, testItem, importantGrade, requirements, nonstandardItems);
    }

    @Override
    public List<Result> processOnThirdParagraph(String paragraph) {
        List<Result> rs = new ArrayList<Result>();
        paragraph.replaceAll(getLineEndByOS(), " ");
        Pattern elementPattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+).*\\s+(A|B|C)\\s+.*\\s+(\\d+|/)\\s+(\\d+|/)\\s+");
        Matcher elementMatcher = elementPattern.matcher(paragraph);
        while (elementMatcher.find()) {
            Result element = new Result();
            element.setLabel(elementMatcher.group(1));
            element.setLevel(elementMatcher.group(2));
            element.setValue1(elementMatcher.group(3));
            element.setValue2(elementMatcher.group(4));
            if (!element.getValue1().equals("/") && !element.getValue1().equals("0")) {
                rs.add(element);
            }
        }
        return rs;
    }

    @Override
    public void prtMacher(Matcher m) {
        int max = m.groupCount();
        System.out.println("  LINE[" + max + "]=" + m.group(0));
        for (int i = 1; i < max; i++) {
            System.out.println("    ITEM=" + m.group(i));
        }
    }

    @Override
    public String processOnSecondParagraph(String paragraph) {
        paragraph = paragraph.trim().replace(" ", "");
        // int lastIndex = paragraph.lastIndexOf("\n");
        int lastIndex = paragraph.lastIndexOf("。");
        if (lastIndex < 1)
            return "";
        return paragraph.substring(0, lastIndex);
    }

    @Override
    public List<Result> processOnFirstParagraph(String paragraph) {
        List<Result> rs = new ArrayList<Result>();
        Pattern itemPattern = Pattern.compile(getLineEndByOS() + "(\\d+)\\s*");
        Matcher itemMatcher = itemPattern.matcher(paragraph);
        int startIndex = 0;
        int endIndex = 0;
        String itemCode = "";
        String tempStr = "";
        boolean findFlag = itemMatcher.find();
        while (findFlag) {
            itemCode = itemMatcher.group(1);
            if (itemMatcher.find()) {
                findFlag = true;
                endIndex = itemMatcher.start();
            } else {
                findFlag = false;
                endIndex = paragraph.length();
            }
            tempStr = paragraph.substring(startIndex, endIndex);
            startIndex = startIndex + getSingleItemResultOfFirstPart(tempStr, itemCode, rs);
        }

        return rs;
    }

    private int getSingleItemResultOfFirstPart(String strToParse, String itemCode, List<Result> rs) {
        // TODO Auto-generated method stub
        Pattern elementPattern = Pattern.compile("(A|B|C)\\s+(\\d+|/)\\s+(\\d+|/)");
        Matcher elementMatcher = elementPattern.matcher(strToParse);
        char itemIndex = 'A' - 1;
        while (elementMatcher.find()) {
            char currentItemIndex = elementMatcher.group(1).charAt(0);
            if (currentItemIndex > itemIndex) {
                itemIndex = currentItemIndex;
                Result element = new Result();
                element.setLabel(itemCode);
                element.setLevel(elementMatcher.group(1));
                element.setValue1(elementMatcher.group(2));
                element.setValue2(elementMatcher.group(3));
                if (!element.getValue1().equals("/") && !element.getValue1().equals("0")) {
                    rs.add(element);
                }
            } else {
                return elementMatcher.start();
            }
        }
        return strToParse.length();
    }

    private String getLineEndByOS() {
        String osName = System.getProperty("os.name");
        String result;
        if (osName.contains("win") || osName.contains("Win")) {
            result = "\r\n";
        } else if (osName.contains("Linux") || osName.contains("linux")) {
            result = "\n";
        } else {
            result = "\r";
        }
        return result;
    }
}
