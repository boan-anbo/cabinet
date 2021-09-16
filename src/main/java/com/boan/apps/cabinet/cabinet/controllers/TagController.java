package com.boan.apps.cabinet.cabinet.controllers;

import com.boan.apps.cabinet.cabinet.dtos.ParseRequest;
import com.boan.apps.cabinet.cabinet.repositories.TagRestRepository;
import com.boan.apps.cabinet.tagger.Tagger;
import com.boan.apps.cabinet.tagger.models.ParseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("tags")
public class TagController {
    private final TagRestRepository tagRepository;

    @Autowired
    public TagController(TagRestRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @PostMapping("test")
    public ParseResult Test(@Valid @RequestBody ParseRequest request) {


//        var newTag = new Tag();
//
//
//
//        newTag.setKey("Hello");
//
//        tagRepository.save(newTag);
        return Tagger.process(request.text);
    }
}
