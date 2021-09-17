package com.boan.apps.cabinet.cabinet.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveResults {
    public int savedCards;
    public int savedFiles;

    public SaveResults(int cardsSaved, int size) {
        savedCards = cardsSaved;
        savedFiles = size;
    }
}
