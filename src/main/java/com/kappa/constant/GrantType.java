package com.kappa.constant;

import lombok.Getter;

/**
  * @created 25/04/2021 - 12:50:39 SA
  * @project vengeance
  * @author thanhvt
  * @description
  * @since 1.0
**/
public enum GrantType {

    IMPLICIT("implicit"),
    PASSWORD("password"),
    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token"),
    CLIENT_CREDENTIALS("client_credentials");

    @Getter
    private final String value;

    GrantType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
