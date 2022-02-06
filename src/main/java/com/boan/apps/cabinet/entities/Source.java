package com.boan.apps.cabinet.entities;

import boan.pdfgongju.core.models.PdfDocument;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.File;
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

    // publication date
    @Parameter(description = "date of publication")
    @Column(name = "publication_date", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date publicationDate = new Date();

    // citation information
    @Parameter(description = "citation information")
    @Column(name = "citation", nullable = true)
    String citation = "";

    @Parameter(description = "the page from which text was taken from")
    @Column(name = "page_index", nullable = true)
    public int pageIndex;

    @Parameter(description = "the source of the id")
    @Column(name = "unique_id", nullable = true)
    public String uniqueId;

    // Explains what the unique Id refers to.
    @Column(name = "unique_id_note", nullable = true)
    String uniqueIdNote;

    @Column(name = "file_path", nullable = true)
    public String filePath;

    @Column(name = "dir_path", nullable = true)
    public String dirPath;

    @Column(name = "file_name", nullable = true)
    public String fileName;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "modified", nullable = false)
    public Date modified = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "created", nullable = false)
    Date created = new Date();


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Card> cards = new ArrayList<Card>();

    public Source(PdfDocument pdfDoc, int _pageIndex) {

        // save full text and page index

        pageIndex = _pageIndex;

        try {

            text = pdfDoc.getFullTexts().get(pageIndex).getText();
        } catch (Exception e) {
            System.out.println("Error: " + "Cannot get page index.");
        }

        try {

            var info = pdfDoc.getDocInfo();
            var _filePath = info.getFilePath();
            if (_filePath != null && _filePath.length() > 0) {

                setFilePath(_filePath);

                // store dir path
                var _dirPath = new File(_filePath).getParent();

                if (_dirPath != null && _dirPath.length() > 0) {
                    setDirPath(_dirPath);
                }
            }

            var _fileName = info.getFileName();
            if (_fileName != null && _fileName.length() > 0) {

                setTitle(_fileName);
                setFileName(_fileName);
            }


            var _citeKey = info.getCiteKey();
            if (_citeKey != null && _citeKey.length() > 0) {

                setUniqueId(_citeKey);
                setUniqueIdNote("Zotero Citekey");
            }
        } catch (Exception e) {
            System.out.println("Error: " + "Cannot get document info.");
        }

    }
}
