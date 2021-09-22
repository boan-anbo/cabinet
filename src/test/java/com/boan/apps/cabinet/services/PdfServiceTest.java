package com.boan.apps.cabinet.services;

import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.repositories.CardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaAuditing // otherwise the @createdDate @modifiedDate will not work in test
public class PdfServiceTest {

    private final String filePath = new File("src/test/java/com/boan/apps/cabinet/services/test-with-annots.pdf").getAbsolutePath();

    @Autowired
    private PdfService pdfService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CardRepository cardRepo;

    @Test
    public void extractPdf() throws IOException {

        var testFile = new File(filePath);
        assertTrue(testFile.exists());

        var extractPdf = pdfService.ExtractPdf(testFile.getAbsolutePath());
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(extractPdf))        ;
    }

    @Test
//    @Transactional
    public void extractAndStorePdf() throws IOException {

        var testFile = new File(filePath);
        assertTrue(testFile.exists());

       var cards = pdfService.ExtractAndStorePdf(Collections.singletonList(testFile.getAbsolutePath()));

       List<Card> results = new ArrayList<>();
       cardRepo.findAll().forEach(results::add);

        ObjectMapper mapper = new ObjectMapper();
//        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(results))        ;
    }

}
