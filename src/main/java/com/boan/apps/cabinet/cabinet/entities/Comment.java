package com.boan.apps.cabinet.cabinet.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "Comments")
@Entity
@Getter
@Setter
@Indexed
@NoArgsConstructor
public class Comment implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    String id;

    @Column(name = "extra", nullable = true)
    String extra;

    @Column(name = "Text", nullable = false)
    String Text = "";

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,

            mappedBy= "comments"
    )
    Set<Card> cards = new HashSet<Card>();
}
