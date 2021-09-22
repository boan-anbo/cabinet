package com.boan.apps.cabinet.query;

import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.entities.QCard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;

@Repository
public class CardQueries {
    private Querydsl querydsl;

    private final QCard card = QCard.card;
    @PersistenceContext
    private EntityManager entityManager;


    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        this.querydsl = new Querydsl(entityManager, (new PathBuilderFactory()).create(Card.class));
    }

    public QueryResult<Card> searchCard(String text, int page, int pageSize) {
        return searchCard(text, PageRequest.of(page, pageSize));

    }

    public QueryResult<Card> searchCard(String text) {
        return searchCard(text, PageRequest.of(2, 40));

    }

    public QueryResult<Card> searchCard(String text, Pageable pageable) {
        return searchCard(text, pageable, false);
    }

    public QueryResult<Card> searchCard(String text, Pageable pageable, Boolean exact) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        // remember to run 'mvn compile' to reflect the lateste version of the entity
        JPAQuery<Card> cards;
        if (exact) {

            cards = query.selectFrom(card)
                    // we have to left join source because source is the only related entity for Card that is NOT eager fetched. Without this, Cards with source as null will not be included because of the "or source" query below, even when other criteria on (eagerly fetched related entities) applies. For example, searching "computing" will not return computing card not because it does not meet the search but simply because its source is null and yet source is queried.
                    .leftJoin(card.source)
                    .where(
                            // card self properties
                            card.text.equalsIgnoreCase(text)
                                    .or(
                                            //Tags
                                            card.tags.any().key.equalsIgnoreCase(text)
                                                    .or(card.tags.any().value.equalsIgnoreCase(text)
                                                            .or(card.tags.any().note.equalsIgnoreCase(text)
                                                                    .or(card.tags.any().extra.equalsIgnoreCase(text))
                                                            )
                                                    )

                                    )
                                    //Comments
                                    .or(
                                            card.comments.any().text.equalsIgnoreCase(text)
                                                    .or(card.comments.any().extra.equalsIgnoreCase(text))


                                    )
                                    // Source
                                    .or(
                                            card.source.title.equalsIgnoreCase(text)
                                                    .or(card.source.text.equalsIgnoreCase(text)
                                                            .or(card.source.extra.equalsIgnoreCase(text))
                                                    )
                                    )
                    );
        } else {
            cards = query.selectFrom(card)
                    .leftJoin(card.source)
                    .where(
                            // card self properties
                            card.text.containsIgnoreCase(text)
                                    .or(
                                            //Tags
                                            card.tags.any().key.containsIgnoreCase(text)
                                                    .or(card.tags.any().value.containsIgnoreCase(text)
                                                            .or(card.tags.any().note.containsIgnoreCase(text)
                                                                    .or(card.tags.any().extra.containsIgnoreCase(text))
                                                            )
                                                    )

                                    )
                                    //Comments
                                    .or(
                                            card.comments.any().text.containsIgnoreCase(text)
                                                    .or(card.comments.any().extra.containsIgnoreCase(text))


                                    )
                                    // Source
                                    .or(
                                            card.source.title.containsIgnoreCase(text)
                                                    .or(card.source.text.containsIgnoreCase(text)
                                                            .or(card.source.extra.containsIgnoreCase(text))
                                                    )
                                    )
                    );

        }
        Long totalElements = cards.fetchCount();
        List<Card> result = querydsl.applyPagination(pageable, cards).fetch();
        return new QueryResult<Card>(result, totalElements);
    }


    public QueryResult<Card> listCardsByTagKey(String tagKey, Pageable pageable, Boolean exact) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        JPAQuery<Card> cards;
        if (exact) {
            cards = query.selectFrom(card)
                    .leftJoin(card.source)
                    .where(
                            //Tags
                            card.tags.any().key.equalsIgnoreCase(tagKey.toLowerCase(Locale.ROOT))
                    );
        } else {
            cards = query.selectFrom(card)
                    .leftJoin(card.source)
                    .where(
                            //Tags
                            card.tags.any().key.containsIgnoreCase(tagKey.toLowerCase(Locale.ROOT))
                    );
        }
        Long totalElements = cards.fetchCount();
        List<Card> result = querydsl.applyPagination(pageable, cards).fetch();
        return new QueryResult<Card>(result, totalElements);
    }

    public QueryResult<Card> listCardsByTagKeyMany(List<String> keys, Pageable pageable, boolean exact) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        JPAQuery<Card> cards;

        BooleanBuilder keyBooleans = new BooleanBuilder();
        // map the list of keys to querydsl boolean expressions.
        if (exact) {
            for (String key : keys) {
                keyBooleans.or(
                        card.tags.any().key.equalsIgnoreCase(key.toLowerCase(Locale.ROOT))
                );
            }
        } else {
            for (String key : keys) {
                keyBooleans.or(
                        card.tags.any().key.containsIgnoreCase(key.toLowerCase(Locale.ROOT))
                );
            }
        }
        // use the boolean expressions to filter.
        cards = query.selectFrom(card)
                .leftJoin(card.source)
                .where(
                        keyBooleans
                );

        Long totalElements = cards.fetchCount();
        List<Card> result = querydsl.applyPagination(pageable, cards).fetch();

        return new QueryResult<Card>(result, totalElements);
    }

    public QueryResult<Card> listCardsBySourceTitle(String sourceTitle, Pageable pageable, Boolean exact) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        JPAQuery<Card> cards;
        if (exact) {
            cards = query.selectFrom(card)
                    .leftJoin(card.source)
                    .where(
                            //Tags
                            card.source.title.equalsIgnoreCase(sourceTitle.toLowerCase(Locale.ROOT))
                    );
        } else {
            cards = query.selectFrom(card)
                    .leftJoin(card.source)
                    .where(
                            //Tags
                            card.tags.any().key.containsIgnoreCase(sourceTitle.toLowerCase(Locale.ROOT))
                    );
        }
        Long totalElements = cards.fetchCount();
        List<Card> result = querydsl.applyPagination(pageable, cards).fetch();
        return new QueryResult<Card>(result, totalElements);
    }


    public void save(Card entity) {
        entityManager.persist(entity);
    }

}
