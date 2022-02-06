package com.boan.apps.cabinet.consts;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;
@Getter
@RequiredArgsConstructor
public enum SelectorType {
    NONE("NONE"),
    CITEKEY("CITEKEY"),
    CARDID("CARDID"),
    FILEPATH("FILEPATH");

    private String code;

    private SelectorType(String code) {
        this.code=code;
    }

    @JsonCreator
    public static SelectorType decode(final String code) {
        return Stream.of(SelectorType.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
