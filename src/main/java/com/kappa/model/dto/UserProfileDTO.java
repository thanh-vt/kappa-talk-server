package com.kappa.model.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.kappa.constant.Gender;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO implements Serializable {

    private String username;

    @NotBlank(message = "{validation.firstName.notBlank}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.notBlank}")
    private String lastName;

    @NotNull(message = "{validation.dateOfBirth.notNull}")
    private Timestamp dateOfBirth;

    @NotNull(message = "{validation.gender.notNull}")
    private Gender gender;

    @Pattern(regexp = "^(\\s*|(\\+84|84|0)+([3|5|7|8|9])([0-9]{8}))$",
            message = "{validation.phoneNumber.invalid}")
    private String phoneNumber;

    @Email(message = "{validation.email.invalid}")
    private String email;

    private String avatarUrl;

    private boolean isOnline;

    @JsonRawValue
    private String otherInfo;

    public UserProfileDTO(String username, String firstName, String lastName,
                          Timestamp dateOfBirth, Gender gender, String phoneNumber,
                          String email, String avatarUrl) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }
}
