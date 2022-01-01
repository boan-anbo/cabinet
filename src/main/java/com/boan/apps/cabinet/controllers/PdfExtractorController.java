package com.boan.apps.cabinet.controllers;

import com.boan.apps.cabinet.dtos.CabinetCardParams;
import com.boan.apps.cabinet.dtos.CabinetResultMany;
import com.boan.apps.cabinet.dtos.ExtractPdfRequest;
import com.boan.apps.cabinet.dtos.SaveResults;
import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.services.PdfService;
import com.boan.apps.cabinet.services.TaggerService;
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
    @Operation(description = "Extract and store pdf and return stats.")
    @PostMapping(
            value = "stats"
    )
    public SaveResults extractAndStorePdfAndReturnStats(@Valid @RequestBody ExtractPdfRequest request) throws IOException {

        var cardsSaved = this.pdfService.extractAndStoreManyPdfandReturnNumber(request.filePaths, true);
        return new SaveResults(cardsSaved, request.filePaths.size());
    }


    @SneakyThrows
    @Operation(description = "Extract and store pdf and return cards stored.")
    @PostMapping(
            value = "cards"
    )
    public CabinetResultMany<Card> extractReturnCards(@Valid @RequestBody ExtractPdfRequest request) throws IOException {

        var cardsSaved = this.pdfService.extractAndStoreManyPdfandReturnCards(request.filePaths, true);
        var params = new CabinetCardParams();
        params.setPage(1);
        params.setPageSize(cardsSaved.size());
        return new CabinetResultMany<Card>(cardsSaved, params);
    }
}
