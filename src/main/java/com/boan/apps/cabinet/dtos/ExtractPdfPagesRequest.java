package com.boan.apps.cabinet.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Extract and return cards on specific pages.
 * This is especially for occasions where the user only needs cards on specific pages extracted and stored, usually during update.
 */
@Data
@NoArgsConstructor
public class ExtractPdfPagesRequest {

    @NotNull
    public String filePath;

    /**
     * An array with page indices of the pdf. All cards on the page indices will be returned to the user.
     */
    @NotNull
    public List<Integer> pageIndices;

    /**
     * Whether store all cards on all pages in the pdf, even though only cards on specified pages will be returned. If false, only cards on pageIndices will be returned.
     * Default to true;
     */
    public Boolean storeAllPages = true;

}
