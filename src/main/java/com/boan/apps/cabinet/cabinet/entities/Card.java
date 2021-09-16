package com.boan.apps.cabinet.cabinet.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "Cards")
@Entity
@Getter
@Setter
@Indexed
@NoArgsConstructor
public class Card implements Serializable {

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

    @Column(name="modified", nullable = false)
    Date modified = new Date();

    @Column(name="created", nullable = false)
    Date created = new Date();


    @Column(name = "text", nullable = false)
    String text;


    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL

    )
    public Set<Tag> tags = new HashSet<Tag>();

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL

    )
    public Set<Comment> comments = new HashSet<Comment>();

    @ManyToOne(
            fetch=FetchType.EAGER
    )
            @JoinColumn(name = "source_id")
    Source source;



    public Card(Source _source) {
        source = _source;
        modified = source.getModified();
    }
}
