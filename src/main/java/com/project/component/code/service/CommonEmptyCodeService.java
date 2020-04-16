package com.project.component.code.service;

import com.project.component.code.domain.CommonCode;
import com.project.component.code.domain.CommonDetailCode;
import com.project.component.code.domain.CommonEmptyCode;
import com.project.component.code.dto.CommonCodeDto;
import com.project.component.code.repository.CommonEmptyCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by KMS on 30/03/2020.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommonEmptyCodeService {

    private final CommonEmptyCodeRepository emptyCodeRepository;

    /**
     * 공통 누락 코드 등록
     * @param code
     * @param detailCode
     */
    public int insertCommonEmptyCode(CommonCode code, CommonDetailCode detailCode) {
        CommonEmptyCode emptyCode = CommonEmptyCode.builder()
                .groupCode(code.getGroupCode())
                .commonCodeId(code.getId())
                .commonCodeId(detailCode.getCommonCodeId())
                .detailCode(detailCode.getDetailCode())
                .value(detailCode.getDetailCodeNm())
                .build();

        return insertCommonEmptyCode(emptyCode);

    }

    /**
     * 공통 누락 코드 등록
     * @param codeDto
     * @param value
     * @return
     */
    public int insertCommonEmptyCode(CommonCodeDto codeDto, String value) {
        CommonEmptyCode emptyCode = CommonEmptyCode.builder()
                .groupCode(codeDto.getCode().getGroupCode())
                .commonCodeId(codeDto.getDetailCode().getCommonCodeId())
                .detailCode(codeDto.getDetailCode().getDetailCode())
                .value(value)
                .build();

        return insertCommonEmptyCode(emptyCode);

    }

    /**
     * 공통 누락 코드 등록
     * @param detailCode
     * @param groupCode
     * @param value
     */
    public int insertCommonEmptyCode(CommonDetailCode detailCode, String groupCode, String value) {
        CommonEmptyCode emptyCode = CommonEmptyCode.builder()
                .groupCode(groupCode)
                .commonCodeId(detailCode.getCommonCodeId())
                .detailCode(detailCode.getDetailCode())
                .value(value)
                .build();

        return insertCommonEmptyCode(emptyCode);

    }

    /**
     * 공통 누락 코드 등록
     * @param code
     */
    public int insertCommonEmptyCode(CommonCode code, String value) {
        CommonEmptyCode emptyCode = CommonEmptyCode.builder()
                .groupCode(code.getGroupCode())
                .commonCodeId(code.getId())
                .value(value)
                .build();

        return insertCommonEmptyCode(emptyCode);
    }

    /**
     * 공통 누락 코드 등록
     * @param groupCode
     * @param value
     */
    public int insertCommonEmptyCode(String groupCode, String value) {
        CommonEmptyCode emptyCode = CommonEmptyCode.builder()
                .groupCode(groupCode)
                .value(value)
                .build();

        return insertCommonEmptyCode(emptyCode);
    }


    /**
     * 공통 누락 코드 등록
     * @param emptyCode
     */
    public int insertCommonEmptyCode(CommonEmptyCode emptyCode) {
        return emptyCodeRepository.insertCommonEmptyCode(emptyCode);
    }
}
