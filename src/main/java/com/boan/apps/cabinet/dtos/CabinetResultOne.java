package com.boan.apps.cabinet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Data
public class CabinetResultOne<T> {
    public T item;

    private CabinetResultOne(T itemInput) {

        item = itemInput;

    }

    private CabinetResultOne() {

    }

    public static <E> CabinetResultOne<E> emptyResult() {
        return new CabinetResultOne<E>();
    }

    public static <E> CabinetResultOne<E> fromItem(E itemInput) {
        return new CabinetResultOne<E>(itemInput);
    }

    public static <E> CabinetResultOne<E> fromItem(Optional<E> itemInput) {
        return itemInput.map(CabinetResultOne::fromItem)
                .orElseGet(CabinetResultOne::emptyResult);
    }
}
