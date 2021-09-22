package com.boan.apps.cabinet.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "Cards")
@Entity
@Getter
@Setter
@Indexed
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @Column(name = "modified", nullable = false)
    Date modified = new Date();

    @Column(name = "created", nullable = false)
    Date created = new Date();


    @Column(name = "text", nullable = false)
    public String text;

    @Column(name = "title", nullable = true)
    public String title;


    @Column(name = "importance", nullable = false)
    @Max(5)
    @Min(0)
    public int importance = 0;


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
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "source_id", nullable = true)
    public Source source;


    public Card(Source _source) {
        source = _source;
        source.modified = new Date();
    }
}
