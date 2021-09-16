package com.boan.apps.cabinet.cabinet.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PdfServiceTest {

    private final String filePath = new File("src/test/java/com/boan/apps/cabinet/cabinet/services/test-with-annots.pdf").getAbsolutePath();

    @Autowired
    private PdfService pdfService;

    @Test
    public void extractPdf() throws IOException {

        var testFile = new File(filePath);
        assertTrue(testFile.exists());

        var extractPdf = pdfService.ExtractPdf(testFile.getAbsolutePath());
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(extractPdf))        ;
    }

    @Test
    public void extractAndStorePdf() throws IOException {

        var testFile = new File(filePath);
        assertTrue(testFile.exists());

        var cards = pdfService.ExtractAndStorePdf(testFile.getAbsolutePath());
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cards))        ;
    }

}
