package com.boan.apps.cabinet.query;

import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.entities.Comment;
import com.boan.apps.cabinet.entities.Source;
import com.boan.apps.cabinet.entities.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class CardDAOTest {
    @Autowired
    private CardQueries cardApi;


    @PersistenceContext
    private EntityManager entityManager;
    private Card cardChina;
    private int totalChinaCards;

    private Card cardComputing;

    // const properties
    private final String CHINA_SOURCE_TITLE = "chinasource";

    @Before
    public void setUp() {

        this.totalChinaCards = 990;
        for (int i = 0; i < this.totalChinaCards; i++) {

            var cardChina = new Card();
            cardChina.setText("China Card" + i);

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
            chinaSource.setTitle(CHINA_SOURCE_TITLE);
            entityManager.persist(chinaSource);
            cardChina.setSource(chinaSource);

            // persis china card
            cardApi.save(cardChina);


            var cardComputing = new Card();
            cardComputing.setText("Computing Card" + i);

            cardApi.save(cardComputing);

            if (i == 0) {
                this.cardChina = cardChina;
                this.cardComputing = cardComputing;
            }
        }
    }

    @Test
    public void should_Search_By_Text() {


        List<Card> computingResult = cardApi.searchCard("Computing", 0, 100).getResult();
        assertThat(cardChina, is(not(in(computingResult))));
        assertThat(cardComputing, is(in(computingResult)));


//        List<Card> computingSingleResult = cardApi.searchCard("Computing Card1", 0, 100);
//        assertThat(100, is(computingSingleResult.size()));


        List<Card> chinaResult = cardApi.searchCard("China", 0, 100).getResult();
        assertThat(cardChina, is(in(chinaResult)));
        assertThat(cardComputing, is(not(in(chinaResult))));

        List<Card> chinaTagResult = cardApi.searchCard("Chinatag", 0, 100).getResult();
        assertThat(cardChina, is(in(chinaTagResult)));
        assertThat(cardComputing, is(not(in(chinaTagResult))));


        List<Card> chinaTagChnResult = cardApi.searchCard("中国标签", 0, 100).getResult();
        assertThat(cardChina, is(in(chinaTagChnResult)));
        assertThat(cardComputing, is(not(in(chinaTagChnResult))));

        List<Card> chinaCommentResult = cardApi.searchCard("Chinacomment", 0, 100).getResult();
        assertThat(cardChina, is(in(chinaCommentResult)));
        assertThat(cardComputing, is(not(in(chinaCommentResult))));

        List<Card> chinaSourceResult = cardApi.searchCard("Chinasource", 0, 100).getResult();
        assertThat(cardChina, is(in(chinaSourceResult)));
        assertThat(cardComputing, is(not(in(chinaSourceResult))));


        List<Card> bothResults = cardApi.searchCard("Card", 0, 100000).getResult();
//        assertThat(cardChina, is(in(bothResults)));
//        assertThat(cardComputing, is(in(bothResults)));
        assertThat(this.totalChinaCards * 2, is(bothResults.size()));


    }

    @Test
    public void should_Search_By_Text_Exact() {


        List<Card> computingResult = cardApi.searchCard("computing card0", PageRequest.of(0, 1), true).getResult();
        assertThat(cardChina, is(not(in(computingResult))));
        assertThat(cardComputing, is(in(computingResult)));

        List<Card> chinaResult = cardApi.searchCard("china card0", PageRequest.of(0, 1), true).getResult();
        assertThat(cardChina, is(in(chinaResult)));
        assertThat(cardComputing, is(not(in(chinaResult))));
        // the card must also has tag attached.
        assertThat(chinaResult.stream().allMatch(card -> card.tags.stream().anyMatch(tag -> tag.getKey().equalsIgnoreCase("中国标签"))), is(true));


    }

    @Test
    public void should_list_cards_by_tag_key_exact() {


        List<Card> chinaResult = cardApi.listCardsByTagKey("中国标签", PageRequest.of(0, 100), true).getResult();

        // make sure every result has at least one tag that has the tag in it.
        assertThat(
                chinaResult.stream()
                        .allMatch(c ->
                                c.tags.stream().anyMatch(t -> Objects.equals(t.getKey(), "中国标签"))
                        ),
                is(true));

    }

    @Test
    public void should_list_cards_by_source_title_exact() {


        List<Card> chinaResult = cardApi.listCardsBySourceTitle(CHINA_SOURCE_TITLE, PageRequest.of(0, 100), true).getResult();

        // make sure every result has at least one tag that has the tag in it.
        assertThat(
                chinaResult.stream()
                        .allMatch(c ->
                                c.getSource().getTitle().equalsIgnoreCase(CHINA_SOURCE_TITLE)
                        ),
                is(true));

    }
}
