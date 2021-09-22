package com.boan.apps.cabinet.init;

import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.entities.Comment;
import com.boan.apps.cabinet.entities.Source;
import com.boan.apps.cabinet.entities.Tag;
import com.boan.apps.cabinet.query.CardQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Transactional
public class Init implements InitializingBean {

    @Autowired
    CardQueries cardApi;

    @PersistenceContext
    EntityManager entityManager;

    private static final Logger LOG
      = LoggerFactory.getLogger(Init.class);

    public Init() {
        LOG.info("Constructor");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("InitializingBean");


    }

    @PostConstruct
    public void postConstruct() {

        LOG.info("PostConstruct");
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        LOG.info("init-method");

        for (int i = 0; i < 2; i++) {

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
            chinaSource.setTitle("chinasource");
            entityManager.persist(chinaSource);
            cardChina.setSource(chinaSource);

            // persis china card
            cardApi.save(cardChina);


            var cardComputing = new Card();
            cardComputing.setText("Computing Card" + i);


            var tagComputing = new Tag();
            tagComputing.setKey("computingtagKey");
            tagComputing.setValue("chinatagvalue");
            tagComputing.setNote("chinatagnote");

            cardComputing.tags.add(tagComputing);

            cardApi.save(cardComputing);

        }
    }
}
