package com.boan.apps.cabinet.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@NoArgsConstructor
public class ExtractPdfRequestOne {

    @NotNull
    public String filePath;
}
