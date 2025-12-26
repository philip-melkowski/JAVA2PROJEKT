package pl.melkowskiphilip.GoodReadsPL.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.melkowskiphilip.GoodReadsPL.entity.Role;


@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {


    private String token;

    private String username;

    private Long userId;

    private Role role;


}
