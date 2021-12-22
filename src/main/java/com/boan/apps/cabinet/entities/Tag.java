package com.boan.apps.cabinet.entities;

import com.boan.apps.cabinet.tagger.models.TagExtract;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table(name = "Tags")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tag  implements Serializable {

    // Shared
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    String id;

    @Column(name = "extra", nullable = false)
    String extra ="";

    @Column(name = "key", nullable = false )
    public String key;

    @Column(name = "value", nullable = true)
    public String value;

    @Column(name = "note", nullable = true)
    public String note;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "tags",
            cascade = CascadeType.ALL)
    Set<Card> cards = new HashSet<Card>();

    public Tag(TagExtract _tag) {
        key  = _tag.getTagKey();
        value = _tag.getTagValue();
        note = _tag.getTagNote();

    }
}
