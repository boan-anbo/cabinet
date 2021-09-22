package com.boan.apps.cabinet.services;


import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.entities.Comment;
import com.boan.apps.cabinet.entities.Source;
import com.boan.apps.cabinet.entities.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExportServiceTest {

    private final String filePath = new File("src/test/java/com/boan/apps/cabinet/services/test_md_export.md").getAbsolutePath();

    @Autowired
    private ExportService exportService;

    private Card cardChina;
    private Card cardComputing;

    @Before
    public void setUp() {


        var cardChina = new Card();
        cardChina.setText("China Card");

        // add tags
        var tagChina = new Tag();
        tagChina.setKey("chinatagkey");
        tagChina.setValue("chinatagvalue");
        tagChina.setNote("chinatagnote");

        cardChina.tags.add(tagChina);

        // add chinese tags
        var tagChinaChn = new Tag();
        tagChinaChn.setKey("中国标签");
        tagChinaChn.setValue("中国价值");
        tagChinaChn.setNote("中国说明");

        cardChina.tags.add(tagChinaChn);

        // add comment
        var commentChina = new Comment();
        commentChina.setText("Chinacomment");
        cardChina.comments.add(commentChina);


        // add project
        var chinaSource = new Source();
        chinaSource.setTitle("Source of China Card");
        cardChina.setSource(chinaSource);


        var cardComputing = new Card();
        cardComputing.setText("Computing Card");


        this.cardChina = cardChina;
        this.cardComputing = cardComputing;

    }


    @Test
    public void Should_Export_Mardown() throws IOException {
        var cards = new ArrayList<Card>();
        cards.add(cardChina);
        cards.add(cardComputing);

        var result = exportService.exportMarkdownLines("Test Title",
                cards
        );


        File file = new File(filePath);

        file.createNewFile();

        try (
                FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8, false);
                BufferedWriter writer = new BufferedWriter(fw)) {
            for (String line : result) {
                writer.append(line);
                writer.newLine();
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }


    }
}
