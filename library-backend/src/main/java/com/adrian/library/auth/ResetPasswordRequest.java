package com.adrian.library.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResetPasswordRequest {

    private String newPassword;
    private String confirmationPassword;
}
