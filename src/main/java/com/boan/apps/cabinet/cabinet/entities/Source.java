package com.boan.apps.cabinet.cabinet.entities;

import boan.pdfgongju.core.models.PdfDocument;
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
    String title = "";

    @Column(name= "unique_id", nullable = true)
    String uniqueId;

    // Explains what the unique Id refers to.
    @Column(name= "unique_id_note", nullable = true)
    String uniqueIdNote;

    @Column(name= "file_path", nullable = true)
    String filePath;

    @Column(name="modified", nullable = false)
    Date modified = new Date();

    @Column(name="created", nullable = false)
    Date created = new Date();


    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Card> cards = new ArrayList<Card>() ;

    public Source(PdfDocument pdfDoc) {

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

        var _modified = info.getModifiedDate();
        if (_modified != null) {

            setModified(modified);
        }

    }
}
