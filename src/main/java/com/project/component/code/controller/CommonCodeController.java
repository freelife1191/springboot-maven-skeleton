package com.project.component.code.controller;

import com.project.common.domain.CommonResult;
import com.project.component.code.domain.CommonCode;
import com.project.component.code.packet.ReqCommonCodeMod;
import com.project.component.code.packet.ReqCommonCodeRegist;
import com.project.component.code.service.CommonCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by KMS on 27/03/2020.
 */
@RestController
@RequestMapping("/common/code")
@RequiredArgsConstructor
@Api(value = "공통 메인 코드 컨트롤러", tags = "공통 메인 코드")
public class CommonCodeController {

    private final CommonCodeService codeService;

    @ApiOperation("공통 메인 코드 리스트 조회")
    @GetMapping
    public CommonResult<List<CommonCode>> getCommonCode(CommonCode commonCode) throws Exception {
        return codeService.selectCommonCode(commonCode);
    }

    @ApiOperation("공통 메인 코드 등록")
    @PostMapping
    public CommonResult<CommonCode> commonCodeRegistration(@Valid @RequestBody ReqCommonCodeRegist regist,
                                                        WebRequest webRequest) throws Exception {
        webRequest.setAttribute("body", regist, RequestAttributes.SCOPE_REQUEST);
        return codeService.insertCommonCode(regist);
    }

    @ApiOperation("공통 메인 코드 수정")
    @PatchMapping
    public CommonResult<Boolean> commonCodeModification(@Valid @RequestBody ReqCommonCodeMod mod,
                                                        WebRequest webRequest) throws Exception {
        webRequest.setAttribute("body", mod, RequestAttributes.SCOPE_REQUEST);
        return codeService.updateCommonCode(mod);
    }

    @ApiOperation("공통 메인 코드 삭제")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> commonCodeDeletion(@ApiParam("공통 메인 코드 ID")
                                                    @PathVariable("id") Integer id) throws Exception {
        return codeService.deleteCommonCode(id);
    }


}
