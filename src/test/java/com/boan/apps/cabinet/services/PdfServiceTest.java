package com.boan.apps.cabinet.services;

import boan.pdfgongju.core.models.PdfAnnotation;
import com.boan.apps.cabinet.repositories.CardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaAuditing // otherwise the @createdDate @modifiedDate will not work in test
public class PdfServiceTest {

    private final String testPdfWithAnnots = new File("src/test/java/com/boan/apps/cabinet/services/test-with-annots.pdf").getAbsolutePath();
    private final String  testPdfWithTwoNewAnnots= new File("src/test/java/com/boan/apps/cabinet/services/test-with-annots-two-new.pdf").getAbsolutePath();

    @Autowired
    private PdfService pdfService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CardRepository cardRepo;

    @Test
    public void extractPdf() throws IOException {

        var testFile = new File(testPdfWithAnnots);
        assertTrue(testFile.exists());

        var extractPdf = pdfService.extractPdfDoc(testFile.getAbsolutePath());
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(extractPdf));
    }

    @Test
    public void checkDuplicateAnnotation() throws IOException {

        var testFileWithAnnots = new File(testPdfWithAnnots);
        var testFileWithTwoNewAnnots = new File(testPdfWithTwoNewAnnots);
        assertTrue(testFileWithAnnots.exists());

        var extractPdf = pdfService.extractPdfDoc(testFileWithAnnots.getAbsolutePath());

        ObjectMapper mapper = new ObjectMapper();
        Assertions.assertEquals(1, extractPdf.get().getExtractedPdf().stream().count());
        List<PdfAnnotation> firstAnnotations = new ArrayList<>();
        firstAnnotations.addAll(extractPdf.get().getExtractedPdf().stream().findFirst().get().annotations);
        assertEquals(4, firstAnnotations.size());


        var extractPdfNew = pdfService.extractPdfDoc(testFileWithTwoNewAnnots.getAbsolutePath());
        List<PdfAnnotation> secondAnnotations = new ArrayList<>();
        secondAnnotations.addAll(extractPdfNew.get().getExtractedPdf().stream().findFirst().get().annotations);
        assertEquals(6, secondAnnotations.size());



        if (extractPdf.isPresent()) {

            var extractPdfString = extractPdf.get().getExtractedPdf();
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(extractPdfString));
        }

        if (extractPdfNew.isPresent()) {

            var extractPdfNewString = extractPdfNew.get().getExtractedPdf();
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(extractPdfNewString));
        }
    }

    @Test
//    @Transactional
    public void extractAndStorePdf() throws IOException {

        var testFile = new File(testPdfWithAnnots);
        assertTrue(testFile.exists());

        var cards = pdfService.extractAndStoreManyPdfandReturnNumber(Collections.singletonList(testFile.getAbsolutePath()), true);

        assertEquals(4, cards);

        var cardsAgain = pdfService.extractAndStoreManyPdfandReturnNumber(Collections.singletonList(testFile.getAbsolutePath()), true);


        // should return 0 because 4 are duplicated.
        assertEquals(0, cardsAgain);

        System.out.println(cardsAgain);

        // find all cards;
        int counter = 0;
        for (Object i : cardRepo.findAll()) {
            counter++;
        }

        assertEquals(4, counter);



        var cardsAgainDuplicated = pdfService.extractAndStoreManyPdfandReturnNumber(Collections.singletonList(testFile.getAbsolutePath()), false);

        // should return 4 because duplicates are allowed.
        assertEquals(4, cardsAgainDuplicated);


        // find all cards;
        int counterAgain = 0;
        for (Object i : cardRepo.findAll()) {
            counterAgain++;
        }

        assertEquals(8, counterAgain);


    }

}
