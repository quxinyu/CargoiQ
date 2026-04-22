package com.efreight.base.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * @author fu yuan hui
 * @date 2024-07-23
 */
@Slf4j
public class RSAUtils {
    /**
     * 生成RSA密钥对
     * 
     * @return KeyPair 包含公钥和私钥
     * @throws Exception 异常
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 加密数据
     * 
     * @param data      需要加密的数据
     * @param publicKey 公钥
     * @return 加密后的数据
     * @throws Exception 异常
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 解密数据
     * 
     * @param data       需要解密的数据
     * @param privateKey 私钥
     * @return 解密后的数据
     * @throws Exception 异常
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(decryptedBytes);
    }

    /**
     * 获取Base64编码的公钥字符串
     * 
     * @param publicKey 公钥
     * @return Base64编码的公钥字符串
     */
    public static String getPublicKeyString(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 获取Base64编码的私钥字符串
     * 
     * @param privateKey 私钥
     * @return Base64编码的私钥字符串
     */
    public static String getPrivateKeyString(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 从Base64编码的字符串获取公钥
     * 
     * @param publicKeyString Base64编码的公钥字符串
     * @return 公钥
     * @throws Exception 异常
     */
    public static PublicKey getPublicKeyFromString(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 从Base64编码的字符串获取私钥
     * 
     * @param privateKeyString Base64编码的私钥字符串
     * @return 私钥
     * @throws Exception 异常
     */
    public static PrivateKey getPrivateKeyFromString(String privateKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static void main(String[] args) {
        try {
            KeyPair keyPair = generateKeyPair();
            String publicKeyString = getPublicKeyString(keyPair.getPublic());
            String privateKeyString = getPrivateKeyString(keyPair.getPrivate());

            log.info("密钥对中的公钥值: {}\n\n", publicKeyString);
            log.info("密钥对中的私钥值: {}\n", privateKeyString);

            String data = "这是一个测试字符串";
            String encryptedData = encrypt(data, keyPair.getPublic());
            String decryptedData = decrypt(encryptedData, keyPair.getPrivate());
            log.info("\n-----------------------------------------------------------\n");
            log.info("\n①原始数据: {}\n②加密后的数据: {}\n③解密后的数据: {}\n", data, encryptedData, decryptedData);
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>RSA工具发生错误",e);
        }
    }
}
