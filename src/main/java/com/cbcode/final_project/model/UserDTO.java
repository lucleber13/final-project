package com.cbcode.final_project.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cbcode.final_project.roles.Role;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String userName;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    //@NotNull
    @Size(max = 255)
    @Enumerated(EnumType.STRING)
    private Role roles;

}
