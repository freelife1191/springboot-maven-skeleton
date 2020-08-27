package com.project.component.code.controller;

import com.project.common.domain.CommonResult;
import com.project.component.code.packet.ReqCommonCodeGET;
import com.project.component.code.packet.ReqCommonCodeMod;
import com.project.component.code.packet.ReqCommonCodeRegist;
import com.project.component.code.packet.ResCommonCode;
import com.project.component.code.service.CommonCodeService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

/**
 * Created by KMS on 27/03/2020.
 */
@RestController
@RequestMapping("/common/code")
@RequiredArgsConstructor
@Api(value = "공통 메인 코드 컨트롤러", tags = "공통 메인 코드")
// @ApiIgnore
public class CommonCodeController {

    private final CommonCodeService codeService;

    @ApiOperation(value = "공통 메인 코드 리스트 조회(페이징)", notes = "# 공통 메인 코드 리스트 조회시 호출\n" +
            "___\n" +
            "총 데이터 건수의 경우 `total_elements`의 값을 보여주면 된다\n" +
            "## 페이징 응답 Sample Data\n" +
            "페이징 관련 객체 상세 설명 참조\n" +
            "```json" +
            "'[\n" +
            "{\n" +
            "  \"code\": 100,\n" +
            "  \"msg\": \"성공\",\n" +
            "  \"result\": {\n" +
            "    // 페이징 리스트 데이터\n" +
            "    \"content\": [],\n" +
            "    // 페이징 데이터 정보\n" +
            "    \"pageable\": {\n" +
            "      // sort 정보(무시)\n" +
            "      \"sort\": {\n" +
            "        \"sorted\": true,\n" +
            "        \"unsorted\": false,\n" +
            "        \"empty\": false\n" +
            "      },\n" +
            "      \"offset\": 0, // 다음 페이징 데이터 조회 시작 숫자 (몇번째 row부터 출력할 지. (1번째 row면 0))\n" +
            "      \"page_number\": 0, // 현재 페이지 번호 0번이 1페이지 페이지를 표현할때는 `0+1` 로 표현하면됨\n" +
            "      \"page_size\": 2, // 한 페이지의 데이터 ROW 사이즈\n" +
            "      \"unpaged\": false, // 페이징 데이터가 아니면 true\n" +
            "      \"paged\": true // 페이징 데이터 이면 true\n" +
            "    },\n" +
            "    \"last\": false, // true 이면 마지막 페이지 ( last=true 로 주면 마지막 페이지를 리턴 )\n" +
            "    \"total_pages\": 250, // size로 게산된 전체 페이지 수\n" +
            "    \"total_elements\": 500, // 전체 데이터 수\n" +
            "    \"size\": 2, // 한 페이지에서 보여줄 데이터 ROW 사이즈, size를 제한하지 않으면 기본값은 백엔드에서 설정한데로 지정되고 백엔드 설정도 없으면 기본값은 20\n" +
            "    \"number\": 0, // 현재 페이지 번호 0번이 1페이지 페이지를 표현할때는 `0+1` 로 표현하면됨\n" +
            "    // sort 정보(무시)\n" +
            "    \"sort\": {\n" +
            "      \"sorted\": true,\n" +
            "      \"unsorted\": false,\n" +
            "      \"empty\": false\n" +
            "    },\n" +
            "    \"number_of_elements\": 2, // 한 페이지에서 보여줄 데이터 ROW 사이즈\n" +
            "    \"first\": true, // true 이면 첫번째 페이지라는 의미 ( 요청시 first=true 로 주면 첫번째 페이지를 리턴 )\n" +
            "    \"empty\": false // 리스트 데이터가 비어있으면 true\n" +
            "  }\n" +
            "}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "## 조회할 페이지 번호\n" +
                    "`0`부터 시작하므로 **1페이지 조회**시 `0`으로 요청해야 됨" +
                    "___\n" +
                    "## 페이지 구성시 필요한 응답 데이터 정보\n" +
                    "`result` 객체 내에 위의 응답 Sample Data 를 참조하여 필요한 응답 페이징 데이터로 페이징을 구현하면됨 \n" +
                    "### 필수 참조 페이징 데이터\n" +
                    "- `total_pages`: `size` 대비 계산된 전체 페이지 수\n" +
                    "- `total_elements`: 전체 데이터 수\n" +
                    "- `size`: 한 페이지에서 보여줄 데이터 **ROW** 사이즈, `size`를 제한하지 않으면 기본값은 현재 `100`으로 지정되어있음\n" +
                    "- `number`: 현재 페이지 번호 `0`번이 **1페이지** 페이지를 표현할때는 `0+1` 로 표현하면됨", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "## 한페이지당 데이터 ROW 수\n" +
                    "`size` 에 따라서 전체 페이지수가 달라짐\n" +
                    "기본값은 `100`으로 보내지 않으면 한 페이지 당 `100` 데이터를 응답함", defaultValue = "100"),
            @ApiImplicitParam(name = "sort", value = "## 정렬\n" +
                    "- 공통코드 ID로 오름차순 정렬 - `common_code_id,asc`\n" +
                    "- 공통코드 ID로 내림차순 정렬 - `common_code_id,desc`\n\n" +
                    "### 정렬 가능한 항목\n" +
                    "- 공통코드 ID: `id`\n" +
                    "- 공통코드: `code`\n" +
                    "- 공통코드 명: `code_nm`\n" +
                    "- 공통코드 영문명: `code_eng_nm`\n" +
                    "- 공통코드 설명: `code_dc`\n" +
                    "- 정렬순서: `order`\n" +
                    "- 사용여부: `enabled`"
                    , defaultValue = "id,asc")
    })
    @GetMapping
    public CommonResult<Page<ResCommonCode>> getCommonCode(@PageableDefault(size = 100, direction = Sort.Direction.ASC) Pageable pageable,
                                                           @Valid ReqCommonCodeGET commonCodeGET) throws Exception {
        return codeService.selectCommonCode(pageable, commonCodeGET);
    }

    @ApiOperation("공통 메인 코드 등록")
    @PostMapping
    public CommonResult<ResCommonCode> commonCodeRegistration(@Valid @RequestBody ReqCommonCodeRegist regist,
                                                              WebRequest webRequest) throws Exception {
        webRequest.setAttribute("body", regist, RequestAttributes.SCOPE_REQUEST);
        return codeService.insertCommonCode(regist);
    }

    @ApiOperation("공통 메인 코드 수정")
    @PatchMapping("/{id}")
    public CommonResult<Boolean> commonCodeModification(@ApiParam(value = "### [ PathVariable ] 공통코드 ID", example = "1", required = true)
                                                        @PathVariable("id") Integer id,
                                                        @Valid @RequestBody ReqCommonCodeMod mod,
                                                        WebRequest webRequest) throws Exception {
        mod.setId(id);
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
