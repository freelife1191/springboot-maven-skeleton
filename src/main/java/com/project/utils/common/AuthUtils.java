package com.project.utils.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import java.io.IOException;
import java.util.Map;

/**
 * Created by KMS on 24/09/2019.
 *
 * 인증 유틸
 */
public class AuthUtils {

    /**
     * JWT 복호화
     * @param data
     * @param secretKey
     * @return
     * @throws IOException
     */
    public static Map<String, String> jwtDecoder(String data, String secretKey) {

        ObjectMapper mapper = new ObjectMapper();

        SignatureVerifier verifier = new MacSigner(secretKey);
        Jwt jwt = JwtHelper.decodeAndVerify(data, verifier);
        Map resultMap = null;
        try {
            resultMap = mapper.readValue(jwt.getClaims(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
        return resultMap;

    }

    /**
     * userId 셋팅
     * @param jwt
     * @param secretKey
     * @return
     * @throws IOException
     */
    public static String getUserId(String jwt, String secretKey) {
        String userId = "";
        if(jwt != null)
            userId = jwtDecoder(jwt, secretKey).get("id");

        return userId;
    }

}
