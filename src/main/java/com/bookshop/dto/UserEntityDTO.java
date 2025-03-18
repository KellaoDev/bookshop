package com.bookshop.dto;

import com.bookshop.entities.permissions.UserEnum;


public class UserEntityDTO {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String cpf;
    private String phone;
    private UserEnum userEnum;

    public UserEntityDTO() {
    }

    public UserEntityDTO(Long id, String username, String email, String password, String confirmPassword, String cpf, String phone, UserEnum userEnum) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.cpf = cpf;
        this.phone = phone;
        this.userEnum = userEnum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserEnum getUserEnum() {
        return userEnum;
    }

    public void setUserEnum(UserEnum userEnum) {
        this.userEnum = userEnum;
    }

}
