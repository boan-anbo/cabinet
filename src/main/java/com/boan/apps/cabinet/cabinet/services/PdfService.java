package com.boan.apps.cabinet.cabinet.services;

import boan.pdfgongju.core.Extract;
import boan.pdfgongju.core.extractors.ExtractOptions;
import boan.pdfgongju.core.models.PdfDocument;
import com.boan.apps.cabinet.cabinet.entities.Card;
import com.boan.apps.cabinet.cabinet.entities.Comment;
import com.boan.apps.cabinet.cabinet.repositories.CardRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    private TaggerService taggerService;
    @Autowired
    private CardRestRepository cardRepo;

    public PdfDocument ExtractPdf(String filePath) throws IOException {
        var opt = new ExtractOptions();

        return Extract.extractFullTextByPdfPath(filePath, opt);
    }


    public List<Card> ExtractAndStorePdf(String filePath) throws IOException {

        var pdfDoc = ExtractPdf(filePath);
        List<Card> results = new ArrayList<Card>();

        if (pdfDoc.annotations.size() > 0) {
            var saved = StoreAnnotations(pdfDoc);
            results.addAll(saved);
        }

        return results;
    }

    public List<Card> StoreAnnotations(PdfDocument pdfDoc) {
        List<Card> results =  new ArrayList<Card>();

        pdfDoc.annotations.forEach(annot -> {

            var newCard = new Card();
            newCard.setText(annot.text);

            if (annot.comment.length() > 0) {

                var newComment = new Comment();
                newComment.setText(annot.comment);

                newCard.comments.add(newComment);

                var newTags = taggerService.extractTagsFromText(newComment.getText());
            }

            var savedCard = cardRepo.save(newCard);

            System.out.println(pdfDoc.getId());

            results.add(savedCard);

        });
        return results;
    }


}
