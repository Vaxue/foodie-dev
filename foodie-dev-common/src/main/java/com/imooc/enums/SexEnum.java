package com.imooc.enums;

/**
 * @Desc: 性别 枚举 0 女 1 男 2保密
 */
public enum SexEnum {
    woman(0,"女"),
    man(1,"男"),
    secret(2,"保密");

    public final  Integer type;
    public String value;

    SexEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
