package com.boan.apps.cabinet.cabinet.controllers;

import com.boan.apps.cabinet.cabinet.dtos.ExtractPdfRequest;
import com.boan.apps.cabinet.cabinet.dtos.SaveResults;
import com.boan.apps.cabinet.cabinet.services.PdfService;
import com.boan.apps.cabinet.cabinet.services.TaggerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@Tag(description = "Extract Pdf and store annotations as cards and notes", name = "Extract Pdf Controller")
@RequestMapping("extract")
@RestController
public class PdfExtractorController {

    @Autowired
    private TaggerService taggerService;

    @Autowired
    private PdfService pdfService;



    @SneakyThrows
    @Operation(description = "Extract and store pdf.")
    @PostMapping
    public SaveResults extractAndStorePdf(@Valid @RequestBody ExtractPdfRequest request) throws IOException {

        var cardsSaved = this.pdfService.ExtractAndStorePdf(request.filePaths);
        return new SaveResults(cardsSaved, request.filePaths.size());
    }
}
