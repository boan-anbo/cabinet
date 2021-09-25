package com.boan.apps.cabinet.services;

import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinetKt.MarkdownWriter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportService {

    public List<String> exportMarkdownLines(String listTitle, List<Card> cards) {

        return  MarkdownWriter.Companion.ExportLines(listTitle, cards);
    }

}
