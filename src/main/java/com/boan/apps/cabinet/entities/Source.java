package com.boan.apps.cabinet.entities;

import boan.pdfgongju.core.models.PdfDocument;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Table(name = "Sources")
@Entity
@Getter
@Setter
@Indexed
@NoArgsConstructor
public class Source implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    String id;

    @Column(name = "extra", nullable = false)
    String extra = "";

    @Column(name = "title", nullable = false)
    public String title = "";

    @Parameter(description = "for context text, e.g. the text from which the excerpt was made from")
    @Column(name = "text", nullable = true)
    String text = "";

    @Parameter(description = "the page from which text was taken from")
    @Column(name="page_index", nullable = true)
    public int pageIndex;

    @Parameter(description = "the source of the id")
    @Column(name= "unique_id", nullable = true)
    public String uniqueId;

    // Explains what the unique Id refers to.
    @Column(name= "unique_id_note", nullable = true)
    String uniqueIdNote;

    @Column(name= "file_path", nullable = true)
    public String filePath;

    @Column(name="modified", nullable = false)
    public Date modified = new Date();

    @Column(name="created", nullable = false)
    Date created = new Date();


    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Card> cards = new ArrayList<Card>() ;

    public Source(PdfDocument pdfDoc, int _pageIndex) {

        // save full text and page index

        pageIndex = _pageIndex;
        text = pdfDoc.getFullTexts().get(pageIndex).getText();

        //

        var info = pdfDoc.getDocInfo();
        var _filePath = info.getFilePath();
        if (_filePath != null && _filePath.length() > 0) {

            setFilePath(_filePath);
        }

        var _fileName = info.getFileName();
        if (_fileName != null && _fileName.length() > 0) {

            setTitle(_fileName);
        }

        var _citeKey = info.getCiteKey();
        if (_citeKey != null && _citeKey.length() > 0) {

            setUniqueId(_citeKey);
            setUniqueIdNote("Zotero Citekey");
        }

    }
}
