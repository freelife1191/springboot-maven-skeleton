package com.project.component.code.controller;

import com.project.common.domain.CommonResult;
import com.project.component.code.domain.CommonDetailCode;
import com.project.component.code.packet.ReqCommonDetailCodeMod;
import com.project.component.code.packet.ReqCommonDetailCodeRegistMulti;
import com.project.component.code.packet.ReqCommonDetailCodeRegistOne;
import com.project.component.code.service.CommonDetailCodeService;
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
@Api(value = "공통 상세 코드 컨트롤러", tags = "공통 상세 코드")
public class CommonDetailCodeController {

    private final CommonDetailCodeService detailCodeService;

    @ApiOperation("공통 상세 코드 리스트 조회")
    @GetMapping("/detail")
    public CommonResult<List<CommonDetailCode>> getCommonDetailCode(CommonDetailCode commonDetailCode) throws Exception {
        return detailCodeService.selectCommonDetailCode(commonDetailCode);
    }

    @ApiOperation("공통 상세 코드 단건 등록")
    @PostMapping("/detail")
    public CommonResult<CommonDetailCode> commonDetailCodeRegistration(@Valid @RequestBody ReqCommonDetailCodeRegistOne regist,
                                                        WebRequest webRequest) throws Exception {
        webRequest.setAttribute("body", regist, RequestAttributes.SCOPE_REQUEST);
        return detailCodeService.insertCommonDetailCode(regist);
    }

    @ApiOperation("공통 상세 코드 다중 등록")
    @PostMapping("/detail/{id}")
    public CommonResult<List<CommonDetailCode>> commonDetailCodeMultiRegistration(@ApiParam("공통 메인 코드 ID")
                                                                                  @PathVariable("id") Integer id,
                                                                                  @Valid @RequestBody List<ReqCommonDetailCodeRegistMulti> registList,
                                                                       WebRequest webRequest) throws Exception {
        webRequest.setAttribute("body", registList, RequestAttributes.SCOPE_REQUEST);
        return detailCodeService.insertCommonDetailCode(id, registList);
    }

    @ApiOperation("공통 상세 코드 수정")
    @PatchMapping("/detail")
    public CommonResult<Boolean> commonDetailCodeModification(@Valid @RequestBody ReqCommonDetailCodeMod mod,
                                                        WebRequest webRequest) throws Exception {
        webRequest.setAttribute("body", mod, RequestAttributes.SCOPE_REQUEST);
        return detailCodeService.updateCommonDetailCode(mod);
    }

    @ApiOperation("공통 상세 코드 삭제")
    @DeleteMapping("/{id}/detail/{code}")
    public CommonResult<Boolean> commonCodeDeletion(@ApiParam("공통 메인 코드 ID")
                                                    @PathVariable("id") Integer id,
                                                    @ApiParam("공통 상세 코드")
                                                    @PathVariable("code") Integer code) throws Exception {
        return detailCodeService.deleteCommonDetailCode(id, code);
    }


}
