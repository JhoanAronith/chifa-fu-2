package com.app.chifafu.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroClienteDTO {

    @NotEmpty(message = "Ingresar el nombre")
    private String nombres;
    @NotEmpty(message = "Ingresar el DNI")
    @Size(max = 8)
    private String dni;
    @NotEmpty(message = "Ingresar el teléfono")
    @Size(max = 9)
    private String telefono;
    @NotEmpty(message = "Ingresar el correo electrónico")
    @Email
    private String email;
    @NotEmpty(message = "Ingresar la dirección")
    private String direccion;
    @NotEmpty(message = "Ingresar la contraseña")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
    @NotEmpty(message = "Confirmar la contraseña")
    private String confirmPassword;

}
