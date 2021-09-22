package com.boan.apps.cabinet.services;

import boan.pdfgongju.core.PdfGongju;
import boan.pdfgongju.core.models.ExtractOptions;
import boan.pdfgongju.core.models.ExtractResult;
import boan.pdfgongju.core.models.PdfAnnotation;
import boan.pdfgongju.core.models.PdfDocument;
import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.entities.Comment;
import com.boan.apps.cabinet.entities.Source;
import com.boan.apps.cabinet.entities.Tag;
import com.boan.apps.cabinet.repositories.CardRepository;
import com.boan.apps.cabinet.repositories.SourceRestRepository;
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
    private CardRepository cardRepo;

    @Autowired

    private SourceRestRepository sourceRepo;

    public ExtractResult ExtractPdf(String filePath) throws IOException {
        var opt = new ExtractOptions();

        return PdfGongju.extractFullTextByPdfPath(filePath, opt);
    }


    public int ExtractAndStorePdf(List<String> filePaths) throws IOException {

        AtomicInteger totalCards = new AtomicInteger();
        for (String filePath : filePaths) {

            var extractResult = ExtractPdf(filePath);

            for (int j = 0; j < extractResult.getExtractedPdf().size(); j++) {


                var pdfDoc = extractResult.getExtractedPdf().get(j);

                if (pdfDoc.annotations.size() > 0) {
                    var cardsSaved = StoreAnnotations(pdfDoc);
                    totalCards.addAndGet(cardsSaved.get());
                }
            }
        }


        return totalCards.get();
    }

    public AtomicInteger StoreAnnotations(PdfDocument pdfDoc) {
        List<Card> results = new ArrayList<Card>();

        AtomicInteger counter = new AtomicInteger();

        Source source = null;
        for (PdfAnnotation annot : pdfDoc.annotations) {

            var annotPageIndex = annot.getPageIndex();

            if (source == null || source.getPageIndex() < annotPageIndex) {

                source = new Source(pdfDoc, annot.getPageIndex());
            }

            // persis source first, because it's the parent for cards, so that card cannot cascade save source on its side.
            sourceRepo.save(source);

            // create new card
            var newCard = new Card(source);
            newCard.setText(annot.text);

            // save comment
            if (annot.comment != null && annot.comment.length() > 0) {


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


        };
        return counter;
    }


}
