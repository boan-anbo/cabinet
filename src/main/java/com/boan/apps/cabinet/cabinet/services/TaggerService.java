package com.boan.apps.cabinet.cabinet.services;

import com.boan.apps.cabinet.tagger.Tagger;
import com.boan.apps.cabinet.tagger.models.ParseResult;
import org.springframework.stereotype.Service;

@Service
public class TaggerService {

    public ParseResult extractTagsFromText(String text) {
        return extractTagsFromText(text, true);
    }

    public ParseResult extractTagsFromText(String text, boolean replace) {
        return Tagger.process(text, replace);
    }
}
