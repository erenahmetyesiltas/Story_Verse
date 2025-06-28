package com.sweproject.storyVerse.request;

import lombok.Data;

@Data
public class UserChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String verifyPassword;
}