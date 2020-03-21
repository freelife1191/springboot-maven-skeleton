package com.project.api.sample.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 취미 상수
 * Created by KMS on 12/03/2020.
 */
public enum Hobby {
    GAME,
    PROGRAMING,
    MOVIE,
    TRAVEL,
    METTING,
    BADOOK,
    SWIMMING;

    private static Map<String, Hobby> strToMap;

    /**
     * String 값으로 Hobby 맵핑
     * @param hobby
     * @return
     */
    public static Hobby getHobby(String hobby) {
        if(strToMap == null) {
            initMapping();
        }
        return strToMap.get(hobby);
    }

    /**
     * 맵 초기화
     */
    private static void initMapping() {
        strToMap = new HashMap<>();
        for (Hobby hobby : values()) {
            strToMap.put(hobby.name(), hobby);
        }
    }
}
