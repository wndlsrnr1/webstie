package com.website.controller.api.model.request.user;

import com.website.utils.common.constance.Regexes;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinFormRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 8, max = 20, message = "{Size.password}")
    @Pattern(
            regexp = Regexes.PASSWORD_PATTERN,
            message = "{Pattern.password}"
    )
    private String password;
    @NotBlank
    @Pattern(
            regexp = Regexes.PASSWORD_PATTERN,
            message = "{Pattern.password}"
    )
    private String password2;
    @NotBlank
    private String address;

}
