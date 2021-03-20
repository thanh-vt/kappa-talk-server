package com.kappa.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonComponent
public class SettingDTO implements Serializable {

    private static final long serialVersionUID = 43L;

    private Long id;

    private Long userId;

    @Builder.Default
    @JsonProperty("dark_mode")
    private Boolean darkMode = true;
}
