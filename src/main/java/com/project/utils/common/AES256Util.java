package com.project.utils.common;

import com.project.exception.common.AesEncryptException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * 양방향 암호화 알고리즘인 AES256 암호화를 지원하는 클래스
 * Created by KMS on 03/03/2020.
 */
public class AES256Util {
    /**
     * 16자리의 키값을 입력하여 객체를 생성한다.
     *
     * @param key
     *            암/복호화를 위한 키값
     * @throws UnsupportedEncodingException
     *             키값의 길이가 16이하일 경우 발생
     */
    private static final String iv = "YnVzaW5lc3Nub3Rl";

    private static SecretKeySpec getKeySpec() {
        String key = "cWtxaGFqZGNqZGRs";

        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes(StandardCharsets.UTF_8);
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * AES256 으로 암호화 한다.
     *
     * @param str 암호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String encrypt(String str) {
        String enStr;
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, getKeySpec(), new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = c.doFinal(str.getBytes(StandardCharsets.UTF_8));
            enStr = new String(Base64.encodeBase64(encrypted));
        } catch (Exception e) {
            throw new AesEncryptException(e.getMessage(), e);
        }
        return enStr;
    }

    /**
     * AES256으로 암호화된 txt 를 복호화한다.
     *
     * @param str 복호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String decrypt(String str) {
        String enStr;
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, getKeySpec(), new IvParameterSpec(iv.getBytes()));
            byte[] byteStr = Base64.decodeBase64(str.getBytes());
            enStr = new String(c.doFinal(byteStr), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new AesEncryptException(e.getMessage(), e);
        }
        return enStr;
    }
}
