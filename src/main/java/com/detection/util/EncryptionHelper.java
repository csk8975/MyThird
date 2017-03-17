package com.detection.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @fileName EncrptionHelper.java
 * @author csk
 * @createTime 2017年3月1日 下午4:13:21
 * @version 1.0
 * @function
 */

public class EncryptionHelper {
    
    public static String encryptStringByMD5(String strToEncrypt) throws Exception {

        MessageDigest md = null;
        String out = null;

        try {
            md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(strToEncrypt.getBytes());
            out = byte2hex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw e;
        }
        return out;
    }
    
    public static String encryptBytesByMD5(String strToEncrypt) throws Exception {

        MessageDigest md = null;
        String out = null;

        try {
            md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(strToEncrypt.getBytes());
            out = byte2hex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw e;
        }
        return out;
    }
    
    public static String encryptFileNameByMD5(String fileName) throws Exception{

        return encryptStringByMD5(fileName)+"."+getExtension(fileName);
    }
    
    public static String getUserToken(String userName, Date updateTime) throws Exception{
        String concateStr = userName + updateTime.toString();
        return encryptStringByMD5(concateStr);
    }
    
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    
    private static String getExtension(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return filename.substring(dot + 1);   
            }   
        }   
        return filename;   
    }
}
