package com.imooc.enums;

public enum CategoryEnum {
    OneCat(1,"一级分类"),
    TwoCat(2,"二级分类"),
    ThreeCat(3,"三级分类");

    public final Integer type;
    public String value;

    CategoryEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
