package com.husyairi.ManaProgressAku.DTO.User;

import com.husyairi.ManaProgressAku.Enums.Role;

public class RegisterUser {
    private String email;

    private String password;

    private String name;

    // we disable bc user dont have option to have admin privilege. only i have hehe
//    private String role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
}
