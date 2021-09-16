package com.boan.apps.cabinet.cabinet.controllers;

import com.boan.apps.cabinet.cabinet.dtos.ExtractRequest;
import com.boan.apps.cabinet.cabinet.services.ExtractorService;
import com.boan.apps.cabinet.tagger.models.ExtractResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("extractor")
@RestController
public class ExtractorController {

    @Autowired
    ExtractorService extractorService;

    @PostMapping
    public ExtractResult ExtractTags(@Valid @RequestBody ExtractRequest request) {

        return extractorService.extractTagsFromText(request.text, request.replace);
    }
}
