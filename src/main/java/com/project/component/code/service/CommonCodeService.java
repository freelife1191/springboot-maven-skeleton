package com.project.component.code.service;

import com.project.common.domain.CommonResult;
import com.project.component.code.domain.CommonCode;
import com.project.component.code.packet.ReqCommonCodeMod;
import com.project.component.code.packet.ReqCommonCodeRegist;
import com.project.component.code.repository.CommonCodeRepository;
import com.project.utils.common.CommonUtils;
import com.project.utils.common.JooqUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.project.common.constant.ResCode.SUCCESS;
import static com.project.h2.entity.tables.JCommonCode.COMMON_CODE;

/**
 * 공통 메인 코드 서비스
 * Created by KMS on 26/03/2020.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonCodeService {

    private final DSLContext dsl;

    private final CommonCodeRepository commonCodeRepository;

    /**
     * 공통 메인 코드 리스트 조회
     * @param commonCode
     * @return
     * @throws Exception
     */
    public CommonResult<List<CommonCode>> selectCommonCode(CommonCode commonCode) throws Exception {
        List<CommonCode> commonCodeList = commonCodeRepository.selectCommonCode(commonCode);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), commonCodeList);
    }

    /**
     * 공통 메인 코드 등록
     * @param reqCommonCodeRegist
     * @return
     */
    public CommonResult<CommonCode> insertCommonCode(ReqCommonCodeRegist reqCommonCodeRegist) throws Exception {
        CommonCode registCommonCode = new CommonCode(reqCommonCodeRegist);
        CommonCode upperCommonCode = null;
        if(Objects.nonNull(registCommonCode.getUpperId()))
            upperCommonCode = commonCodeRepository.findById(registCommonCode.getUpperId());


        if(Objects.nonNull(upperCommonCode)) {
            registCommonCode.setDepth(upperCommonCode.getDepth()+1);
            registCommonCode.setPath(CommonUtils.getJoinData(Arrays.asList(upperCommonCode.getPath(), JooqUtils.getAutoIncrementValue(dsl, COMMON_CODE))));
            registCommonCode.setPathNm(CommonUtils.getJoinData(Arrays.asList(upperCommonCode.getPathNm(), registCommonCode.getCodeNm())));
        } else {
            registCommonCode.setPath(String.valueOf(JooqUtils.getAutoIncrementValue(dsl, COMMON_CODE)));
            registCommonCode.setPathNm(registCommonCode.getCodeNm());
        }

        int id = commonCodeRepository.insertCommonCode(registCommonCode);
        CommonCode commonCode = commonCodeRepository.findById(id);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), commonCode);
    }

    /**
     * 공통 메인 코드 수정
     * @param reqCommonCodeMod
     * @return
     */
    public CommonResult<Boolean> updateCommonCode(ReqCommonCodeMod reqCommonCodeMod) throws Exception {
        CommonCode modCommonCode = new CommonCode(reqCommonCodeMod);

        if(Objects.isNull(modCommonCode.getUpperId())) {
            modCommonCode.setDepth(0);
            modCommonCode.setPath(String.valueOf(modCommonCode.getId()));
            modCommonCode.setPathNm(modCommonCode.getCodeNm());
        } else {
            CommonCode originalCommonCode = commonCodeRepository.findById(reqCommonCodeMod.getId());

            if( originalCommonCode.getUpperId() != modCommonCode.getUpperId() ) {
                CommonCode upperCommonCode = commonCodeRepository.findById(reqCommonCodeMod.getUpperId());

                modCommonCode.setDepth(upperCommonCode.getDepth()+1);
                modCommonCode.setPath(CommonUtils.getJoinData(Arrays.asList(upperCommonCode.getPath(), upperCommonCode.getId())));
                modCommonCode.setPathNm(CommonUtils.getJoinData(Arrays.asList(upperCommonCode.getPathNm(), modCommonCode.getCodeNm())));
            }
        }

        int resultCount = commonCodeRepository.updateCommonCode(modCommonCode);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), BooleanUtils.toBoolean(resultCount));
    }

    /**
     * 공통 메인 코드 삭제
     * @param id
     * @return
     */
    public CommonResult<Boolean> deleteCommonCode(Integer id) throws Exception {
        int resultCount = commonCodeRepository.deleteCommonCode(id);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), BooleanUtils.toBoolean(resultCount));
    }

}