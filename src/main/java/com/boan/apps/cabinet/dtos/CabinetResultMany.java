package com.boan.apps.cabinet.dtos;

import com.boan.apps.cabinet.consts.SelectorType;
import com.boan.apps.cabinet.entities.Card;
import com.boan.apps.cabinetKt.MarkdownWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CabinetResultMany<T> {

    public List<String> queries = new ArrayList<>();
    public List<String> markdown;
    public List<T> items;
    public int totalResults;
    public int page;
    public int pageSize;
    public int totalPages;
    public Boolean hasNextPage;
    public Boolean hasPrevPage;
    public Boolean isFirst;
    public Boolean isLast;
    public Boolean isEmpty;
    // list to filter the result;
    public SelectorType selectorType;


    private CabinetResultMany(Page<T> pageInput, CabinetCardParams params) {

        items = pageInput.getContent();
        totalResults = Math.toIntExact(pageInput.getTotalElements());
        page = pageInput.getNumber();
        pageSize = pageInput.getSize();
        totalPages = pageInput.getTotalPages();
        hasNextPage = pageInput.hasNext();
        hasPrevPage = pageInput.hasPrevious();
        isFirst = pageInput.isFirst();
        isLast = pageInput.isLast();
        isEmpty = pageInput.isEmpty();
        selectorType = params.selectorType;


        if (params.includeMarkdown) {
            if (this.items != null && this.items.size() > 0) {
                this.markdown = MarkdownWriter.ExportLines(params.getMarkdownTitle(), (List<Card>) this.items);
            } else {
                this.markdown = new ArrayList<String>();
                this.markdown.add("No Results.");
            }
        }

    }

    private CabinetResultMany() {

    }

    public CabinetResultMany(List<T> cards, CabinetCardParams params) {
        this.items = cards;
        if (params.includeMarkdown) {
            if (this.items != null && this.items.size() > 0) {
                this.markdown = MarkdownWriter.ExportLines(params.getMarkdownTitle(), (List<Card>) this.items);
            } else {
                this.markdown = new ArrayList<String>();
                this.markdown.add("No Results.");
            }
        }
        if (cards != null) {
            this.totalResults = cards.size();
            if (this.totalResults > 0) {
                this.pageSize = this.totalResults;
                this.page = 1;
                this.totalPages = 1;
            }
        }

    }

    public static CabinetResultMany emptyResult() {
        return new CabinetResultMany();
    }


    public static <E> CabinetResultMany<E> fromList(
            List<E> cards,
            CabinetCardParams params
    ) {
        var resultMany = new CabinetResultMany<E>(cards, params);

        return resultMany;
    }

    public static <E> CabinetResultMany<E> fromPage(
            Page<E> pageInput,
            CabinetCardParams params
    ) {
        var resultMany = new CabinetResultMany<E>(pageInput, params);

        return resultMany;
    }


    public static <E> CabinetResultMany<E> fromPage(
            Page<E> pageInput,
            CabinetCardParams params,
            List<String> queries
    ) {
        var resultMany = new CabinetResultMany<E>(pageInput, params);
        resultMany.queries = queries;

        return resultMany;
    }
}
