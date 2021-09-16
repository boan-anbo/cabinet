package com.boan.apps.cabinet.cabinet.services;

import com.boan.apps.cabinet.tagger.Tagger;
import com.boan.apps.cabinet.tagger.models.ExtractResult;
import org.springframework.stereotype.Service;

@Service
public class ExtractorService {

    public ExtractResult extractTagsFromText(String text) {
        return extractTagsFromText(text, true);
    }

    public ExtractResult extractTagsFromText(String text, boolean replace) {
        return Tagger.process(text, replace);
    }
}
