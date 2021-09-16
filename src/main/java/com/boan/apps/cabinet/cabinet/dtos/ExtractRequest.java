package com.boan.apps.cabinet.cabinet.dtos;


import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class ExtractRequest {
    public ExtractRequest() {
    }

    @NotNull
    @NotBlank
    public String text;

//    @Value("${replace:true}")
    @NotNull
    public Boolean replace;
}
