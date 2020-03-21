package com.project.utils.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by KMS on 10/12/2019.
 */
@Getter
@AllArgsConstructor
@ToString
public enum ConditionType {
    eq,
    notEqual,
    like,
    notLike,
    contains,
    startsWith,
    endsWith,
    in,
    notIn,
    isNull,
    isNotNull,
    between,
    notBetween,
    ne,
    lt,
    le,
    ge,
    lessThan,
    lessOrEqual,
    greaterThan,
    greaterOrEqual,
    isTrue,
    isFalse;
}
