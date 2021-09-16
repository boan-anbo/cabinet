package com.boan.apps.cabinet.cabinet.dtos;


import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.validation.Valid;

@Data
public class ExtractRequest {
    public ExtractRequest() {
    }

    @Valid
    @NotNull
    public String text;

    @Valid
    @NotNull
    public Boolean replace = true;
}
