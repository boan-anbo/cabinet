package com.boan.apps.cabinet.cabinet.controllers;

import com.boan.apps.cabinet.cabinet.dtos.ParseRequest;
import com.boan.apps.cabinet.cabinet.services.TaggerService;
import com.boan.apps.cabinet.tagger.models.ParseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Tag parser controller", description = "Parser Controller")
@RequestMapping("parser")
@RestController
public class ParserController {

    final TaggerService taggerService;

    public ParserController(TaggerService taggerService) {
        this.taggerService = taggerService;
    }

    @Operation(description = "Parse tags in the string and return tags and original or replaced text.")
    @PostMapping
    public ParseResult ParseTags(@Valid @RequestBody ParseRequest request) {

        return taggerService.extractTagsFromText(request.text, request.replace);
    }
}
