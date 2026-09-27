package com.example.demo.dto;

import com.example.demo.entity.User;
import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String email;
    private String nickname;
    private String visaType;
    private String nationality;
    private String role;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.email = user.getEmail();
        response.nickname = user.getNickname();
        response.visaType = user.getVisaType();
        response.nationality = user.getNationality();
        response.role = user.getRole();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getVisaType() {
        return visaType;
    }

    public String getNationality() {
        return nationality;
    }

    public String getRole() {
        return role;
    }
}
