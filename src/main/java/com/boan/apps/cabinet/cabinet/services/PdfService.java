package com.boan.apps.cabinet.cabinet.services;

import boan.pdfgongju.core.Extract;
import boan.pdfgongju.core.extractors.ExtractOptions;
import boan.pdfgongju.core.models.PdfDocument;
import com.boan.apps.cabinet.cabinet.entities.Card;
import com.boan.apps.cabinet.cabinet.entities.Comment;
import com.boan.apps.cabinet.cabinet.entities.Source;
import com.boan.apps.cabinet.cabinet.entities.Tag;
import com.boan.apps.cabinet.cabinet.repositories.CardRestRepository;
import com.boan.apps.cabinet.cabinet.repositories.SourceRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PdfService {

    @Autowired
    private TaggerService taggerService;
    @Autowired
    private CardRestRepository cardRepo;

    @Autowired

    private SourceRestRepository sourceRepo;

    public PdfDocument ExtractPdf(String filePath) throws IOException {
        var opt = new ExtractOptions();

        return Extract.extractFullTextByPdfPath(filePath, opt);
    }


    public int ExtractAndStorePdf(List<String> filePaths) throws IOException {

        AtomicInteger totalCards = new AtomicInteger();
        for (int i = 0; i < filePaths.size(); i++) {

            var pdfDoc = ExtractPdf(filePaths.get(i));

            if (pdfDoc.annotations.size() > 0) {
                var cardsSaved = StoreAnnotations(pdfDoc);
               totalCards.addAndGet(cardsSaved.get());
            }
        }


        return totalCards.get();
    }

    public AtomicInteger StoreAnnotations(PdfDocument pdfDoc) {
        List<Card> results =  new ArrayList<Card>();

        AtomicInteger counter = new AtomicInteger();

        var source = new Source(pdfDoc);

        // persis source first, because it's the parent for cards, so that card cannot cascade save source on its side.
        sourceRepo.save(source);


        pdfDoc.annotations.forEach(annot -> {

            var newCard = new Card(source);
            newCard.setText(annot.text);

            if (annot.comment.length() > 0) {


                var parseResult = taggerService.extractTagsFromText(annot.getComment());
                // save comment

                if (parseResult.getProcessedText().trim().length() > 0) {

                    var newComment = new Comment();
                    newComment.setText(parseResult.getProcessedText());

                    newCard.comments.add(newComment);
                }

                // saving tags
                var tagExtracts = parseResult.getTags();
                tagExtracts.forEach(tagExtract -> {
                    var newTag = new Tag(tagExtract);
                    newCard.tags.add(newTag);
                });
            }

            cardRepo.save(newCard);

            System.out.println(pdfDoc.getId());

            counter.getAndIncrement();


        });
        return  counter;
    }


}
