package com.project.component.code.service;

import com.project.common.domain.CommonResult;
import com.project.component.code.domain.CommonCode;
import com.project.component.code.packet.ReqCommonCodeGET;
import com.project.component.code.packet.ReqCommonCodeMod;
import com.project.component.code.packet.ReqCommonCodeRegist;
import com.project.component.code.packet.ResCommonCode;
import com.project.component.code.repository.CommonCodeRepository;
import com.project.utils.common.CommonUtils;
import com.project.utils.common.JooqUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
@Transactional
public class CommonCodeService {

    private final DSLContext dsl;
    private final CommonCodeRepository commonCodeRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * 공통 메인 코드 리스트 조회
     * @param commonCodeGET
     * @return
     * @throws Exception
     */
    public CommonResult<Page<ResCommonCode>> selectCommonCode(Pageable pageable, ReqCommonCodeGET commonCodeGET) throws Exception {
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), commonCodeRepository.selectCommonCode(pageable, commonCodeGET));
    }

    /**
     * 공통 메인 코드 등록
     * @param reqCommonCodeRegist
     * @return
     */
    public CommonResult<ResCommonCode> insertCommonCode(ReqCommonCodeRegist reqCommonCodeRegist) throws Exception {

        SQLDialect dialect = SQLDialect.DEFAULT;
        if(profile.equals("h2"))
            dialect = SQLDialect.H2;

        CommonCode registCommonCode = new CommonCode(reqCommonCodeRegist);
        ResCommonCode upperCommonCode = null;
        if(Objects.nonNull(registCommonCode.getUpperId()))
            upperCommonCode = commonCodeRepository.findById(registCommonCode.getUpperId());

        if(Objects.nonNull(upperCommonCode)) {
            registCommonCode.setDepth(upperCommonCode.getDepth()+1);
            registCommonCode.setPath(CommonUtils.getJoinData(Arrays.asList(upperCommonCode.getPath(), JooqUtils.getAutoIncrementValue(dsl, COMMON_CODE, dialect))));
            registCommonCode.setPathNm(CommonUtils.getJoinData(Arrays.asList(upperCommonCode.getPathNm(), registCommonCode.getCodeNm())));
        } else {
            registCommonCode.setPath(String.valueOf(JooqUtils.getAutoIncrementValue(dsl, COMMON_CODE, dialect)));
            registCommonCode.setPathNm(registCommonCode.getCodeNm());
        }

        int id = commonCodeRepository.insertCommonCode(registCommonCode);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), commonCodeRepository.findById(id));
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
            ResCommonCode originalCommonCode = commonCodeRepository.findById(reqCommonCodeMod.getId());

            if( originalCommonCode.getUpperId() != modCommonCode.getUpperId() ) {
                ResCommonCode upperCommonCode = commonCodeRepository.findById(reqCommonCodeMod.getUpperId());

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