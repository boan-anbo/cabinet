package com.boan.apps.cabinet.cabinet.controllers;

import com.boan.apps.cabinet.cabinet.dtos.ExtractPdfRequest;
import com.boan.apps.cabinet.cabinet.entities.Card;
import com.boan.apps.cabinet.cabinet.services.PdfService;
import com.boan.apps.cabinet.cabinet.services.TaggerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Api(value = "Extract Pdf and store annotations as cards and notes", tags = "Extract Pdf Controller")
@RequestMapping("extract")
@RestController
public class PdfExtractorController {

    @Autowired
    private TaggerService taggerService;

    @Autowired
    private PdfService pdfService;



    @SneakyThrows
    @ApiOperation(value = "Extract and store pdf.")
    @PostMapping
    public List<Card> extractAndStorePdf(@Valid @RequestBody ExtractPdfRequest request) throws IOException {

        return this.pdfService.ExtractAndStorePdf(request.filePath);
    }
}