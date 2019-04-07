package com.sheygam.java_23_07_04_19_cw;

public class AuthRequestDto {
    String email;
    String password;

    public AuthRequestDto() {
    }

    public AuthRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
