package com.boan.apps.cabinet.cabinet.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ExtractPdfRequest {

    @NotNull
    @NotBlank
    public String filePath;
}
