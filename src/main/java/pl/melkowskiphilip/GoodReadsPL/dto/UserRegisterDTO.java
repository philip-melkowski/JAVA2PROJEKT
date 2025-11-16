package pl.melkowskiphilip.GoodReadsPL.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String email;
    private String password;
}