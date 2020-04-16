package com.project.utils.common;

import com.project.component.code.dto.CommonCodeDto;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 차량 정보 유틸
 */
public class CodeUtils {

    /**
     * 공통 메인 코드 조회
     * 공통 상세 코드명으로 공통 메인 코드 조회
     * @param detailCodeNm
     * @param codeDtoList
     * @return
     */
    public static Integer getCode(String detailCodeNm, List<CommonCodeDto> codeDtoList) {
        return getCommonCodeDto(detailCodeNm, codeDtoList).getCode().getCode();
    }

    /**
     * 공통 상세 코드 조회
     * 공통 상세 코드명으로 공통 상세 코드 조회
     * @param detailCodeNm
     * @param codeDtoList
     * @return
     */
    public static Integer getDetailCode(String detailCodeNm, List<CommonCodeDto> codeDtoList) {
        return getCommonCodeDto(detailCodeNm, codeDtoList).getDetailCode().getDetailCode();
    }

    /**
     * 공통 상세 코드 영문명 조회
     * 공통 상세 코드명으로 공통 상세 코드 영문명 조회
     * @param detailCodeNm
     * @param codeDtoList
     * @return
     */
    public static String getDetailCodeEngNm(String detailCodeNm, List<CommonCodeDto> codeDtoList) {
        return getCommonCodeDto(detailCodeNm, codeDtoList).getDetailCode().getDetailCodeEngNm();
    }

    /**
     * 공통 상세 코드 영문명 조회
     * 공통 상세 코드명으로 공통 상세 코드 영문명 조회
     * @param detailCode
     * @param codeDtoList
     * @return
     */
    public static String getDetailCodeEngNm(Integer detailCode, List<CommonCodeDto> codeDtoList) {
        return getCommonCodeDto(detailCode, codeDtoList).getDetailCode().getDetailCodeEngNm();
    }

    /**
     * CommonCodeDto 단건 추출
     * detailCodeNm으로 조회
     * @param detailCodeNm
     * @param codeDtoList
     * @return
     */
    public static CommonCodeDto getCommonCodeDto(String detailCodeNm, List<CommonCodeDto> codeDtoList) {
        return codeDtoList.stream()
                .filter(data -> StringUtils.isNotEmpty(data.getDetailCode().getDetailCodeNm()) && data.getDetailCode().getDetailCodeNm().equals(detailCodeNm))
                .findFirst().orElse(new CommonCodeDto());
    }

    /**
     * CommonCodeDto 단건 추출
     * detailCode 로 조회
     * @param detailCode
     * @param codeDtoList
     * @return
     */
    public static CommonCodeDto getCommonCodeDto(Integer detailCode, List<CommonCodeDto> codeDtoList) {
        return codeDtoList.stream()
                .filter(data -> Objects.nonNull(data.getDetailCode().getDetailCode()) && data.getDetailCode().getDetailCode().equals(detailCode))
                .findFirst().orElse(new CommonCodeDto());
    }

}
