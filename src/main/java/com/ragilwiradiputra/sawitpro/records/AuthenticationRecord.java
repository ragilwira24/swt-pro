package com.ragilwiradiputra.sawitpro.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AuthenticationRecord(
    @NotNull
    @NotBlank
    @Pattern(regexp = "^08\\d*$")
    String username,

    @NotNull
    @NotBlank
    String password) {
}
