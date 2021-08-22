package com.project.api.sample.packet;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KMS on 2021/04/26.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@ApiModel("파일 업로드 요청 FORM 객체")
public class ReqFileDataFormSample {
    private List<ReqFileDataSample> dataList = new ArrayList<>();
}
