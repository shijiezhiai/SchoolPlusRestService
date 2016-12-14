package com.websystique.springmvc.types;

/**
 * Created by yangyma on 11/23/16.
 */
public enum SchoolTypeEnum {

    KINDERGARTEN((short)0),
    PRIMARY((short)1),
    JUNIOR_HIGH((short)2),
    SENIOR_HIGH((short)3);

    private short type;


    SchoolTypeEnum(short type) {
        this.type = type;
    }

    public SchoolTypeEnum fromType(short type) {
        switch (type) {
            case 0:
                return KINDERGARTEN;
            case 1:
                return PRIMARY;
            case 2:
                return JUNIOR_HIGH;
            case 3:
                return SENIOR_HIGH;
            default:
                return null;
        }
    }
}
