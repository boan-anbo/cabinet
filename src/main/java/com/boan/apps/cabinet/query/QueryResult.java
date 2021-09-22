package com.boan.apps.cabinet.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QueryResult<T> {

    List<T> result;
    Long totalResults;
}
