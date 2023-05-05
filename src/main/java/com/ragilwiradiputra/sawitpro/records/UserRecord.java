package com.ragilwiradiputra.sawitpro.records;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserRecord(

    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number cannot be empty")
    @Size(min = 10, max = 13, message = "Minimal length phone number is 10 and max length is 13")
    @Pattern(regexp = "^08\\d*$", message = "Phone number must start with 08")
    String phoneNumber,

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name number cannot be empty")
    @Size(min = 3, max = 60, message = "Minimal length name is 3 and max length is 60")
    String name,

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 16, message = "Minimal length phone number is 6 and max length is 16")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "Password must containing at least 1 capital letter and 1 number")
    String password) {

}
