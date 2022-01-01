package com.boan.apps.cabinet.services;

import boan.pdfgongju.core.PdfGongju;
import boan.pdfgongju.core.models.ExtractOptions;
import boan.pdfgongju.core.models.ExtractResult;
import boan.pdfgongju.core.models.PdfAnnotation;
import boan.pdfgongju.core.models.PdfDocument;
import com.boan.apps.cabinet.controllers.CardController;
import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.entities.Comment;
import com.boan.apps.cabinet.entities.Source;
import com.boan.apps.cabinet.entities.Tag;
import com.boan.apps.cabinet.repositories.CardRepository;
import com.boan.apps.cabinet.repositories.SourceRestRepository;
import com.boan.apps.cabinetKt.MarkdownWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PdfService {

    Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private TaggerService taggerService;
    @Autowired
    private CardRepository cardRepo;


    @Autowired
    private SourceRestRepository sourceRepo;

    @Autowired
    private CardService cardService;

    public Optional<ExtractResult> extractPdfDoc(String filePath) throws IOException {
        var opt = new ExtractOptions();

        return PdfGongju.extractFullTextByPdfPath(filePath, opt);
    }


    /**
     * @param filePaths
     * @param noDuplicate if true, will check if the card already exists in the database and skip it if it does.
     * @return
     * @throws IOException
     */
    public List<Card> extractAndStoreManyPdfandReturnCards(List<String> filePaths, boolean noDuplicate) throws IOException {

        var allResults = new ArrayList<Card>();


        for (String filePath : filePaths) {

            var hasAnnotations = PdfGongju.checkIfPdfFileHasAnnotations(filePath);

            if (!hasAnnotations) {
                continue;
            }

            var extractResult = extractPdfDoc(filePath);

            if (extractResult.isPresent()) {


                for (int j = 0; j < extractResult.get().getExtractedPdf().size(); j++) {


                    var pdfDoc = extractResult.get().getExtractedPdf().get(j);

                    if (pdfDoc.annotations.size() > 0) {
                        var cardsSaved = storeMultipleAnnotationsByPdfDocument(pdfDoc, noDuplicate, true);

                        allResults.addAll(cardsSaved);
                    }
                }
            } else {
                logger.warn("Skipped. Did not received result for " + filePath);
            }
        }

        logger.info("Archivization finished. " + filePaths.size() + " files processed. " + allResults.size() + " stored.");


        return allResults;
    }

    /**
     * @param filePaths
     * @param noDuplicate if true, will check if the card already exists in the database and skip it if it does.
     * @return
     * @throws IOException
     */
    public int extractAndStoreManyPdfandReturnNumber(List<String> filePaths, boolean noDuplicate) throws IOException {

        AtomicInteger totalCards = new AtomicInteger();

        AtomicInteger totalFiles = new AtomicInteger();

        for (String filePath : filePaths) {

            var hasAnnotations = PdfGongju.checkIfPdfFileHasAnnotations(filePath);

            if (!hasAnnotations) {
                System.gc();
//                continue;
                continue;
            }

            Optional<ExtractResult> extractResult = null;
            try {
                extractResult = extractPdfDoc(filePath);

                if (extractResult.isPresent()) {


                    for (int j = 0; j < extractResult.get().getExtractedPdf().size(); j++) {


                        var pdfDoc = extractResult.get().getExtractedPdf().get(j);

                        if (pdfDoc.annotations.size() > 0) {
                            storeMultipleAnnotationsByPdfDocument(pdfDoc, noDuplicate, false);

                            totalCards.addAndGet(pdfDoc.annotations.size());
                        }
                    }
                } else {
                    logger.warn("Skipped. Did not received result for " + filePath);
                }
                System.gc();
                totalFiles.addAndGet(1);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        logger.info("Archivization finished. " + totalFiles.get() + " files processed. " + totalCards.get() + " stored.");


        return totalCards.get();
    }


    public List<Card> storeMultipleAnnotationsByPdfDocument(PdfDocument pdfDoc, boolean noDuplicates, boolean returnExistingCards) throws IOException {

        var resultCards = new ArrayList<Card>();

        AtomicInteger counter = new AtomicInteger();

        Source source = null;
        for (PdfAnnotation annot : pdfDoc.annotations) {

            var cardAlreadyExists = cardService.getCardsWithSameTextAndModifiedDate(annot);

            if (returnExistingCards && cardAlreadyExists.size() > 0) {
                resultCards.addAll(cardAlreadyExists);
            }
            // check duplicates.
            if (noDuplicates && cardAlreadyExists.size() > 0) {
                continue;
            }

            var annotPageIndex = annot.getPageIndex();

            if (source == null || source.getPageIndex() < annotPageIndex) {

                source = new Source(pdfDoc, annot.getPageIndex());
            }

            var savedCard = storeSingleAnnotationFromSource(source, annot);

            resultCards.add(savedCard);


            counter.getAndIncrement();
        }
        ;

        System.out.println(pdfDoc.getId());
        return resultCards;
    }

    private Card storeSingleAnnotationFromSource(Source source, PdfAnnotation annot) {
        // persis source first, because it's the parent for cards, so that card cannot cascade save source on its side.
        sourceRepo.save(source);
        return storeCardFromSourceAndAnnotation(source, annot);

    }

    public Card storeSingleAnnotationFromDoc(PdfDocument pdfDoc, PdfAnnotation annot) {
        Source source = new Source(pdfDoc, annot.getPageIndex());
        // persis source first, because it's the parent for cards, so that card cannot cascade save source on its side.
        sourceRepo.save(source);

        return storeCardFromSourceAndAnnotation(source, annot);
    }

    private Card storeCardFromSourceAndAnnotation(Source source, PdfAnnotation annot) {

        // create new card
        var newCard = new Card(source);
        newCard.setText(annot.text);
        newCard.setModified(annot.getModifiedDate());


        switch (annot.getAnnotationType().toLowerCase()) {
            case "highlight":
                newCard.setImportance(1);
            default:
                newCard.setImportance(0);
        }


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

        newCard.title = MarkdownWriter.getAlternativeTitles(newCard);

        return cardRepo.save(newCard);
    }


}
