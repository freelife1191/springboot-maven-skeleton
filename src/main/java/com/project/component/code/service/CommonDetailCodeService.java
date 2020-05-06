package com.project.component.code.service;

import com.project.common.domain.CommonResult;
import com.project.component.code.domain.CommonCode;
import com.project.component.code.domain.CommonDetailCode;
import com.project.component.code.dto.CommonCodeDto;
import com.project.component.code.packet.ReqCommonDetailCodeMod;
import com.project.component.code.packet.ReqCommonDetailCodeRegistMulti;
import com.project.component.code.packet.ReqCommonDetailCodeRegistOne;
import com.project.component.code.repository.CommonCodeRepository;
import com.project.component.code.repository.CommonDetailCodeRepository;
import com.project.exception.common.DataRegistrationFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.project.common.constant.ResCode.SUCCESS;

/**
 * 공통 상세 코드 서비스
 * Created by KMS on 26/03/2020.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommonDetailCodeService {

    private final CommonCodeRepository codeRepository;

    private final CommonDetailCodeRepository detailCodeRepository;

    /**
     * 공통 코드 조인 데이터 리스트 조회
     * @param code
     * @return
     */
    public List<CommonCodeDto> getCommonCodeDtoList(CommonCode code) {
        return getCommonCodeDtoList(code, null);
    }

    /**
     * 공통 코드 조인 데이터 리스트 조회
     * @param detailCode
     * @return
     */
    public List<CommonCodeDto> getCommonCodeDtoList(CommonDetailCode detailCode) {
        return getCommonCodeDtoList(null, detailCode);
    }

    /**
     * 공통 코드 조인 데이터 리스트 조회
     * @param code
     * @param detailCode
     * @return
     */
    public List<CommonCodeDto> getCommonCodeDtoList(CommonCode code, CommonDetailCode detailCode) {

        CommonCodeDto condition = CommonCodeDto.builder()
                .code(code)
                .detailCode(detailCode)
                .build();

        return getCommonCodeDtoList(condition);
    }

    /**
     * 공통 코드 조인 데이터 리스트 조회
     * @param commonCodeDto
     * @return
     */
    public List<CommonCodeDto> getCommonCodeDtoList(CommonCodeDto commonCodeDto) {
        return detailCodeRepository.selectCommonCodeJoin(commonCodeDto);
    }

    /**
     * 공통 상세 코드 리스트 조회
     * @param commonDetailCode
     * @return
     */
    public CommonResult<List<CommonDetailCode>> selectCommonDetailCode(CommonDetailCode commonDetailCode) {
        List<CommonDetailCode> commonDetailCodeList = detailCodeRepository.selectCommonDetailCode(commonDetailCode);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), commonDetailCodeList);
    }

    /**
     * 공통 상세 코드 단건 등록
     * @param regist
     * @return
     */
    public CommonResult<CommonDetailCode> insertCommonDetailCode(ReqCommonDetailCodeRegistOne regist) throws Exception {
        CommonDetailCode commonDetailCode = new CommonDetailCode(regist);

        CommonCode commonCode = codeRepository.findById(regist.getCommonCodeId());
        if(Objects.isNull(commonCode))
            throw new DataRegistrationFailedException("등록된 공통 메인 코드가 없어서 공통 상세 코드 등록이 실패 했습니다");

        int detailCode = detailCodeRepository.insertCommonDetailCode(commonDetailCode);
        CommonDetailCode resultDetailCode = detailCodeRepository.findByCode(regist.getCommonCodeId(), detailCode);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), resultDetailCode);
    }

    /**
     * 공통 상세 코드 다중 등록
     * @param registList
     * @return
     */
    public CommonResult<List<CommonDetailCode>> insertCommonDetailCode(Integer id, List<ReqCommonDetailCodeRegistMulti> registList) throws Exception {
        List<CommonDetailCode> commonDetailCodeList = new ArrayList<>();

        CommonCode commonCode = codeRepository.findById(id);
        if(Objects.isNull(commonCode))
            throw new DataRegistrationFailedException("등록된 공통 메인 코드가 없어서 공통 상세 코드 등록이 실패 했습니다");

        registList.forEach(regist -> commonDetailCodeList.add(new CommonDetailCode(id, regist)));

        List<Integer> detailCodes = detailCodeRepository.insertCommonDetailCode(id, commonDetailCodeList);
        List<CommonDetailCode> resultDetailCode = detailCodeRepository.findByCodes(id, detailCodes);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), resultDetailCode);
    }

    /**
     * 공통 상세 코드 수정
     * @param mod
     * @return
     */
    public CommonResult<Boolean> updateCommonDetailCode(ReqCommonDetailCodeMod mod) throws Exception {
        CommonDetailCode commonDetailCode = new CommonDetailCode(mod);
        int resultCount = detailCodeRepository.updateCommonDetailCode(commonDetailCode);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), BooleanUtils.toBoolean(resultCount));
    }

    /**
     * 공통 상세 코드 삭제
     * @param commonCodeId
     * @param detailCode
     * @return
     */
    public CommonResult<Boolean> deleteCommonDetailCode(Integer commonCodeId, Integer detailCode) throws Exception {
        int resultCount = detailCodeRepository.deleteCommonDetailCode(commonCodeId, detailCode);
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), BooleanUtils.toBoolean(resultCount));
    }

}
