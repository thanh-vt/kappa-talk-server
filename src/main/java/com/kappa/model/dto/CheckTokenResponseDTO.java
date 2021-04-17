package com.kappa.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckTokenResponseDTO {

    @JsonProperty("user_name")
    private String userName;

    private String[] scope;

    private Boolean active;

    private Long id;

    private Long exp;

    private String[] authorities;

    @JsonProperty("client_id")
    private String clientId;

}
