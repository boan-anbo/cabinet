package com.boan.apps.cabinet.cabinet.controllers;

import com.boan.apps.cabinet.cabinet.dtos.ExtractRequest;
import com.boan.apps.cabinet.cabinet.repositories.TagRepository;
import com.boan.apps.cabinet.tagger.Tagger;
import com.boan.apps.cabinet.tagger.models.ExtractResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TagController {
    private final TagRepository tagRepository;

    @Autowired
    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @PostMapping("/test")
    public ExtractResult Test(@Valid @RequestBody ExtractRequest request) {


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
