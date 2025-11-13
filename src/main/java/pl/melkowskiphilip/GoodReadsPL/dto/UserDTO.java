package pl.melkowskiphilip.GoodReadsPL.dto;

import lombok.Data;
import pl.melkowskiphilip.GoodReadsPL.entity.Role;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private boolean enabled;

    private Role role;
}
