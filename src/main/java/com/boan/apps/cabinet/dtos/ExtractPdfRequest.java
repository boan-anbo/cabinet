package com.boan.apps.cabinet.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
public class ExtractPdfRequest {

    @NotNull
    public List<String> filePaths;
}
