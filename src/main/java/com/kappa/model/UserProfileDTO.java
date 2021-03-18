package com.kappa.model;

import com.kappa.constant.Gender;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO implements Serializable {

    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private Timestamp dateOfBirth;

    private Gender gender;

    private String phoneNumber;

    @Email
    @NotBlank
    private String email;

    private String avatarUrl;

}
