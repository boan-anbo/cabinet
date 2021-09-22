package com.boan.apps.cabinet.dtos;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Dictionary;
import java.util.List;


@Data
public class CabinetCardParams {
    public boolean includeMarkdown = false;
    public String markdownTitle = "Untitled";
    String search;
    int page = 0;
    int pageSize = 30;
    // for searching texts
    Boolean searchExact = false;
    // for things like matching tag key.
    Boolean mactchExact = true;

    public Pageable toPageable() {
        return PageRequest.of(page, pageSize);
    }

}
