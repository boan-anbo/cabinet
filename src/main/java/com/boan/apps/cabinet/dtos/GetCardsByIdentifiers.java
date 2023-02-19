package com.boan.apps.cabinet.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetCardsByIdentifiers {

    // source file paths
    public List<String> filePaths = null;
    // source citekeys
    public List<String> citeKeys = null;
    public List<String> cardIds = null;

    public List<String> tagKeys = null;

    public boolean hasAnyLists() {

        return (filePaths != null && filePaths.size() > 0) ||
                (citeKeys != null && citeKeys.size() > 0) ||
                (cardIds != null && cardIds.size() > 0) ||
                (tagKeys != null && tagKeys.size() > 0);
    }
}

