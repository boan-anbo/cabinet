package com.boan.apps.cabinet.controllers;

import com.boan.apps.cabinet.consts.ExportFormat;
import com.boan.apps.cabinet.dtos.*;
import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinet.services.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("cards")
@Tag(name = "Card")
public class CardController {
    Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardService cardService;

    @Operation(
            operationId = "getCards",
            summary = "list cards",
            description = "list cards by params."
    )
    @GetMapping("")
    public CabinetResultMany<Card> getCards(@ParameterObject CabinetCardParams params) {
        return cardService.getCards(params);
    }

    @Operation(
            operationId = "getCardsBySelectors",
            summary = "list cards by identifiers plus standard params. Identifiers include cardsIds etc",
            description = "list cards by sources."
    )
    @PostMapping("")
    public CabinetResultMany<Card> getCardsBySources(@RequestBody GetCardsByIdentifiers identifiersRequest, @ParameterObject CabinetCardParams params) {

        // load post json body to the param.
        params.loadSourceRequest(identifiersRequest, params.getSelectorType());

        return cardService.getCards(params);
    }

//    @Operation(
//            operationId = "getCardsById",
//            summary = "get cards by card ids.",
//            description = "get cards by ids."
//    )
//    @PostMapping("")
//    public CabinetResultMany<Card> getCardsById(@RequestBody GetCardsById request, @ParameterObject CabinetCardParams params) {
//        return cardService.getCardsById(request, params);
//    }



    @Operation(
            operationId = "exportCardsByParams",
            summary = "export cards by params",
            description = "export cards by params"
    )
    @GetMapping(value = "export/params/{format}", produces = MediaType.TEXT_MARKDOWN_VALUE + ";charset=UTF-8")
    @ResponseBody
    public String exportCards(@PathVariable ExportFormat format, @ParameterObject CabinetCardParams params) {
        params.includeMarkdown = true;
        var results = cardService.getCards(params);
        return String.join("\n", results.getMarkdown());
    }

    @Operation(
            operationId = "exportCardsById",
            summary = "export cards by ids",
            description = "export cards by ids"
    )
    @PostMapping(value = "export/ids/{format}", produces = MediaType.TEXT_MARKDOWN_VALUE + ";charset=UTF-8")
    @ResponseBody
    public String exportCards(@PathVariable ExportFormat format, @ParameterObject CabinetCardParams params, @RequestBody GetCardsById request) {
        params.includeMarkdown = true;
        var results = cardService.getCardsById(request, params);
        return String.join("\n", results.getMarkdown());
    }

    @Operation(
            operationId = "getCardsByTags",
            summary = "get cards by tags",
            description = "get cards by a list of tag keys. Tag key matching can be exact or fuzzy.")
    @GetMapping("/tags")
    public CabinetResultMany<Card> getCardsByTags(@RequestParam(name = "key") List<String> tagKeys, @ParameterObject CabinetCardParams params) {
        return cardService.getCardsByTags(tagKeys, params);
    }

    @Operation(
            operationId = "exportCardsByTags",
            summary = "export cards by tags",
            description = "export cards by tags"
    )
    @GetMapping(value = "/tags/export/", produces = MediaType.TEXT_MARKDOWN_VALUE)
    @ResponseBody
    public String exportCardsByTags(
            @RequestParam ExportFormat format,
            @RequestParam(name="key") List<String> tagKeys,
            @ParameterObject CabinetCardParams params
    ) {
        params.includeMarkdown = true;
        var results =  cardService.getCardsByTags(tagKeys, params);

        return String.join("\n", results.getMarkdown());
    }

    @Operation(
            operationId = "getCardById",
            summary = "get card by card id.",
            description = "get one card by card id."
    )
    @GetMapping("/{cardId}")
    public CabinetResultOne<Card> getCardId(@PathVariable String cardId, @ParameterObject CabinetCardParams params) {
        return cardService.getCard(cardId, params);
    }

}
