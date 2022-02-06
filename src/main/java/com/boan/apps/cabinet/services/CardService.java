package com.boan.apps.cabinet.services;

import boan.pdfgongju.core.models.PdfAnnotation;
import com.boan.apps.cabinet.controllers.CardController;
import com.boan.apps.cabinet.dtos.CabinetCardParams;
import com.boan.apps.cabinet.dtos.CabinetResultMany;
import com.boan.apps.cabinet.dtos.CabinetResultOne;
import com.boan.apps.cabinet.dtos.GetCardsById;
import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.repositories.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CardService {
    Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardRepository cardRepo;


    public CabinetResultMany<Card> getCardsByTags(List<String> keys, CabinetCardParams params) {
        var cardsPage = cardRepo.listCardsByTagKeyMany(keys, params, params.toPageable());
        // return keys back to the consumer.
        return CabinetResultMany.fromPage(cardsPage, params, keys);
    }

    public CabinetResultMany<Card> getCardsById(GetCardsById cardIds, CabinetCardParams params ) {
        var search = params.getSearch();
        if (search != null && !search.isEmpty()) {
            return getCardsBySearching(params);
        }
        var results = cardRepo.findAllById(cardIds.getCardIds());
        List<Card> cards = new ArrayList<Card>();
        results.iterator().forEachRemaining(cards::add);
        return CabinetResultMany.fromList(cards, params);
    }

    public CabinetResultMany<Card> getCards(CabinetCardParams params) {
        var search = params.getSearch();
        if (search != null && !search.isEmpty()) {
            return getCardsBySearching(params);
        }
        var result = cardRepo.getAll(params);
        return CabinetResultMany.fromPage(result, params);
    }


    public CabinetResultMany<Card> getCardsBySearching(CabinetCardParams params) {
        var search = params.getSearch();
        var exact = params.getSearchExact();
        Page<Card> cardsPage = null;
        if (exact == null || !exact) {
            cardsPage = cardRepo.searchCardText(search, params, params.toPageable());
        } else {
            cardsPage = cardRepo.searchCardTextExact(search, params, params.toPageable());
        }

        return CabinetResultMany.fromPage(cardsPage, params);
    }

    public CabinetResultOne<Card> getCard(String cardId, CabinetCardParams params) {

        var card =  cardRepo.findById(cardId);
        return CabinetResultOne.fromItem(card);
    }


    /**
     * get card by modiefied modifiedDate;
     */
    public CabinetResultMany<Card> getCardsByModifiedDate(Date modifiedDate) {
        var getAllParams = new CabinetCardParams();
        getAllParams.setModifiedDate(modifiedDate);
        return getCards(getAllParams);
    }


    /**
     * check if a pdf annotation already exists in the database as a card
     */
    public boolean checkIfCardExists(PdfAnnotation annotation) {
        var cardsWithSameTextAndModifiedDate = getCardsWithSameTextAndModifiedDate(annotation);

        return cardsWithSameTextAndModifiedDate.size() > 0;
    }

    public List<Card> getCardsWithSameTextAndModifiedDate(PdfAnnotation annotation) {
        var cardsWithSameModifiedDate = getCardsByModifiedDate(annotation.getModifiedDate());


        var filteredCardsWithSameText = cardsWithSameModifiedDate
                .getItems()
                .stream()
                .filter(card -> card.getText()
                        .equals(annotation.getText()));
        return filteredCardsWithSameText.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

}
