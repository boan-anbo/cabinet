package com.boan.apps.cabinet.cabinet.entities;

import com.boan.apps.cabinet.tagger.models.TagExtract;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    @Column(name = "Key", nullable = false )
    String key;
    @Column(name = "Value", nullable = true)
    String value;
    @Column(name = "Note", nullable = true)
    String note;

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
