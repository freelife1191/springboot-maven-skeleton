package com.project.component.excel.tutorial.reponseString;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by KMS on 29/08/2019.
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class StatRow {

    private final String name;
    private final int value1;
    private final int value2;

}
