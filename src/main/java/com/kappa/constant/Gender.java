package com.kappa.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
  * @created 25/04/2021 - 12:43:27 SA
  * @project vengeance
  * @author thanhvt
  * @description gender constants
  * @since 1.0
**/
public enum Gender {

    MALE(1),
    FEMALE(2),
    BISEXUAL(3),
    UNKNOWN(4);

    private static final Map<Integer, Gender> GENDER_MAP = new HashMap<>();

    static {
        for (Gender gender : Gender.values()) {
            GENDER_MAP.put(gender.value, gender);
        }
    }

    @Getter
    @JsonValue
    private final int value;

    Gender(int value) {
        this.value = value;
    }

    /**
     * @created 25/04/2021 - 12:44:06 SA
     * @author thanhvt
     * @description map int value to corresponding gender enum
     * @param integer value to convert to gender
     * @return gender enum
     */
    @JsonCreator
    public static Gender fromValue(Integer integer) {
        if (integer == null) return Gender.UNKNOWN;
        Gender gender = GENDER_MAP.get(integer);
        if (gender == null) return Gender.UNKNOWN;
        return gender;
    }

}
