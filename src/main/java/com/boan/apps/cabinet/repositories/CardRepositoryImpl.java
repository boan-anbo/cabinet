
package com.boan.apps.cabinet.repositories;

import com.boan.apps.cabinet.dtos.CabinetCardParams;
import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.query.CardQueries;
import com.boan.apps.cabinet.query.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Repository
@Transactional
public class CardRepositoryImpl implements CardRepostioryCustom {
    @Autowired
    private CardQueries cardDao;

    private final int defaultPageSize = 50;

    @Override
    public Page<Card> searchCardText(@Valid @NotEmpty String text, Pageable pageable) {

        QueryResult<Card> queryResult = cardDao.searchCard(text,
                pageable,
                false
        );

        return new PageImpl<Card>(queryResult.getResult(), pageable, queryResult.getTotalResults()) {};
    }

    @Override
    public Page<Card> searchCardTextExact(String text, Pageable pageable) {
        QueryResult<Card> queryResult = cardDao.searchCard(text,
                pageable,
                true
        );

        return new PageImpl<Card>(queryResult.getResult(), pageable, queryResult.getTotalResults()) {};
    }


    @Override
    public Page<Card> listCardsByTagKey(String tagKey, Pageable pageable) {
        QueryResult<Card> queryResult = cardDao.listCardsByTagKey(tagKey,
                pageable,
                false
        );

        return new PageImpl<Card>(queryResult.getResult(), pageable, queryResult.getTotalResults()) {};
    }

    @Override
    public Page<Card> listCardsByTagKeyExact(String tagKey, Pageable pageable) {
        QueryResult<Card> queryResult = cardDao.listCardsByTagKey(tagKey,
                pageable,
                true
        );
        return new PageImpl<Card>(queryResult.getResult(), pageable, queryResult.getTotalResults()) {};
    }

    @Override
    public Page<Card> listCardsByTagKeyMany(List<String> keys, CabinetCardParams params, Pageable pageable) {
        QueryResult<Card> queryResult = cardDao.listCardsByTagKeyMany(keys,
                pageable,
                params.getMactchExact()
        );
        return new PageImpl<Card>(queryResult.getResult(), pageable, queryResult.getTotalResults()) {};
    }


    @Override
    public Page<Card> listCardsBySourceTitle(String sourceTitle, Pageable pageable) {
        QueryResult<Card> queryResult = cardDao.listCardsBySourceTitle(sourceTitle,
                pageable,
                false
        );

        return new PageImpl<Card>(queryResult.getResult(), pageable, queryResult.getTotalResults()) {};
    }

    @Override
    public Page<Card> listCardsBySourceTitleExact(String sourceTitle, Pageable pageable) {
        QueryResult<Card> queryResult = cardDao.listCardsBySourceTitle(sourceTitle,
                pageable,
                true
        );

        return new PageImpl<Card>(queryResult.getResult(), pageable, queryResult.getTotalResults()) {};
    }
}
