package com.efreight.base.common.core.utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author leo
 * @date 2024/5/10 18:28
 **/
@Slf4j
public class FileUtils {
    /**
     * 文件一致性校验支持的两种算法 MD5 SHA256
     * */
    public static final String MD5 = "MD5";
    public static final String SHA256 = "SHA256";

    /**
     * 获取user.home目录
     * @return
     */
    public static String getUserHomePath() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + "temp";
    }
    /**
     * 根据文件名创建临时文件
     * 优先创建在user.home/temp目录下的临时文件
     * 如果user.home不能创建，则创建在当前项目路径下
     * @param fileName
     * @return
     */
    public static File createTempFile(String fileName) {
        try {
            return createUserHomeTempFile(fileName);
        }catch (Exception e){
            return new File(fileName);
        }
    }

    /**
     * 创建在user.home/temp目录下的临时文件
     * @param fileName
     * @return
     */
    public static File createUserHomeTempFile(String fileName) {
        String userTempFileDir = getUserHomePath();

        if(!FileUtil.file(userTempFileDir).exists()){
            FileUtil.mkdir(userTempFileDir);
        }

        String tempFilePath = userTempFileDir + File.separator + fileName;
        return new File(tempFilePath);
    }

    /*-----------------------------------文件一致性校验--------start-------------------------------------------*/
    /**
     * 校验文件是否一致
     * @param file
     * @param fileName
     * @param hashValue
     * @return
     */
    public static boolean fileCheck(File file, String fileName, String hashValue) {
        return fileCheck(file, fileName, true, hashValue);
    }

    /**
     * 校验文件是否一致
     * @param file
     * @param fileName
     * @param isCheckHash 是否校验哈希值
     * @param hashValue
     * @return
     */
    public static boolean fileCheck(File file, String fileName, boolean isCheckHash, String hashValue) {
        if(file == null || fileName == null){
            return false;
        }
        if(isCheckHash){
            return fileName.equals(file.getName()) && fileHashCheck(file, hashValue);
        }else{
            return fileName.equals(file.getName());
        }
    }

    /**
     * 校验文件哈希值是否一致 默认使用SHA256
     * @param file
     * @param hashValue
     * @return
     */
    public static boolean fileHashCheck(File file, String hashValue) {
        return fileHashCheck(file, hashValue, SHA256);
    }

    /**
     * 校验文件哈希值是否一致
     * @param file
     * @param hashValue
     * @param algorithm
     * @return
     */
    public static boolean fileHashCheck(File file, String hashValue, String algorithm) {
        if(file == null ){
            log.error("文件路径不能为空");
            return false;
        }
        if(hashValue == null){
            log.error("文件哈希值不能为空");
            return false;
        }
        return hashValue.equals(getFileHash(file, algorithm));
    }

    /**
     * 获取hash值
     * @param file
     * @return
     */
    public static String getFileHash(File file, String algorithm) {
        if(MD5.equals(algorithm)){
            return fileHashMD5(file);
        }else if(SHA256.equals(algorithm)){
            return fileHashSHA256(file);
        }else {
            log.error("不支持的哈希算法：{}", algorithm);
            return null;
        }
    }

    /**
     * 计算文件SHA-256哈希值校验和
     * @param file
     * @return
     */
    public static String fileHashSHA256(File file) {
        try {
            // 获取SHA-256 MessageDigest实例
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 打开文件并读取内容
            FileInputStream fis = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];

            int bytesRead;
            while ((bytesRead = fis.read(dataBytes)) != -1) {
                // 更新摘要
                md.update(dataBytes, 0, bytesRead);
            }

            // 获得哈希值
            byte[] hashBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", 0xFF & b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error: " + e.getMessage());
        } catch (IOException e) {
            log.error("Error reading file: " + e.getMessage());
        }
        return null;

    }

    /**
     * 计算文件MD5哈希值校验和
     * @param file
     * @return
     */
    public static String fileHashMD5(File file) {
        try {
            // 获取MD5 MessageDigest实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 打开文件并读取内容
            FileInputStream fis = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];

            int bytesRead;
            while ((bytesRead = fis.read(dataBytes)) != -1) {
                // 更新摘要
                md.update(dataBytes, 0, bytesRead);
            }
            BigInteger bigInteger = new BigInteger(1, md.digest());
            return bigInteger.toString(16);

        } catch (NoSuchAlgorithmException e) {
            log.error("Error: " + e.getMessage());
        } catch (IOException e) {
            log.error("Error reading file: " + e.getMessage());
        }
        return null;
    }

    /*-----------------------------------文件一致性校验--------start-------------------------------------------*/

    public static void deleteTemp(List<String> txtFtpPathList){
        if(txtFtpPathList == null){
            return ;
        }
        for (String txtFtpPath : txtFtpPathList){
            FileUtil.del(txtFtpPath);
        }
    }
}
