package com.project.utils.common;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KMS on 17/12/2019.
 */
public class MaskingUtils {

    /**
     * maskingName
     * 정규식으로 처리
     * @param str
     * @return String
     */
    public static String getRegexMaskingName(String str) {
        if(StringUtils.isEmpty(str)) return str;
        str = str.trim();
        String replaceString = str;

        String pattern = "";
        if(str.length() == 2) {
            pattern = "^(.)(.+)$";
        } else {
            pattern = "^(.)(.+)(.)$";
        }

        Matcher matcher = Pattern.compile(pattern).matcher(str);

        if(matcher.matches()) {
            replaceString = "";

            for(int i=1;i<=matcher.groupCount();i++) {
                String replaceTarget = matcher.group(i);
                if(i == 2) {
                    char[] c = new char[replaceTarget.length()];
                    Arrays.fill(c, '*');

                    replaceString = replaceString + String.valueOf(c);
                } else {
                    replaceString = replaceString + replaceTarget;
                }

            }
        }
        return replaceString;
    }

    /**
     * 이름 마스킹 처리
     * @param name
     * @return
     */
    public static String getMaskedName(String name) {
        if(StringUtils.isEmpty(name)) return name;
        name = name.trim();

        String maskedName = "";     // 마스킹 이름
        String firstName = "";      // 성
        String middleName = "";     // 이름 중간
        String lastName = "";       //이름 끝
        int lastNameStartPoint;     // 이름 시작 포인터

        if(!name.equals("") || name != null){
            if(name.length() > 1){
                firstName = name.substring(0, 1);
                lastNameStartPoint = name.indexOf(firstName);

                if(name.trim().length() > 2){
                    middleName = name.substring(lastNameStartPoint + 1, name.trim().length()-1);
                    lastName = name.substring(lastNameStartPoint + (name.trim().length() - 1), name.trim().length());
                }else{
                    middleName = name.substring(lastNameStartPoint + 1, name.trim().length());
                }

                String makers = "";
                for(int i = 0; i < middleName.length(); i++){
                    makers += "*";
                }

                lastName = middleName.replace(middleName, makers) + lastName;
                maskedName = firstName + lastName;
            }else{
                maskedName = name;
            }
        }

        return maskedName;
    }
}
