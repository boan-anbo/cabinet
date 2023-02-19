package com.boan.apps.cabinet.dtos;

import com.boan.apps.cabinet.consts.SelectorType;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;


@Data
public class CabinetCardParams {
    public boolean includeMarkdown = false;
    public String markdownTitle = "Untitled";
    String search;
    // for searching texts
    Boolean searchExact = false;
    int page = 0;
    int pageSize = 30;
    // for things like matching tag key.
    Boolean mactchExact = true;
    // filters
    Boolean commentedOnly = false;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Date fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Date toDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Date modifiedDate;

    String order = "desc";

    String sortBy = "modified";

    SelectorType selectorType = SelectorType.NONE;

    List<String> sourceIdentifiers = null;



    public Pageable toPageable() {
        var sortOrder = this.order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, pageSize, Sort.by(sortOrder, this.sortBy));
    }

    public void loadSourceRequest(GetCardsByIdentifiers filterIndentifierRequest, SelectorType identifierType) {

        if (identifierType != SelectorType.NONE && identifierType != null) {
            switch (identifierType) {
                case CITEKEY -> {
                    this.setSourceIdentifiers(filterIndentifierRequest.getCiteKeys());
                }
                case FILEPATH -> {
                    this.setSourceIdentifiers(filterIndentifierRequest.getFilePaths());
                }
                case CARDID -> {
                    this.setSourceIdentifiers(filterIndentifierRequest.getCardIds());
                }
                case TAGKEY -> {
                    this.setSourceIdentifiers(filterIndentifierRequest.getTagKeys());
                }
            }

            if (this.getSourceIdentifiers() == null || this.getSourceIdentifiers().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No filter identifiers in POST json body provided for the selector type " + identifierType.name());
            }

        }

        if (identifierType == SelectorType.NONE) {
            if (filterIndentifierRequest.hasAnyLists()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "identifiers in POST json body provided but the selector type is None");
            }
        }
    }
}
