package com.boan.apps.cabinet.cabinet.controllers;

import com.boan.apps.cabinet.cabinet.dtos.ParseRequest;
import com.boan.apps.cabinet.cabinet.services.TaggerService;
import com.boan.apps.cabinet.tagger.models.ParseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Tag parser controller", tags = "Parser Controller")
@RequestMapping("parser")
@RestController
public class ParserController {

    final TaggerService taggerService;

    public ParserController(TaggerService taggerService) {
        this.taggerService = taggerService;
    }

    @ApiOperation(value = "Parse tags in the string and return tags and original or replaced text.")
    @PostMapping
    public ParseResult ParseTags(@Valid @RequestBody ParseRequest request) {

        return taggerService.extractTagsFromText(request.text, request.replace);
    }
}
