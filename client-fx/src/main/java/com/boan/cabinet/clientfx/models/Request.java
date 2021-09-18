package com.boan.cabinet.clientfx.models;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Request {
    public List<String> filesPaths ;

}
