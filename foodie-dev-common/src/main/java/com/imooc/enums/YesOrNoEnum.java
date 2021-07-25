package com.imooc.enums;

/**
 * @Desc: 是否 枚举
 */
public enum YesOrNoEnum {
    YES(1,"是"),
    NO(0,"否");
    public final Integer type;
    public final String value;

    YesOrNoEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}

