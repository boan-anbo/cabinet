package com.boan.apps.cabinet.services;

import boan.pdfgongju.core.models.PdfAnnotation;
import boan.pdfgongju.core.models.PdfDocument;
import com.boan.apps.cabinet.repositories.CardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaAuditing // otherwise the @createdDate @modifiedDate will not work in test
public class CardServiceTest {

    private final String testPdfWithAnnots = new File("src/test/java/com/boan/apps/cabinet/services/test-with-annots.pdf").getAbsolutePath();
    private final String  testPdfWithTwoNewAnnots= new File("src/test/java/com/boan/apps/cabinet/services/test-with-annots-two-new.pdf").getAbsolutePath();

    @Autowired
    private CardService cardService;

    @Autowired
    private PdfService pdfService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CardRepository cardRepo;

    @Test
    public void testStorePdfsWithoutDuplications() throws IOException {

        var testFile = new File(testPdfWithAnnots);
        assertTrue(testFile.exists());


        var extractedPdfDoc = pdfService.extractPdfDoc(testFile.getAbsolutePath()).get();

        var firstAnnot = extractedPdfDoc.getExtractedPdf().stream().findFirst().get().getAnnotations().stream().findFirst().get();

        var checkCardExists = cardService.checkIfCardExists(firstAnnot);

        assertFalse(checkCardExists);

        System.out.println("firstAnnot.modifiedDate: " + firstAnnot.modifiedDate);

        assertEquals(4, extractedPdfDoc.getExtractedPdf().stream().findFirst().get().annotations.size());

        var cards = pdfService.extractAndStoreManyPdfandReturnNumber(Collections.singletonList(testFile.getAbsolutePath()), true);

        var savedCardsWithModifiedDAte = cardService.getCardsByModifiedDate(firstAnnot.modifiedDate).items;

        assertEquals(1, savedCardsWithModifiedDAte.size());

        assertEquals(4, cards);

        var firstCard = cardService.getCardsByModifiedDate(firstAnnot.modifiedDate).items.stream().findFirst().get();

        assertEquals(firstAnnot.modifiedDate, firstCard.getModified());

        var checkCardExistsAgain = cardService.checkIfCardExists(firstAnnot);

        assertTrue(checkCardExistsAgain);

        System.out.println("firstCard.modifiedDate: " + firstCard.getModified());



//        var extractPdf = pdfService.extractPdfDoc(testFileWithAnnots.getAbsolutePath());


    }

    @Test
    public void checkDuplicates() {

        var testDateString = "1639011957195";
        Long testDate = Long.parseLong(testDateString);

        var firstAnnot = new PdfAnnotation();
        firstAnnot.modifiedDate = new Date(testDate);
        firstAnnot.setText("test");
        firstAnnot.setPageIndex(0);
        var pdfDoc = new PdfDocument();

        // same card with same modified date and text. SHould be treated the same as the first annotaiton.
        var secondAnnot = new PdfAnnotation();
        secondAnnot.modifiedDate = new Date(testDate);
        secondAnnot.setText("test");
        secondAnnot.setPageIndex(0);

        // different card with different text and same modified date. Should be treated as different card.
        var thirdAnnot = new PdfAnnotation();
        thirdAnnot.modifiedDate = new Date(testDate);
        thirdAnnot.setText("test different");
        thirdAnnot.setPageIndex(0);

        assertFalse(cardService.checkIfCardExists(firstAnnot));
        assertFalse(cardService.checkIfCardExists(secondAnnot));
        assertFalse(cardService.checkIfCardExists(thirdAnnot));

        pdfService.storeSingleAnnotationFromDoc(pdfDoc, firstAnnot);


        // even though only the first annot is stored, it should return turn for the second annot and false for the third annot because the third has different text.
        assertTrue(cardService.checkIfCardExists(firstAnnot));
        assertTrue(cardService.checkIfCardExists(secondAnnot));
        assertFalse(cardService.checkIfCardExists(thirdAnnot));

        pdfService.storeSingleAnnotationFromDoc(pdfDoc, thirdAnnot);

        assertTrue(cardService.checkIfCardExists(firstAnnot));
        assertTrue(cardService.checkIfCardExists(secondAnnot));
        assertTrue(cardService.checkIfCardExists(thirdAnnot));

    }

}
