package com.boan.apps.cabinet.repositories;

import com.boan.apps.cabinet.dtos.CabinetCardParams;
import com.boan.apps.cabinet.entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

interface CardRepostioryCustom {

    Page<Card> searchCardText(String text, Pageable pageable);

    Page<Card> searchCardTextExact(String text, Pageable pageable);

    Page<Card> listCardsByTagKey(String tagKey, Pageable pageable);

    Page<Card> listCardsByTagKeyExact(String tagKey, Pageable pageable);

    Page<Card> listCardsByTagKeyMany(List<String> keys, CabinetCardParams params, Pageable pageable);

    Page<Card> listCardsBySourceTitle(String sourceTitle, Pageable pageable);

    Page<Card> listCardsBySourceTitleExact(String sourceTitle, Pageable pageable);
}
